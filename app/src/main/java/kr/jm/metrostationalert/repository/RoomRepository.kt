package kr.jm.metrostationalert.repository

import kr.jm.metrostationalert.data.entitiy.LatLngEntity
import kotlinx.coroutines.flow.Flow

interface RoomRepository {

    fun getAllItem(): Flow<List<LatLngEntity>>

    suspend fun deleteItem(latLngEntity: LatLngEntity)

    suspend fun insertItem(latLngEntity: LatLngEntity)
}