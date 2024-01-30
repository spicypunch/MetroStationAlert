package com.example.metrostationalert.data

import com.example.metrostationalert.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
) {
    object Search : BottomNavItem(route = "search", title = "Search", icon = R.drawable.search)
    object Bookmark :
        BottomNavItem(route = "bookmark", title = "Bookmark", icon = R.drawable.add_circle)
}