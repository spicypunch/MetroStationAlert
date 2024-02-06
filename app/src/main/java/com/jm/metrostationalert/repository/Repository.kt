package com.jm.metrostationalert.repository

import com.jm.metrostationalert.data.SubwayArrivalResponse
import com.jm.metrostationalert.data.entity.LatLngEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllItem(): Flow<List<LatLngEntity>>

    suspend fun deleteItem(latLngEntity: LatLngEntity)

    suspend fun insertItem(latLngEntity: LatLngEntity)

    suspend fun getSubwayArrivalTime(stationName: String): SubwayArrivalResponse
}