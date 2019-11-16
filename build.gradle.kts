buildscript {

    repositories {
        google()
        jcenter()
        maven { setUrl("https://plugins.gradle.org/m2/")}
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath ("org.jlleitschuh.gradle:ktlint-gradle:8.2.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
