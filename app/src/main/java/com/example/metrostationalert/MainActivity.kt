package com.example.metrostationalert

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.metrostationalert.ui.theme.MetroStationAlertTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val assetManager = resources.assets
        val inputStream = assetManager.open("SubwayStations.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val subwayStations: SubwayStationsEntity =
            Gson().fromJson(jsonString, SubwayStationsEntity::class.java)

        setContent {
            MetroStationAlertTheme {
                App()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val optionList = listOf(
        "지하철역 검색", "지하철 호선 검색"
    )
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "지금 내려야 한다.") }) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
//            DropdownMenu(expanded = true, onDismissRequest = { /*TODO*/ }) {
//                optionList.forEach { option ->
//                    DropdownMenuItem(text = { Text(text = item) }, onClick = { /*TODO*/ })
//
//                }
//            }
            Spinner(optionList, { option ->

            })
        }
    }
}

