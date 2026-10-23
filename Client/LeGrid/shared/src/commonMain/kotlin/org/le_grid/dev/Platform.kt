package org.le_grid.dev

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform