package com.example.metrostationalert

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.metrostationalert.ui.theme.MetroStationAlertTheme
import com.example.metrostationalert.ui.theme.ViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MetroStationAlertTheme {
                App()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {
    val viewModel = viewModel<ViewModel>()
    viewModel.convertSubwayData(LocalContext.current)
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
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "지금 내려야 한다.") }) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
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
                            .height(50.dp)
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
}

@Composable
fun LazyList(subwayStationsResult: List<SubwayStationsEntity.SubwayStationItem>) {
    LazyColumn() {
        items(subwayStationsResult) { item ->
            Box(modifier = Modifier.fillMaxWidth()) {
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

@Preview
@Composable
fun PreviewApp() {
    App()
}