package com.jm.metrostationalert.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.jm.metrostationalert.ViewModel
import com.jm.metrostationalert.data.entity.LatLngEntity

@Composable
fun ArrivalTimeDialog(
    item: LatLngEntity,
    viewModel: ViewModel = hiltViewModel(),
    onClicked: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getSubwayArrivalTime(item.stationName)
    }
    val stationArrivalTime = viewModel.getStationArrivalTime.value

    Dialog(onDismissRequest = { onClicked(false) }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row {
                    if (stationArrivalTime != null) {
                        if (stationArrivalTime.realtimeArrivalList != null) {
                            Text(text = stationArrivalTime.realtimeArrivalList[0].trainLineNm)
                            Text(text = stationArrivalTime.realtimeArrivalList[0].arvlMsg2)
                        } else {
                            Text(text = "데이터가 없습니다.")
                        }
                    }
                }
            }
        }
    }
}