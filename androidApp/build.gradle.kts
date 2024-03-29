plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()

        applicationId = "com.mobileweb3.cosmostools.android"
        versionCode = 1
        versionName = "1.0"
    }

    //sign for release
//    signingConfigs {
//        create("release") {
//            storeFile = file("./key/key.jks")
//            com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).apply {
//                storePassword = getProperty("storePwd")
//                keyAlias = getProperty("keyAlias")
//                keyPassword = getProperty("keyPwd")
//            }
//        }
//    }

    buildTypes {
        create("debugPG") {
            isDebuggable = false
            isMinifyEnabled = true
            versionNameSuffix = " debugPG"
            matchingFallbacks.add("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
        getByName("release") {
            isMinifyEnabled = true
            //signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("lib/x86_64/darwin/libscrypt.dylib")
        exclude("lib/x86_64/freebsd/libscrypt.so")
        exclude("lib/x86_64/linux/libscrypt.so")
    }

    dependencies {
        implementation(project(":shared"))
        //desugar utils
        coreLibraryDesugaring(libs.desugar.jdk.libs)
        //Compose
        implementation(libs.androidx.compose.ui)
        implementation(libs.androidx.compose.ui.tooling)
        implementation(libs.androidx.compose.foundation)
        implementation(libs.androidx.compose.material)
        implementation(libs.androidx.compose.navigation)
        //Compose Utils
        implementation(libs.activity.compose)
        implementation(libs.accompanist.insets)
        implementation(libs.accompanist.swiperefresh)
        implementation(libs.accompanist.placeholder)
        implementation(libs.accompanist.systemuicontroller)
        //Coroutines
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android)
        //DI
        implementation(libs.koin.core)
        implementation(libs.koin.android)
        //WorkManager
        implementation(libs.work.runtime.ktx)
        //Glide
        implementation(libs.landscapist.glide)

        implementation("com.journeyapps:zxing-android-embedded:4.3.0")
        implementation("androidx.compose.material:material-icons-extended:1.3.1")

        implementation("com.google.firebase:firebase-bom:31.1.1")
        implementation("com.google.firebase:firebase-messaging:23.1.1")

        implementation("com.google.firebase:firebase-crashlytics-ktx:18.3.3")
        implementation("com.google.firebase:firebase-analytics-ktx:21.2.0")
    }
}