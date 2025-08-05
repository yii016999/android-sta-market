plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.sta.market"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sta.market"
        minSdk = 26
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
                "proguard-rules.pro",
            )
        }
    }
    // java 17 is required for Compose
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        stabilityConfigurationFile =
            rootProject.layout.projectDirectory.file("stability_config.conf")
    }
    // Enable Compose
    buildFeatures {
        compose = true
    }
    hilt {
        enableAggregatingTask = false
    }
    lint {
        checkTestSources = false
        // avoid known Lint bug when analyzing test code; can re-enable when issue is fixed
        checkDependencies = true
    }
}

// Configure Detekt for static code analysis
detekt {
    config.setFrom("$rootDir/detekt.yml")
    buildUponDefaultConfig = true
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx)
    // lifecycle and viewmodel libraries
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Compose libraries
    implementation(libs.androidx.activity.compose)
    // use bom for Compose to manage versions
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window.size)
    // unit testing libraries
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    // ui testing libraries
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // debugging and tooling libraries
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // hilt for dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    // coroutines for asynchronous programming
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // navigation component for Compose
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.kotlin.test)}
