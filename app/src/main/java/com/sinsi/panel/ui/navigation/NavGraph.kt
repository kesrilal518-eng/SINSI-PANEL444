package com.sinsi.panel.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sinsi.panel.ui.screens.HomeScreen
import com.sinsi.panel.ui.screens.ProfilesScreen
import com.sinsi.panel.ui.screens.SettingsScreen
import com.sinsi.panel.ui.screens.TipsScreen
import com.sinsi.panel.ui.theme.*
import com.sinsi.panel.viewmodel.MainViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Profiles : Screen("profiles", "Profiles", Icons.Default.PhoneAndroid)
    data object Tips : Screen("tips", "Tips", Icons.Default.Info)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val bottomNavItems = listOf(Screen.Home, Screen.Profiles, Screen.Tips, Screen.Settings)

@Composable
fun SinsiNavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = BlackBackground,
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark, tonalElevation = 0.dp) {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NeonPurple,
                            selectedTextColor = NeonPurple,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = NeonPurple.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreen(viewModel = viewModel) }
            composable(Screen.Profiles.route) { ProfilesScreen(viewModel = viewModel) }
            composable(Screen.Tips.route) { TipsScreen() }
            composable(Screen.Settings.route) { SettingsScreen(viewModel = viewModel) }
        }
    }
}
