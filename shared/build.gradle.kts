plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.louiscad.complete-kotlin")
    kotlin("plugin.serialization")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }
    val coroutinesVersion = "1.7.0"
    val ktorVersion = "2.3.0"
    val napierVersion = "2.6.1"
    val koinVersion = "3.4.1"

    sourceSets {
        val commonMain by getting
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-client-auth:$ktorVersion")
            implementation("io.ktor:ktor-client-cio:$ktorVersion")
            implementation("io.ktor:ktor-client-logging:$ktorVersion")
            implementation("io.github.aakira:napier:$napierVersion")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            api("io.insert-koin:koin-core:$koinVersion")
        }

        val commonTest by getting
        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        val androidMain by getting
        androidMain.dependencies {
            implementation("io.ktor:ktor-client-android:$ktorVersion")
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")
        }

        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        val shared by creating {
            dependencies {
                implementation("com.russhwolf:multiplatform-settings:1.0.0")
            }
            androidMain.dependsOn(this)
            iosMain.dependsOn(this)
            commonMain.dependsOn(this)
        }
    }
}
android {
    namespace = "com.mivanovskaya.gitviewer.shared"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}