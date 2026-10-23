package org.le_grid.dev

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.le_grid.dev.di.initKoin

fun MainViewController() = ComposeUIViewController (
    configure = {
        initKoin()
    }

){
    App(batteryManager = BatteryManager())
}