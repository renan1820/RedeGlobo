package com.gitproject.redeglobo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gitproject.redeglobo.detail.ui.DetailScreen
import com.gitproject.redeglobo.domain.model.NavigationDestination
import com.gitproject.redeglobo.home.ui.mobile.HomeScreenMobile
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
                    dest.route == NavigationDestination.Search.route
                } == true

                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == NavigationDestination.Home.route
                            } == true,
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
                            onClick = {
                                navController.navigate(NavigationDestination.Search.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { _ ->
            NavHost(
                navController = navController,
                startDestination = NavigationDestination.Home.route
            ) {
                composable(NavigationDestination.Home.route) {
                    HomeScreenMobile(
                        onContentClick = { id ->
                            navController.navigate(NavigationDestination.Detail(id).createRoute())
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
                    DetailScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }
    }
}
