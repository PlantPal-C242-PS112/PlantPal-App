import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.plantpal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.android.plantpal"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"https://plantpal-api-454827052627.asia-southeast1.run.app/api/v1/\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    // Core Android libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Navigation components
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")

    // Maps and location services
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Image loading with Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Retrofit for networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room database and Paging
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("androidx.test:monitor:1.7.2")
    implementation("com.android.volley:volley:1.2.1")
    testImplementation("junit:junit:4.12")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.paging:paging-runtime-ktx:3.3.4")
    implementation("androidx.room:room-paging:2.6.1")

    // DataStore for preferences
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("io.github.chaosleung:pinview:1.4.4")

}