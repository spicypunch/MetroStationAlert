package com.example.metrostationalert

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metrostationalert.data.entity.LatLngEntity
import com.example.metrostationalert.data.entity.SubwayStationsEntity
import com.example.metrostationalert.repository.RoomRepositoryImpl
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val roomRepository: RoomRepositoryImpl
) : ViewModel() {
    val isLoading = mutableStateOf(false)

    private var subwayStationList: MutableList<SubwayStationsEntity.SubwayStationItem> =
        mutableListOf()

    private var _subwayStations =
        mutableStateOf(emptyList<SubwayStationsEntity.SubwayStationItem>())
    val subwayStations: State<List<SubwayStationsEntity.SubwayStationItem>> = _subwayStations

    private var _getAllItems = mutableStateOf(emptyList<LatLngEntity>())
    val getAllItems: State<List<LatLngEntity>> = _getAllItems

    fun convertSubwayData(context: Context) {
        isLoading.value = true
        subwayStationList.clear()
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

    fun getAllItems() {
        viewModelScope.launch {
            try {
                roomRepository.getAllItem().collect() { result ->
                    _getAllItems.value = result
                }
            } catch (e: Exception) {
                Log.e("GetItemErr", e.toString())
            }

        }
    }

    fun insertItem(item: LatLngEntity) {
        viewModelScope.launch {
            try {
                roomRepository.insertItem(item)
            } catch (e: Exception) {
                Log.e("InsertErr", e.toString())
            }
        }
    }

    fun deleteItem(item: LatLngEntity) {
        viewModelScope.launch {
            try {
                roomRepository.deleteItem(item)
            } catch (e: Exception) {
                Log.e("DeleteErr", e.toString())
            }
        }
    }
}