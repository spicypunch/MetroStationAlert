package com.example.metrostationalert

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.metrostationalert.datastore.DataStore
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class Service : Service() {
    private val dataStore = DataStore(this)
    private var bookmarkLatitude = 0.0
    private var bookmarkLongitude = 0.0
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getBookmarkLocation()
        createLocationRequest()
        createLocationCallback()
        startLocationUpdates()
    }

    private fun getBookmarkLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.getLatitude.collect() {
                bookmarkLatitude = it
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.getLongitude.collect() {
                bookmarkLongitude = it
            }
        }

    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    handleNewLocation(it)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun handleNewLocation(location: Location) {
        // 위치 정보를 받았을 때 처리할 동작
        Log.e("latitude", bookmarkLatitude.toString())
        Log.e("longitude", bookmarkLongitude.toString())
    }


    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "알림",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)

            val notification: Notification = Notification.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_launcher_foreground) //알림 아이콘
                .setContentTitle("알림") //알림의 제목 설정
                .setContentText("앱이 실행 중입니다.") //알림의 내용 설정
                .build()

            startForeground(1, notification) //인수로 알림 ID와 알림 지정
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}