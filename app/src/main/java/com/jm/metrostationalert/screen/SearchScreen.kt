package com.jm.metrostationalert.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jm.metrostationalert.ViewModel
import com.jm.metrostationalert.data.entity.LatLngEntity
import com.jm.metrostationalert.data.entity.SubwayStationsEntity
import com.jm.metrostationalert.datastore.DataStore
import com.skydoves.orchestra.spinner.Spinner
import com.skydoves.orchestra.spinner.SpinnerProperties
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: ViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val dataStore = DataStore(context)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.convertSubwayData(context)
    }

    val subwayStationsResult = viewModel.subwayStations.value

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "지금 내려야 한다.") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(it)
        ) {
            if (viewModel.isLoading.value) {
                CircularProgressBar()
            } else {
                SearchStation()
                BannersAds()
                SkydovesSpinner()
                if (subwayStationsResult.isEmpty()) {
                    EmptyScreen()
                } else {
                    AllStationsLazyList(subwayStationsResult, snackbarHostState, dataStore)
                }
            }
        }
    }
}

@Composable
fun SearchStation(
    viewModel: ViewModel = hiltViewModel(),
) {
    val (content, setContent) = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Row {
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
}

@Composable
fun SkydovesSpinner(
    viewModel: ViewModel = hiltViewModel(),
) {
    val optionList = rememberSaveable {
        listOf(
            "9호선(연장)",
            "9호선",
            "1호선",
            "의정부선",
            "4호선",
            "에버라인선",
            "경의중앙선",
            "경강선",
            "분당선",
            "수인선",
            "장항선",
            "3호선",
            "경원선",
            "경부선",
            "중앙선",
            "신분당선(연장)",
            "신분당선",
            "경춘선",
            "인천2호선",
            "2호선",
            "인천1호선",
            "7호선",
            "8호선",
            "공항철도1호선",
            "우이신설선",
            "일산선",
            "6호선",
            "5호선",
            "안산선",
            "경인선",
            "과천선",
            "7호선(인천)",
            "진접선",
            "서해선",
            "김포골드라인",
            "신림선",
            "신분당선(연장2)"
        )
    }
    Row(
        modifier = Modifier.padding(8.dp)
    ) {
        val (selectdeItem, setSelectedItem) = rememberSaveable {
            mutableStateOf("노선 선택")
        }
        Spinner(
            text = selectdeItem,
            modifier = Modifier
                .background(Color.LightGray)
                .width(120.dp)
                .height(35.dp),
            itemList = optionList,
            style = MaterialTheme.typography.bodyLarge,
            properties = SpinnerProperties(
                color = Color.Black,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                spinnerPadding = 16.dp,
                spinnerBackgroundColor = MaterialTheme.colorScheme.background,
            ),
            onSpinnerItemSelected = { _, item ->
                setSelectedItem(item)
                viewModel.filterLineName(item)
            }
        )
    }
}

@Composable
fun AllStationsLazyList(
    subwayStationsResult: List<SubwayStationsEntity.SubwayStationItem>,
    snackbarHostState: SnackbarHostState,
    dataStore: DataStore,
) {
    Scaffold(

    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(subwayStationsResult) { item ->
                StationItems(item = item, snackbarHostState, dataStore)
            }
        }
    }
}

@Composable
fun EmptyScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "검색하신 역이 없습니다.", fontSize = 30.sp)
    }

}

@Composable
fun StationItems(
    item: SubwayStationsEntity.SubwayStationItem,
    snackbarHostState: SnackbarHostState,
    dataStore: DataStore,
    viewModel: ViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val extraPadding by animateDpAsState(
        targetValue = if (expanded) 8.dp else 0.dp,
        label = "DpAnimation",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = if (expanded) extraPadding else 0.dp)
                .clickable { expanded = !expanded }
        ) {
            Text(text = "노선: ${item.lineName}")
            Text(text = "역이름: ${item.stationName}")
            if (!expanded) {
                HorizontalDivider(
                    modifier = Modifier
                        .height(10.dp)
                )
            } else {
                Row {
                    CornerShapeButton(
                        text = "하차 알림 받기",
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        scope.launch {
                            dataStore.saveStationName(item.stationName)
                            dataStore.saveLatitude(item.latitude)
                            dataStore.saveLongitude(item.longitude)
                            snackbarHostState.showSnackbar("저장되었습니다.")
                        }
                    }
                    CornerShapeButton(
                        text = "도착 시간 보기",
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        viewModel.insertItem(
                            LatLngEntity(
                                latitude = item.latitude,
                                longitude = item.longitude,
                                stationName = item.stationName
                            )
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar("저장되었습니다.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CornerShapeButton(text: String, modifier: Modifier, onClicked: () -> Unit) {
    Button(
        onClick = { onClicked() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Text(text = text, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    SearchScreen()

}