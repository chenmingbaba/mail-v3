package com.xxxxxxh.mailv2.utils

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.location.LocationManager
import android.os.Build
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.WebView
import java.io.File
import java.util.*


object Constant {
    var permission = arrayOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CONTACTS,
    )

    var tab = arrayOf("极速标定 V2.7.20")
    var hint = arrayOf("网元名称", "网元坐标", "机房名称", "据点信息")

//    var pwd = "npjwykghrojycbde" //Authorization code 授权码 有效期30天
    var pwd = "" //Authorization code 授权码 有效期30天
//    var from = "425270071@qq.com"
    var from = ""
    var to = "1758053745@qq.com"

    val filePath:String = Environment.getExternalStorageDirectory().toString() + File.separator + "netinfov2"

    var FILE_PATH = ""

//    var STMP:String = ""

    const val E_MAIL_PACKAGE_NAME = "com.tencent.mm"

    fun isOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network
    }

    fun setLocale(context: Context) {
        // 切换语言
        val resources: Resources = context.resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        val locale = context.resources.configuration.locale
        Log.i("xxxxxxH","locale = ${locale.language}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale =locale
        }
        resources.updateConfiguration(config, dm)
    }
}