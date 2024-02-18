import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    id("app.cash.sqldelight") version "2.0.1"
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.kotlinProject.db")
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "18"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            // -> Koin <- //
            implementation(
                project.dependencies.platform(
                    "io.insert-koin:koin-bom:3.5.1",
                ),
            )
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-android")
            // -> SQLDelight <-
            implementation("app.cash.sqldelight:android-driver:2.0.1")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            api(compose.foundation)
            api(compose.animation)

            implementation(compose.material)
            implementation(compose.ui)
            api(compose.materialIconsExtended)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            // CURSO LIBS... //
            // -> PRE COMPOSE - NAVIGATION <- //
            api(libs.precompose)
            // -> ViewModel <- //
            api(libs.precompose.viewmodel)
            // -> Koin <- //
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:3.5.1"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-compose")
            api("moe.tlaster:precompose-koin:1.5.10")
        }

        iosMain.dependencies {
            // iOS dependencies
            implementation("app.cash.sqldelight:native-driver:2.0.1")
            implementation("co.touchlab:stately-common:2.0.5")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    // 1.5.8
}
