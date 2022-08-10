buildscript {
    val kotlinVersion = "1.7.0"
    val hiltVersion by extra("2.43.1")
    extra["navVersion"] = "2.5.1"
    extra["composeVersion"] = "1.2.0"
    extra["activityVersion"] = "1.5.1"
    extra["lifecycleVersion"] = "2.5.1"

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
