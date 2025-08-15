plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "app.dpc.kid"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.dpc.kid"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            // CI에서 시크릿 사용 시 자동으로 채워짐
            storeFile = file(System.getenv("ANDROID_KEYSTORE_PATH") ?: "${'$'}{rootDir}/release.keystore")
            storePassword = System.getenv("ANDROID_STORE_PASSWORD") ?: ""
            keyAlias = System.getenv("ANDROID_KEY_ALIAS") ?: ""
            keyPassword = System.getenv("ANDROID_KEY_PASSWORD") ?: ""
        }
    }

    buildTypes {
        getByName("debug") { }
        create("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            // 키스토어 없으면 debug 서명으로라도 빌드되게
            val hasKeystore = file(System.getenv("ANDROID_KEYSTORE_PATH") ?: "${'$'}{rootDir}/release.keystore").exists() &&
                    (System.getenv("ANDROID_KEY_ALIAS") ?: "").isNotEmpty()
            signingConfig = if (hasKeystore) signingConfigs.getByName("release") else signingConfigs.getByName("debug")
        }
    }

    compileOptions {
