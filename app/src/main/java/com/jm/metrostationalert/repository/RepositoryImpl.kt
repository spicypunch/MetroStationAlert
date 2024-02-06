package com.jm.metrostationalert.repository

import com.jm.metrostationalert.data.SubwayArrivalResponse
import com.jm.metrostationalert.data.entity.LatLngEntity
import com.jm.metrostationalert.retrofit.OpenApiService
import com.jm.metrostationalert.room.Dao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val openApiService: OpenApiService
) : Repository {
    override fun getAllItem(): Flow<List<LatLngEntity>> {
        return dao.getAllItem()
    }

    override suspend fun deleteItem(latLngEntity: LatLngEntity) = withContext(Dispatchers.IO) {
        dao.deleteItem(latLngEntity)
    }

    override suspend fun insertItem(latLngEntity: LatLngEntity) = withContext(Dispatchers.IO) {
        dao.insertItem(latLngEntity)
    }

    override suspend fun getSubwayArrivalTime(stationName: String): SubwayArrivalResponse {
        return openApiService.getSubwayArrivalTime(stationName)
    }
}