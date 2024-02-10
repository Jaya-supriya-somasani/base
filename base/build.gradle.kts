plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.base"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.11.0")
    api("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //material design dependency
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.legacy:legacy-support-v4:1.0.0")
    api("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //navigation dependencies
    api("androidx.navigation:navigation-fragment-ktx:2.7.6")
    api("androidx.navigation:navigation-ui-ktx:2.7.6")


    //dagger dependencies
    implementation("com.google.dagger:dagger:2.44.2")
    implementation("com.google.dagger:dagger-android:2.44.2")
    implementation("com.google.dagger:dagger-android-support:2.44.2")
    kapt("com.google.dagger:dagger-compiler:2.44.2")
    kapt("com.google.dagger:dagger-android-processor:2.44.2")
}