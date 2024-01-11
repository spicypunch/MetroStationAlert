package com.example.metrostationalert.repository

import com.example.metrostationalert.data.entitiy.LatLngEntity
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun getAllItem(): Flow<List<LatLngEntity>>

    suspend fun deleteItem(latLngEntity: LatLngEntity)

    suspend fun insertItem(latLngEntity: LatLngEntity)
}