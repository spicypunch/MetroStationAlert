package com.example.metrostationalert.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metrostationalert.ViewModel
import com.example.metrostationalert.data.entity.SubwayStationsEntity
import com.example.metrostationalert.datastore.DataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: ViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val dataStore = DataStore(context)
    LaunchedEffect(Unit) {
        viewModel.convertSubwayData(context)
    }

    val scope = rememberCoroutineScope()
    val subwayStationsResult = viewModel.subwayStations.value

    val keyboardController = LocalSoftwareKeyboardController.current

    val optionList = rememberSaveable {
        listOf("지하철역", "지하철 호선")
    }
    val (selectdeItem, setSelectedItem) = rememberSaveable {
        mutableStateOf("검색 유형")
    }
    val (content, setContent) = rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "지금 내려야 한다.") }) },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(it)
        ) {
            if (viewModel.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Row {
                    //                    Spinner(
                    //                        text = selectdeItem,
                    //                        modifier = Modifier
                    //                            .background(Color.LightGray)
                    //                            .width(80.dp)
                    //                            .height(35.dp),
                    //                        itemList = optionList,
                    //                        style = MaterialTheme.typography.bodyLarge,
                    //                        properties = SpinnerProperties(
                    //                            color = Color.Black,
                    //                            textAlign = TextAlign.Center,
                    //                            overflow = TextOverflow.Ellipsis,
                    //                            maxLines = 1,
                    //                            spinnerPadding = 16.dp,
                    //                            spinnerBackgroundColor = MaterialTheme.colorScheme.background,
                    //                        ),
                    //                        onSpinnerItemSelected = { _, item ->
                    //                            setSelectedItem(item)
                    //                        }
                    //                    )
                    OutlinedTextField(
                        value = content, onValueChange = setContent, modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                            .height(50.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            keyboardController?.hide()
                            viewModel.searchStation(content)
                        })
                    )

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            viewModel.searchStation(content)
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(text = "검색")
                    }
                }
                AllStationsLazyList(subwayStationsResult, dataStore)
            }
        }
    }
}

@Composable
fun AllStationsLazyList(
    subwayStationsResult: List<SubwayStationsEntity.SubwayStationItem>,
    dataStore: DataStore,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(subwayStationsResult) { item ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        //                    viewModel.insertItem(
                        //                        LatLngEntity(
                        //                            latitude = item.latitude,
                        //                            longitude = item.longitude,
                        //                            stationName = item.stationName
                        //                        )
                        //                    )

                        scope.launch {
                            dataStore.saveStationName(item.stationName)
                            dataStore.saveLatitude(item.latitude)
                            dataStore.saveLongitude(item.longitude)
                            snackbarHostState.showSnackbar("저장되었습니다.")
                        }
                    }) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(text = "노선: ${item.lineName}")
                        Text(text = "역이름: ${item.stationName}")
                        HorizontalDivider(
                            modifier = Modifier
                                .height(10.dp)
                        )
                    }
                }
            }
        }
    }
}