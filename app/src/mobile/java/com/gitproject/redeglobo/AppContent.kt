package com.gitproject.redeglobo

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboNavBar
import com.gitproject.redeglobo.ui.theme.GloboWhite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gitproject.redeglobo.detail.ui.DetailScreen
import com.gitproject.redeglobo.domain.model.NavigationDestination
import com.gitproject.redeglobo.home.ui.mobile.HomeScreenMobile
import com.gitproject.redeglobo.login.ui.LoginScreen
import com.gitproject.redeglobo.player.ui.PlayerScreen
import com.gitproject.redeglobo.search.ui.SearchScreen
import com.gitproject.redeglobo.ui.theme.RedeGloboTheme

@Composable
fun GloboAppContent() {
    RedeGloboTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            bottomBar = {
                val showBottomBar = currentDestination?.hierarchy?.any { dest ->
                    dest.route == NavigationDestination.Home.route ||
                    dest.route == NavigationDestination.Search.route ||
                    dest.route == NavigationDestination.Login.route
                } == true

                if (showBottomBar) {
                    val navItemColors = NavigationBarItemDefaults.colors(
                        selectedIconColor   = GloboWhite,
                        selectedTextColor   = GloboWhite,
                        indicatorColor      = GloboBlue,
                        unselectedIconColor = GloboGray,
                        unselectedTextColor = GloboGray
                    )
                    NavigationBar(containerColor = GloboNavBar) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == NavigationDestination.Home.route
                            } == true,
                            colors = navItemColors,
                            onClick = {
                                navController.navigate(NavigationDestination.Home.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                            label = { Text("Buscar") },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == NavigationDestination.Search.route
                            } == true,
                            colors = navItemColors,
                            onClick = {
                                navController.navigate(NavigationDestination.Search.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, contentDescription = "Conta") },
                            label = { Text("Conta") },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == NavigationDestination.Login.route
                            } == true,
                            colors = navItemColors,
                            onClick = {
                                navController.navigate(NavigationDestination.Login.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavigationDestination.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(NavigationDestination.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(NavigationDestination.Home.route) {
                                popUpTo(NavigationDestination.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(NavigationDestination.Home.route) {
                    HomeScreenMobile(
                        onContentClick = { id ->
                            navController.navigate(NavigationDestination.Detail(id).createRoute())
                        },
                        onPlayClick = {
                            navController.navigate(NavigationDestination.Player.route)
                        },
                        onSearchClick = {
                            navController.navigate(NavigationDestination.Search.route)
                        }
                    )
                }
                composable(NavigationDestination.Search.route) {
                    SearchScreen(
                        onContentClick = { id ->
                            navController.navigate(NavigationDestination.Detail(id).createRoute())
                        }
                    )
                }
                composable(NavigationDestination.Detail.ROUTE) {
                    DetailScreen(
                        onBackClick = { navController.popBackStack() },
                        onPlayClick = { navController.navigate(NavigationDestination.Player.route) }
                    )
                }
                composable(NavigationDestination.Player.route) {
                    PlayerScreen(
                        videoUrl = NavigationDestination.Player.VIDEO_URL,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
