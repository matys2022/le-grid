package org.le_grid.dev

import assertk.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun testGetInitials() {
        assertThat(getInitials("Mathias Samek"), "MS")
    }

}