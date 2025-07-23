package ru.stanise.animebrowsing.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun currentScreen(navController: NavHostController): AppScreen? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    return remember(navBackStackEntry) {
        AppScreen.entries.find { it.name == navBackStackEntry?.destination?.route }
    }
}

val transientRoutes = setOf(
    AppScreen.Launcher.name,
    AppScreen.Error.name,
    AppScreen.NotFound.name
)

fun NavHostController.safeNavigate(route: String) {
    val currentRoute = currentBackStackEntry?.destination?.route

    if (currentRoute == route) return

    navigate(route) {
        when {
            currentRoute in transientRoutes -> {
                popUpTo(currentRoute!!) { inclusive = true }
            }
            else -> {
                popUpTo(route) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }
}

@Composable
fun ObserveNavigation(navController: NavHostController, onNavigated: () -> Unit) {

    DisposableEffect(navController) {
        val callback = NavController.OnDestinationChangedListener { _, destination, _ ->
            onNavigated()
        }

        navController.addOnDestinationChangedListener(callback)

        onDispose {
            navController.removeOnDestinationChangedListener(callback)
        }
    }
}
