package com.example.metrostationalert.repository

import com.example.metrostationalert.data.entitiy.LatLngEntity
import com.example.metrostationalert.room.Dao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val dao: Dao
) : RoomRepository {
    override fun getAllItem(): Flow<List<LatLngEntity>> {
        return dao.getAllItem()
    }

    override suspend fun deleteItem(latLngEntity: LatLngEntity) {
        dao.deleteItem(latLngEntity)
    }

    override suspend fun insertItem(latLngEntity: LatLngEntity) {
        dao.insertItem(latLngEntity)
    }
}