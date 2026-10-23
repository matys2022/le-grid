package org.le_grid.dev

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test

class CounterKtTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testCountingUp() = runComposeUiTest {
        setContent {
            Counter()
        }

        onNodeWithText("0").assertExists()
        onNodeWithText("1").assertDoesNotExist()
        onNodeWithText("Increment").performClick()
        onNodeWithText("1").assertExists()
        onNodeWithText("0").assertDoesNotExist()
    }
}