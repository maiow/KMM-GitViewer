package com.mivanovskaya.gitviewer.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform