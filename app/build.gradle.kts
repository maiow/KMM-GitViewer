plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.mivanovskaya.gitviewer.androidapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.mivanovskaya.gitviewer.androidapp"
        minSdk = 21
        targetSdk = 33
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

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("androidx.fragment:fragment-ktx:1.5.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    implementation("io.insert-koin:koin-core:3.4.1")
    implementation("io.insert-koin:koin-android:3.4.1")

    implementation("org.jetbrains:markdown:0.4.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}