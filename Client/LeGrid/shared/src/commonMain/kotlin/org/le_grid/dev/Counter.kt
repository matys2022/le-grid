package org.le_grid.dev

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Counter() {
    val counterKey = intPreferencesKey("counter")
    val prefs = koinInject<DataStore<Preferences>>()
    val counter by prefs.data.map { it[counterKey] ?: 0  }.collectAsState(initial = 0)
    val scope = rememberCoroutineScope()
    Column {
        Text(counter.toString())
        Button(onClick = {
            scope.launch {
                prefs.edit { dataStore ->
                    dataStore[counterKey] = counter + 1
                }
            }
        }) {
            Text("Increment")
        }
    }
}