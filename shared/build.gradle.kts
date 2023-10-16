plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.serialization)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            linkerOpts += "-ld64"
        }
    }

    sourceSets {
        val commonMain by getting
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiations)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization)
            implementation(libs.napier)

            api(libs.koin.core)
        }

        val androidMain by getting
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.crypto)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val shared by creating {
            dependencies {
                implementation(libs.multiplatform.settings)
            }
            androidMain.dependsOn(this)
            iosMain.dependsOn(this)
            commonMain.dependsOn(this)
        }
    }
}
android {
    namespace = "com.mivanovskaya.gitviewer.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}