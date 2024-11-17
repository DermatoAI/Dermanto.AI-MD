import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    /*
    added plugins
     */
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.dokka")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dermatoai"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    packaging {
        @Suppress("DEPRECATION")
        exclude("META-INF/DEPENDENCIES")
    }
    defaultConfig {
        applicationId = "com.dermatoai"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["appAuthRedirectScheme"] = "com.googleusercontent.apps.dermato_ai"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.material)
    /*
    added library
    - room
    - retrofit
    - hilt
     */
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    implementation(libs.kotlinx.coroutines.android)


    implementation(libs.play.services.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
}
/*
configure dokka
 */
tasks.register("cleanDocs") {
    doLast {
        val docsDir = rootDir.resolve("docs")
        if (docsDir.exists()) {
            delete(docsDir)
        }
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(rootDir.resolve("docs"))

    dokkaSourceSets {
        named("main") {
            moduleName.set("Dermato.AI")
            suppressInheritedMembers.set(true)
            includeNonPublic.set(false)
        }
    }

}