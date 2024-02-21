package com.jm.metrostationalert.screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jm.metrostationalert.ViewModel
import com.jm.metrostationalert.data.entity.LatLngEntity
import com.jm.metrostationalert.data.entity.SubwayStationsEntity
import com.jm.metrostationalert.datastore.DataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMarkScreen(
    viewModel: ViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dataStore = DataStore(context)
    LaunchedEffect(Unit) {
        viewModel.getAllItems()
    }
    val bookmarkList = viewModel.getAllItems.value
    var stationName by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        dataStore.getStationName.collect() {
            stationName = it
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
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(text = "하차 알림", fontSize = 32.sp)
                    if (stationName != "") {
                        Text(text = "역이름: $stationName", fontSize = 24.sp)
                    } else {
                        Text(text = "저장된 역이 없습니다.", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "도착 시간", fontSize = 32.sp)
                    BookmarkStationLazyList(bookmarkList = bookmarkList)

                }
            }
            BannersAds()
        }
    }
}

@Composable
fun BookmarkStationLazyList(
    bookmarkList: List<LatLngEntity>,
//    onLongClick: (LatLngEntity) -> Unit
) {

    LazyColumn() {
        items(bookmarkList) { item ->
            ArrivalTime(item) {

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArrivalTime(
    item: LatLngEntity,
    onLongClick: (LatLngEntity) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { showDialog = true },
                onLongClick = { onLongClick(item) }
            )
    ) {
        Text(text = "역이름: ${item.stationName}")
    }

    if (showDialog) {
        ArrivalTimeDialog(item) {
            showDialog = it
        }
    }
}