package com.example.metrostationalert

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.metrostationalert.data.entitiy.SubwayStationsEntity
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ViewModel : ViewModel() {

    val isLoading = mutableStateOf(false)

    private var subwayStationList: MutableList<SubwayStationsEntity.SubwayStationItem> =
        mutableListOf()

    private var _subwayStations =
        mutableStateOf(emptyList<SubwayStationsEntity.SubwayStationItem>())
    val subwayStations: State<List<SubwayStationsEntity.SubwayStationItem>> = _subwayStations

    fun convertSubwayData(context: Context) {
        isLoading.value = true
        val assetManager = context.resources.assets
        val inputStream = assetManager.open("SubwayStations.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val result = Gson().fromJson(jsonString, SubwayStationsEntity::class.java)
        result.forEach {
            subwayStationList.add(it)
        }
        _subwayStations.value = subwayStationList
        isLoading.value = false
    }

    fun searchStation(searchString: String) {
        _subwayStations.value = subwayStationList.filter { it.stationName.contains(searchString) }
    }
}