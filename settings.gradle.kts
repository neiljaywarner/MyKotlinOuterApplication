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
        // Add Flutter repository
        maven { url = uri("https://storage.googleapis.com/download.flutter.io") }
        // Add local Flutter module repository
        maven {
            url =
                uri("${System.getenv("HOME") ?: "."}/AndroidStudioProjects/my_inner_flutter_module/build/host/outputs/repo")
        }
    }
}

rootProject.name = "MyKotlinOuterApplication"
include(":app")

// Note: We no longer include the Flutter module directly using include_flutter.groovy
// Instead, we're referencing the AAR artifacts directly from the repositories above