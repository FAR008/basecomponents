import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library") version "8.11.1"
    id("org.jetbrains.kotlin.android") version "2.2.0"
    `maven-publish`
}

android {
    namespace = "com.farooqdev.basecomponents"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    testOptions {
        targetSdk = 36
    }

    lint {
        targetSdk = 36
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

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    // Essential dependencies (marked as api to expose to consumers)
    api("androidx.core:core-ktx:1.16.0")

    // Fragment components (api since they're fundamental)
    api("androidx.fragment:fragment-ktx:1.8.8")

    // Navigation (compileOnly - let apps decide their navigation version)
    compileOnly("androidx.navigation:navigation-fragment-ktx:2.9.2")

// Lifecycle components (required for repeatOnLifecycle)
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")

    // Coroutines (compileOnly - optional for consumers)
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Test dependencies (not exposed to consumers)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.FAR008"
                artifactId = "basecomponents"
                version = "1.0.3"

                from(components["release"])

                pom {
                    name.set("BaseComponents")
                    description.set("Android base components library")
                    url.set("https://github.com/FAR008/basecomponents")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("FAR008")
                            name.set("Muhammad Farooq")
                        }
                    }
                }
            }
        }
    }
}