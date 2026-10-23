package org.le_grid.dev

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import legrid.shared.generated.resources.Res
import legrid.shared.generated.resources.hello_world
import legrid.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.koinConfiguration
import org.le_grid.dev.dependencies.MyViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch
import org.le_grid.dev.networking.InsultCensorViewModel
import org.le_grid.dev.util.NetworkError
import org.le_grid.dev.util.onError
import org.le_grid.dev.util.onResult
import org.le_grid.dev.util.onSuccess


enum class MainActivityDestinations(val route : String, val desc : String)  {
    DependencyInjection("dp_nj", "Dependency Injection"),
    Image("img", "Image"),
    Resource("rsc", "Resource"),
    BatteryLevel("btr_lvl", "Battery Level"),
    Counter("cntr", "Counter"),
    KTor("ktr", "K Tor"),
    Permission("prmsn", "Permissions"),

}

@Composable
fun App(
    batteryManager: BatteryManager
) {
    val navController = rememberNavController()
    val startDestination : MainActivityDestinations = MainActivityDestinations.DependencyInjection
    var currentDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    KoinApplication(configuration = koinConfiguration { }){
        MaterialTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                        MainActivityDestinations.entries.forEachIndexed { index, destination ->
                            NavigationBarItem(
                                selected = currentDestination == index,
                                onClick = {
                                    navController.navigate(route = destination.route)
                                    currentDestination = index
                                },
                                icon = {
                                    Icon(
                                        Icons.Rounded.AddCircle,
                                        contentDescription = destination.desc
                                    )
                                },
                                label = { Text(destination.route) }
                            )
                        }
                    }
                }
            ){contentPadding ->
                NavHost(
                    navController = navController,
                    startDestination = startDestination.route,

                    ) {
                    composable (route = MainActivityDestinations.Image.route) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Image(painterResource(Res.drawable.logo), null)
                        }
                    }
                    composable (route = MainActivityDestinations.Resource.route) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.hello_world),
                                color = Color.White
                            )
                        }
                    }
                    composable (route = MainActivityDestinations.BatteryLevel.route) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "This is Hello World battery level :${batteryManager.getBatteryLevel()}%",
                                color = Color.White
                            )
                        }
                    }
                    composable (route = MainActivityDestinations.DependencyInjection.route) {
                        val viewModel = koinViewModel<MyViewModel>()
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = viewModel.getHelloWorldString()
                            )
                        }
                    }
                    composable (route = MainActivityDestinations.Counter.route) {
                        val viewModel = koinViewModel<MyViewModel>()
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Counter()

                            Text(
                                text = viewModel.getHelloWorldString()
                            )
                        }
                    }
                    composable (route = MainActivityDestinations.KTor.route) {
                        val censorClient = koinViewModel<InsultCensorViewModel>()
                        var censoredText by remember {
                            mutableStateOf<String?>(null)
                        }
                        var uncensoredText by remember {
                            mutableStateOf<String>("")
                        }
                        var isLoading by remember {
                            mutableStateOf<Boolean>(false)
                        }
                        var errorMessage by remember {
                            mutableStateOf<NetworkError?>(null)
                        }
                        val scope = rememberCoroutineScope()
                        Column(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray)
                        ) {
                            TextField(
                                value = uncensoredText,
                                onValueChange = {uncensoredText = it},
                                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                                placeholder = {
                                    Text("Uncensored Text")
                                }
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        errorMessage = null

                                        censorClient.censorWords(uncensoredText)
                                            .onSuccess {
                                                censoredText = it
                                            }
                                            .onError {
                                                errorMessage = it
                                            }
                                        isLoading = false
                                    }
                                }
                            ){
                                if(isLoading){
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(15.dp),
                                        strokeWidth = 1.dp,
                                        color = Color.White,
                                    )
                                }else{
                                    Text("Censor!")
                                }
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            censoredText?.let {
                                Text(it)
                            }
                            errorMessage?.let {
                                Text(text = it.name, color = Color.Red)
                            }
                        }
                    }
                    composable (route = MainActivityDestinations.Permission.route) {
                        val viewModel = koinViewModel<MyViewModel>()
                        val factory = rememberPermissionsControllerFactory()
                        val controller = remember(factory){
                            factory.createPermissionsController()
                        }

                        BindEffect(controller)

                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.DarkGray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Counter()

                            Text(
                                text = viewModel.getHelloWorldString()
                            )
                        }
                    }
                }
            }}

        }

    }

