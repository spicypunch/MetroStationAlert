package com.example.metrostationalert.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metrostationalert.ViewModel
import com.example.metrostationalert.data.entity.LatLngEntity

@Composable
fun BookMarkScreen(
    viewModel: ViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getAllItems()
    }
    val bookmarkList = viewModel.getAllItems.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        BookmarkStationLazyList(bookmarkList) {
            viewModel.deleteItem(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkStationLazyList(bookmarkList: List<LatLngEntity>, onLongClick: (LatLngEntity) -> Unit) {
    LazyColumn() {
        items(bookmarkList) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {},
                        onLongClick = { onLongClick(item) }
                    )
            ) {
                Column {
                    Text(text = "역이름: ${item.stationName}")
                    Divider(
                        modifier = Modifier
                            .height(10.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}