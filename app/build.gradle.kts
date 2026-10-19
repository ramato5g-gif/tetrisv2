plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "com.example.app5fe80a"
    compileSdk = 35
    defaultConfig { applicationId = "com.example.app5fe80a"; minSdk = 26; targetSdk = 35; versionCode = 1; versionName = "1.0" }
    buildTypes { getByName("release") { isMinifyEnabled = false } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
