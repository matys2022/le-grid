package org.le_grid.dev.di

import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.engines
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.le_grid.dev.datastore.createDataStore
import org.le_grid.dev.dependencies.DbClient
import org.le_grid.dev.dependencies.MyViewModel
import org.le_grid.dev.networking.createHttpClient

actual val platformModule: Module = module {
    singleOf(::DbClient)
    single { createHttpClient(Darwin.create()) }
    single { createDataStore (get()) }
    viewModelOf(::MyViewModel)
}