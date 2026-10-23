package org.le_grid.dev

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test


class GetStringLengthTest {
    @Test
    fun testStringLength(){
        assertThat(getStringLength("Hello")).isEqualTo(5)
    }

}