package org.le_grid.dev.di

import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import org.le_grid.dev.datastore.createDataStore


import org.le_grid.dev.dependencies.DbClient
import org.le_grid.dev.dependencies.MyRepository
import org.le_grid.dev.dependencies.MyRepositoryImpl
import org.le_grid.dev.networking.InsultCensorClient
import org.le_grid.dev.networking.InsultCensorViewModel
import org.le_grid.dev.networking.createHttpClient
import org.le_grid.dev.permissions.PermissionsViewModel

expect val platformModule : Module
val sharedModule = module {
    single {
        createHttpClient(get())
    }
    single {
        InsultCensorClient(get())
    }
    viewModel {
        InsultCensorViewModel(get())
    }
    viewModel {
        PermissionsViewModel(get())
    }
    single {
        createDataStore(get())
    }
    single<MyRepository> {
        MyRepositoryImpl(get())
    }
}