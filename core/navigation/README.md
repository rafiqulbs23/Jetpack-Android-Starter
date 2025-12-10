# Module :core:navigation

Centralized navigation module that manages all navigation routes and graphs for the application.

## Purpose

This module consolidates all navigation logic in one place, making it easier to:
- Manage navigation routes across the entire app
- Ensure consistent navigation patterns
- Reduce coupling between feature modules
- Simplify navigation testing and maintenance

## Structure

```
core/navigation/
├── auth/              # Authentication navigation routes
├── home/              # Home feature navigation routes
├── profile/           # Profile feature navigation routes
├── NavHost.kt         # Main navigation host composable
└── TopLevelDestination.kt  # Top-level destination definitions
```

## Dependencies

- `:core:ui` - For UI utilities and components
- `:feature:auth` - For auth screens
- `:feature:home` - For home screens
- `:feature:profile` - For profile screens

## Usage

### In App Module

```kotlin
import com.aristopharma.v2.core.navigation.AppNavHost
import com.aristopharma.v2.core.navigation.auth.AuthNavGraph
import com.aristopharma.v2.core.navigation.home.HomeNavGraph

@Composable
fun JetpackNavHost(
    appState: JetpackAppState,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    val startDestination = if (appState.isUserLoggedIn) 
        HomeNavGraph::class else AuthNavGraph::class
    
    AppNavHost(
        navController = appState.navController,
        startDestination = startDestination,
        onShowSnackbar = onShowSnackbar,
    )
}
```

### Navigation Extensions

Navigation extensions are available for each feature:

```kotlin
// Home navigation
navController.navigateToHomeNavGraph()
navController.navigateToItemScreen(itemId)

// Auth navigation
navController.navigateToAuthNavGraph()
navController.navigateToSignInScreen()
navController.navigateToSignUpScreen()

// Profile navigation
navController.navigateToProfileScreen()
```

## Adding New Navigation Routes

1. Create route definitions in the appropriate feature subdirectory:
   ```kotlin
   @Serializable
   data object NewScreen
   ```

2. Add navigation extension:
   ```kotlin
   fun NavController.navigateToNewScreen() {
       navigate(NewScreen) { launchSingleTop = true }
   }
   ```

3. Add screen composable to NavGraphBuilder:
   ```kotlin
   fun NavGraphBuilder.newScreen(
       onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
   ) {
       composable<NewScreen> {
           NewScreen(onShowSnackbar = onShowSnackbar)
       }
   }
   ```

4. Register in `AppNavHost`:
   ```kotlin
   AppNavHost(...) {
       // ... existing graphs
       newScreen(onShowSnackbar = onShowSnackbar)
   }
   ```

## Benefits

- **Centralized Management**: All navigation logic in one place
- **Type Safety**: Uses Kotlin Serialization for type-safe navigation
- **Separation of Concerns**: Features focus on UI, navigation handles routing
- **Easier Testing**: Navigation logic can be tested independently
- **Better Maintainability**: Changes to navigation structure are localized


