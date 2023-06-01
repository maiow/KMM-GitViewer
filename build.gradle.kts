// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id("com.louiscad.complete-kotlin") version "1.1.0" apply false
    kotlin("android") version "1.8.21" apply false
    kotlin("multiplatform") version "1.8.21" apply false
    kotlin("plugin.serialization") version "1.8.10" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}


