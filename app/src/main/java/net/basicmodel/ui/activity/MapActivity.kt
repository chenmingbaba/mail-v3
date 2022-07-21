package net.basicmodel.ui.activity

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.tools.PictureFileUtils.isDownloadsDocument
import com.luck.picture.lib.tools.PictureFileUtils.isExternalStorageDocument
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_activity_map.*
import net.basicmodel.R
import net.basicmodel.entity.MarkerEntity
import net.basicmodel.entity.TourEntityNew
import net.basicmodel.event.MessageEvent
import net.basicmodel.popup.MarkersPopupWindow
import net.basicmodel.utils.*
import org.greenrobot.eventbus.EventBus
import java.io.File


@Suppress("CAST_NEVER_SUCCEEDS")
class MapActivity : AppCompatActivity(), AMap.OnMapLoadedListener, AMap.OnMarkerDragListener,
    LocationSource.OnLocationChangedListener, AMapLocationListener,
    GeocodeSearch.OnGeocodeSearchListener {
    var mMapView: MapView? = null
    var aMap: AMap? = null
    var myLocationStyle: MyLocationStyle? = null
    var markerOptions: MarkerOptions? = null
    var marker: Marker? = null
    var index = -1
    var mapLoad = false
    var loadLocation = false
    var geocoderSearch: GeocodeSearch? = null
    var curAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //隐私政策合规
            AMapLocationClient.updatePrivacyShow(this, true, true)
            AMapLocationClient.updatePrivacyAgree(this, true)
            MapsInitializer.updatePrivacyShow(this, true, true)
            MapsInitializer.updatePrivacyAgree(this, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setContentView(R.layout.layout_activity_map)
        mMapView = findViewById(R.id.map)
        mMapView!!.onCreate(savedInstanceState)



        initMap()
        initView()
    }


    fun initView() {
        val i = intent
        index = i.getIntExtra("index", -1)
        map_confirm.setOnClickListener {
            var latLng: LatLng? = null
            var lat = ""
            var longt = ""
            if (marker == null) {
                latLng = LatLng(
                    aMap!!.cameraPosition.target.latitude,
                    aMap!!.cameraPosition.target.longitude
                )
                lat = if (latLng.latitude != 0.0) MyLocationManager.get()
                    .formatDouble(latLng.latitude)!! else ""
                longt =
                    if (latLng.longitude != 0.0) MyLocationManager.get()
                        .formatDouble(latLng.longitude)!! else ""
            } else {
                latLng = marker!!.position
                lat = if (latLng.latitude != 0.0) MyLocationManager.get()
                    .formatDouble(latLng.latitude)!! else ""
                longt =
                    if (latLng.longitude != 0.0) MyLocationManager.get()
                        .formatDouble(latLng.longitude)!! else ""
            }
            EventBus.getDefault().post(MessageEvent("map", index, longt, lat))
            EventBus.getDefault().post(MessageEvent("address", curAddress))
            finish()
        }
        map_offline.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    com.amap.api.maps.offlinemap.OfflineMapActivity::class.java
                )
            )
        }

        map_import.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, 1)
        }

        map_list.setOnClickListener {
            if (markersJson.isEmpty()) {
                Toast.makeText(this, "请先导入坐标", Toast.LENGTH_SHORT).show()
            } else {
                val type = object : TypeToken<ArrayList<MarkerEntity>>() {}.type
                val markers: ArrayList<MarkerEntity> = Gson().fromJson(markersJson, type)
                currentMarker?.let { it1 -> markers.add(0, it1) }
                MarkersPopupWindow.MarkersPopupWindowBuilder.init(this)
                    .setData(markers) { marker ->
                        aMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    marker.lat,
                                    marker.lgt
                                ), 16f
                            )
                        )
                    }
                    .build()
                    .show(it)
            }
        }


        geocoderSearch = GeocodeSearch(this)
        geocoderSearch!!.setOnGeocodeSearchListener(this)
    }

    private var currentMarker: MarkerEntity? = null
    private fun initMap() {
        aMap = mMapView!!.map
        aMap!!.mapType = AMap.MAP_TYPE_NORMAL // 矢量地图模式
        myLocationStyle =
            MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle!!.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle!!.showMyLocation(false)
        myLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)
        aMap!!.myLocationStyle = myLocationStyle //设置定位蓝点的Style
        aMap!!.uiSettings.isMyLocationButtonEnabled = true //设置默认定位按钮是否显示，非必需设置。
        aMap!!.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap!!.setOnMapLoadedListener(this)
        aMap!!.setOnMarkerDragListener(this)
        aMap!!.setOnMyLocationChangeListener { location ->
            if (mapLoad) {
                if (!loadLocation) {
                    aMap?.let {
                        it.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                ), 16f
                            )
                        )
                        currentMarker =
                            MarkerEntity(0, location.latitude, location.longitude, "当前位置")
                        addMarker(
                            location.latitude,
                            location.longitude,
                            "当前位置"
                        )
                        val query = RegeocodeQuery(
                            LatLonPoint(location.latitude, location.longitude),
                            200F, GeocodeSearch.AMAP
                        )
                        geocoderSearch!!.getFromLocationAsyn(query)
//                        if (index == 2) {
//                            initAllCode()
//                        }
                    }
                    loadLocation = true
                }

            }
        }
    }

    private fun initAllCode() {
        val keys = MMKVUtils.getKeys("tour")
        val data = ArrayList<TourEntityNew>()
        if (keys.size > 0) {
            for (item in keys) {
                val entity = MMKV.defaultMMKV()!!.decodeParcelable(item, TourEntityNew::class.java)
                data.add(entity!!)
            }
        }
        if (data.size > 0) {
            for (item in data) {
                val location = item.nettour_lat_lng.split(",")
                val lgt = location[0].toDouble()
                val lat = location[1].toDouble()
                addMarker(lat, lgt, item.nettour_stake)
            }
        }

    }

    private fun addMarker(lat: Double, longt: Double, title: String) {
        val markerOptions = MarkerOptions()
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            .position(LatLng(lat, longt))
            .title(title)
            .draggable(true)
        marker = aMap!!.addMarker(markerOptions)
        marker!!.showInfoWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView!!.onSaveInstanceState(outState)
    }

    override fun onMapLoaded() {
        mapLoad = true
    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDrag(p0: Marker?) {

    }

    override fun onMarkerDragEnd(p0: Marker?) {
        p0!!.remove()
        addMarker(p0.position.latitude, p0.position.longitude, p0.title)
        val query = RegeocodeQuery(
            LatLonPoint(p0.position.latitude, p0.position.longitude),
            200F, GeocodeSearch.AMAP
        )
        geocoderSearch!!.getFromLocationAsyn(query)
    }

    override fun onLocationChanged(p0: Location?) {
    }

    override fun onLocationChanged(p0: AMapLocation?) {
    }

    override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
        p0?.let {
            curAddress = it.regeocodeAddress.formatAddress
        }
        Log.i("xxxxxxH", "address = $curAddress")
    }

    override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {
    }

    private var markersJson = ""

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                it.path?.let { it1 ->
                    Log.e("file", it1)
                    val file = File(it1)
                    Log.e("file-path", file.path)

                    try {
                        markersJson =
                            FileIOUtils.readFile2String(UriUtils.getFilePathFromURI(this, it))
                        val type = object : TypeToken<List<MarkerEntity>>() {}.type
                        val markers: List<MarkerEntity> = Gson().fromJson(markersJson, type)
                        markers.map { marker ->
                            marker.apply {
                                addMarker(lat, lgt, title)
                            }
                        }
                        Log.e("file-json", markersJson)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "请选择正确的文件", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}