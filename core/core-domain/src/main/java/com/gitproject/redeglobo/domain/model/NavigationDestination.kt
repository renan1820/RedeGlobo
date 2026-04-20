package com.gitproject.redeglobo.domain.model

sealed class NavigationDestination(val route: String) {
    data object Home : NavigationDestination("home")
    data object Search : NavigationDestination("search")
    data object Watchlist : NavigationDestination("watchlist")

    data class Detail(val contentId: String) : NavigationDestination("detail/{contentId}") {
        fun createRoute() = "detail/$contentId"

        companion object {
            const val ROUTE = "detail/{contentId}"
            const val ARG_CONTENT_ID = "contentId"
        }
    }
}
