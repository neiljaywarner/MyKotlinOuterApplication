pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add Flutter SDK repository
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
        // Add Flutter module repository
        maven { url = uri("../receive_images_flutter_demo/build/host/outputs/repo") }
    }
}

rootProject.name = "MyKotlinOuterApplication"
include(":app")

// Comment out includeBuild since we're using direct AAR dependencies now
/* 
includeBuild("../receive_images_flutter_demo/.android") {
    dependencySubstitution {
        // Substitute AAR dependencies with project dependencies for the Flutter module
        // This assumes the Flutter module's group is com.example.receive_images_flutter_demo
        // and the include_flutter.groovy script renames the module to :flutter
        substitute(module("com.example.receive_images_flutter_demo:flutter_debug:1.0")).using(
            project(":flutter")
        )
        substitute(module("com.example.receive_images_flutter_demo:flutter_profile:1.0")).using(
            project(":flutter")
        )
        substitute(module("com.example.receive_images_flutter_demo:flutter_release:1.0")).using(
            project(":flutter")
        )

        // Generic substitution if the above are too specific or versions change
        // substitute(module("com.example.receive_images_flutter_demo")).using(project(":flutter"))
    }
}
*/