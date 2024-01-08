package com.example.metrostationalert.ui.theme

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.metrostationalert.SubwayStationsEntity
import com.google.gson.Gson

class ViewModel : ViewModel() {

    val isLoading = mutableStateOf(false)

    private var subwayStationList: MutableList<SubwayStationsEntity.SubwayStationItem> =
        mutableListOf()

    private var _subwayStations =
        mutableStateOf(emptyList<SubwayStationsEntity.SubwayStationItem>())
    val subwayStations: State<List<SubwayStationsEntity.SubwayStationItem>> = _subwayStations

    private var _searchResult = mutableStateOf<SubwayStationsEntity.SubwayStationItem?>(null)
    val searchResult: State<SubwayStationsEntity.SubwayStationItem?> = _searchResult

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
        _searchResult.value = subwayStationList.find { it.stationName.contains(searchString) }
    }
}