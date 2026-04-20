package com.gitproject.redeglobo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.gitproject.redeglobo.detail.ui.DetailScreen
import com.gitproject.redeglobo.domain.model.NavigationDestination
import com.gitproject.redeglobo.home.ui.tv.HomeScreenTv
import com.gitproject.redeglobo.search.ui.SearchScreen
import com.gitproject.redeglobo.ui.theme.RedeGloboTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GloboAppContent() {
    RedeGloboTheme {
        val navController = rememberNavController()

        Surface {
            NavHost(
                navController = navController,
                startDestination = NavigationDestination.Home.route
            ) {
                composable(NavigationDestination.Home.route) {
                    HomeScreenTv(
                        onContentClick = { id ->
                            navController.navigate(NavigationDestination.Detail(id).createRoute())
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
