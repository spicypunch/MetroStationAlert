package com.example.metrostationalert.repository

import com.example.metrostationalert.data.entity.LatLngEntity
import com.example.metrostationalert.room.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val dao: Dao
) : RoomRepository {
    override fun getAllItem(): Flow<List<LatLngEntity>> {
        return dao.getAllItem()
    }

    override suspend fun deleteItem(latLngEntity: LatLngEntity) = withContext(Dispatchers.IO) {
        dao.deleteItem(latLngEntity)
    }

    override suspend fun insertItem(latLngEntity: LatLngEntity) = withContext(Dispatchers.IO) {
        dao.insertItem(latLngEntity)
    }
}