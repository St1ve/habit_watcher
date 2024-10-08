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
    }
}

rootProject.name = "Habit Watcher"
include(
    ":app",
    ":core:uikit",
    ":core:di",
    ":features:habitlist",
    ":features:nav-container",
    ":features:root",
    ":features:statistics",
    ":features:new-habit",
    ":core:db",
    ":common",
    ":features:details",
    ":testutils",
)
