package com.jm.metrostationalert

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.internal.Logger.TAG
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.jm.metrostationalert.data.SubwayArrivalResponse
import com.jm.metrostationalert.data.entity.LatLngEntity
import com.jm.metrostationalert.data.entity.SubwayStationsEntity
import com.jm.metrostationalert.repository.RepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {
    val isLoading = mutableStateOf(false)

    private var subwayStationList: MutableList<SubwayStationsEntity.SubwayStationItem> =
        mutableListOf()

    private var _subwayStations =
        mutableStateOf(emptyList<SubwayStationsEntity.SubwayStationItem>())
    val subwayStations: State<List<SubwayStationsEntity.SubwayStationItem>> = _subwayStations

    private var _getAllItems = mutableStateOf(emptyList<LatLngEntity>())
    val getAllItems: State<List<LatLngEntity>> = _getAllItems

    private var _getStationArrivalTime = mutableStateOf<SubwayArrivalResponse?>(null)
    val getStationArrivalTime: State<SubwayArrivalResponse?> = _getStationArrivalTime

    fun convertSubwayData(context: Context) {
        try {
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
        } catch (e: IOException) {
            // 파일 열기 실패
            Log.e(TAG, "Error reading SubwayStations.json: $e")
        } catch (e: JsonSyntaxException) {
            // JSON 파싱 실패
            Log.e(TAG, "Error parsing JSON: $e")
        } catch (e: Exception) {
            // 기타 예외 처리
            Log.e(TAG, "Unexpected error: $e")
        } finally {
            isLoading.value = false
        }
    }

    fun searchStation(searchString: String) {
        _subwayStations.value = subwayStationList.filter { it.stationName.contains(searchString) }
    }

    fun filterLineName(lineName: String) {
        _subwayStations.value = subwayStationList.filter { it.lineName == lineName }
    }

    fun getAllItems() {
        viewModelScope.launch {
            try {
                repository.getAllItem().collect() { result ->
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
                repository.insertItem(item)
            } catch (e: Exception) {
                Log.e("InsertErr", e.toString())
            }
        }
    }

    fun deleteItem(item: LatLngEntity) {
        viewModelScope.launch {
            try {
                repository.deleteItem(item)
            } catch (e: Exception) {
                Log.e("DeleteErr", e.toString())
            }
        }
    }

    fun getSubwayArrivalTime(stationName: String) {
        viewModelScope.launch {
            try {
                _getStationArrivalTime.value = repository.getSubwayArrivalTime(stationName)
            } catch (e: Exception) {
                Log.e("HttpErr", e.toString())
            }
        }
    }
}