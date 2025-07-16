package ru.stanise.animebrowsing.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun currentScreen(navController: NavHostController): AppScreen? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    return remember(navBackStackEntry) {
        AppScreen.entries.find { it.name == navBackStackEntry?.destination?.route }
    }
}


fun NavHostController.safeNavigate(
    route: String,
    popUpToRoute: String? = null,
    inclusive: Boolean = false,
    launchSingleTop: Boolean = true
) {
    val currentRoute = this.currentBackStackEntry?.destination?.route

    if (currentRoute != route) {
        this.navigate(route) {
            if (popUpToRoute != null) {
                popUpTo(popUpToRoute) { this.inclusive = inclusive }
            }
            this.launchSingleTop = launchSingleTop
        }
    }
}
