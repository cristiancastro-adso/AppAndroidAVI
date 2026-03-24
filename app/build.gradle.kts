import org.gradle.kotlin.dsl.invoke

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.pipe.avi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pipe.avi"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        jniLibs {
            // Force compression of native libraries to support 16 KB page sizes
            // if using older libraries that aren't ELF-aligned.
            useLegacyPackaging = true
        }
    }
}

dependencies {

    // AndroidX básicas
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // UI
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.browser:browser:1.5.0")

    // Imágenes
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.davemorrissey.labs:subsampling-scale-image-view:3.10.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Charts
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Cloudinary (Updated to 3.1.2 for better 16KB support)
    implementation("com.cloudinary:cloudinary-android:3.1.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    // Material actualizado
    implementation("com.google.android.material:material:1.11.0")

    implementation ("androidx.webkit:webkit:1.12.1")

}