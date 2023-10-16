plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.serialization)
    alias(libs.plugins.safeArgs)
}

android {
    namespace = "com.mivanovskaya.gitviewer.androidapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mivanovskaya.gitviewer.androidapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":shared"))

    implementation(libs.android.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.splashscreen)

    implementation(libs.kotlinserialization)

    implementation(libs.fragment)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.recyclerview)

    implementation(libs.koin.android)

    implementation(libs.markdown)
    implementation(libs.glide)

}