package com.example.metrostationalert.screen

import android.util.Log
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metrostationalert.ViewModel
import com.example.metrostationalert.data.entity.LatLngEntity
import com.example.metrostationalert.datastore.DataStore
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMarkScreen(
//    viewModel: ViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dataStore = DataStore(context)
//    LaunchedEffect(Unit) {
//        viewModel.getAllItems()
//    }
//    val bookmarkList = viewModel.getAllItems.value
    var stationName by remember {
        mutableStateOf("")
    }
    var latitude by remember {
        mutableStateOf(0.0)
    }
    var longitude by remember {
        mutableStateOf(0.0)
    }
    LaunchedEffect(Unit) {
        dataStore.getStationName.collect() {
            stationName = it
        }
    }
    LaunchedEffect(Unit) {
        dataStore.getLatitude.collect() {
            latitude = it
        }
    }
    LaunchedEffect(Unit) {
        dataStore.getLongitude.collect() {
            longitude = it
        }
    }
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "즐겨찾기") }) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .padding(it)
        ) {
            BookmarkStationLazyList(
                bookmarkList = listOf(
                    LatLngEntity(
                        stationName = stationName,
                        latitude = latitude,
                        longitude = longitude
                    )
                )
            )

            //            viewModel.deleteItem(it)

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookmarkStationLazyList(
    bookmarkList: List<LatLngEntity>,
//    onLongClick: (LatLngEntity) -> Unit
) {
    LazyColumn() {
        items(bookmarkList) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {},
//                        onLongClick = { onLongClick(item) }
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