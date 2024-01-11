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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.metrostationalert.ViewModel
import com.example.metrostationalert.data.entitiy.SubwayStationsEntity

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: ViewModel = hiltViewModel(),
) {

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
    Column(
        modifier = Modifier
            .padding(8.dp)
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
                        .height(50.dp)
                ) {
                    Text(text = "검색")
                }
            }
            LazyList(subwayStationsResult)
        }
    }
}

@Composable
fun LazyList(
    subwayStationsResult: List<SubwayStationsEntity.SubwayStationItem>,
) {
    val scope = rememberCoroutineScope()
    LazyColumn() {
        items(subwayStationsResult) { item ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                }) {
                Column {
                    Text(text = "노선: ${item.lineName}")
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