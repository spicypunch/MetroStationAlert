package com.example.metrostationalert


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

class SubwayStationsEntity : ArrayList<SubwayStationsEntity.SubwayStationItem>(){
    @Parcelize
    data class SubwayStationItem(
        val latitude: Double, // 37.513011
        val lineName: String, // 9호선(연장)
        val longitude: Double, // 127.053282
        val notUse: Int, // 4128
        val stationName: String // 삼성중앙
    ) : Parcelable
}