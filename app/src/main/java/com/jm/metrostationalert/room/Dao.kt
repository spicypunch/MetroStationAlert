package com.jm.metrostationalert.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jm.metrostationalert.data.entity.LatLngEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM LatLngEntity")
    fun getAllItem(): Flow<List<LatLngEntity>>

    @Insert
    fun insertItem(item: LatLngEntity)

    @Delete
    fun deleteItem(item: LatLngEntity)
}