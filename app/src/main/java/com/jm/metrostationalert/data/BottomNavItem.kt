package com.jm.metrostationalert.data

import com.jm.metrostationalert.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Search : BottomNavItem(route = "search", title = "Search", icon = R.drawable.search)
    object Bookmark : BottomNavItem(route = "bookmark", title = "Bookmark", icon = R.drawable.bookmark)
    object Settings : BottomNavItem(route = "settings", title = "Settings", icon = R.drawable.settings)
}