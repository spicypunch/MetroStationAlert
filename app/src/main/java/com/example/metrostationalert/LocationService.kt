package com.example.metrostationalert

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class LocationService : Service() {
    private val dataStore = DataStore(this)
    private var bookmarkLatitude = 0.0
    private var bookmarkLongitude = 0.0
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private var alertState = true

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
        val distance = calculateDistance(
            bookmarkLatitude = bookmarkLatitude,
            bookmarkLongitude = bookmarkLongitude,
            currentLatitude = location.latitude,
            currentLongitude = location.longitude
        )

        if (distance <= 1.0 && alertState) {
            sendNotification()
            alertState = false
        } else if (distance > 1.0) {
            alertState = true
        }
    }


    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "알림",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val notification: Notification = Notification.Builder(this, "1")
                .setSmallIcon(R.drawable.subway) //알림 아이콘
                .setContentTitle("앱이 실행 중입니다.") //알림의 제목 설정
                .setContentText("") //알림의 내용 설정
                .build()

            startForeground(1, notification)
        }

        if (intent?.action == "ACTION_STOP") {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val closeIntent = Intent(this, LocationService::class.java)
            closeIntent.action = "ACTION_STOP"
            val closePendingIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_IMMUTABLE)
            val notification: Notification = Notification.Builder(this, "1")
                .setSmallIcon(R.drawable.subway)
                .setContentTitle("곧 도착합니다!!!!")
                .setContentText("내릴 준비!!!!!!!!!!!!!!!!!!!!!!!!!!")
//                .addAction(R.drawable.check, "닫기", closePendingIntent)
                .build()

            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun calculateDistance(
        bookmarkLatitude: Double,
        bookmarkLongitude: Double,
        currentLatitude: Double,
        currentLongitude: Double
    ): Double {
        val earthRadius = 6371

        val latDistance = Math.toRadians(currentLatitude - bookmarkLatitude)
        val lonDistance = Math.toRadians(currentLongitude - bookmarkLongitude)

        val a = sin(latDistance / 2).pow(2.0) +
                cos(Math.toRadians(bookmarkLatitude)) *
                cos(Math.toRadians(currentLatitude)) *
                sin(lonDistance / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }
}