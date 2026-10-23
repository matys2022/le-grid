package org.le_grid.dev.networking

import kotlinx.serialization.Serializable


@Serializable
data class CensoredText(
    public val result: String
)