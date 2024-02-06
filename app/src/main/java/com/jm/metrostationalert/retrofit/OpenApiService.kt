package com.jm.metrostationalert.retrofit

import com.jm.metrostationalert.BuildConfig
import com.jm.metrostationalert.data.SubwayArrivalResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenApiService {

    @GET("${BuildConfig.OpenAPIKey}/json/realtimeStationArrival/0/4/{stationName}")
    suspend fun getSubwayArrivalTime(
        @Path("stationName") stationName: String
    ): SubwayArrivalResponse
}