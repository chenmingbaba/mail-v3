package net.basicmodel.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MyLocationManager {
    companion object {
        private var i: MyLocationManager? = null
            get() {
                field ?: run {
                    field = MyLocationManager()
                }
                return field
            }

        @Synchronized
        fun get(): MyLocationManager {
            return i!!
        }
    }

    fun getLocation(context: Context, listener: LocationListener) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "请打开gps定位", Toast.LENGTH_LONG).show()
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0f,
            listener
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, 0, 0f,
            listener
        )
    }

    fun formatDouble(data: Double): String? {
        return DecimalFormat("#.000000").format(data)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDate2(date: Date?): String? {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }
}