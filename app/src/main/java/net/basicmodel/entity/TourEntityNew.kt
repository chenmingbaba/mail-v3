package net.basicmodel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TourEntityNew(
    var nettour_stake: String = "",
    var nettour_id: String = "",
    var nettour_point: String = "",
    var nettour_end_distance: String = "",
    var nettour_port: String = "",
    var nettour_start_distance: String = "",
    var nettour_lat_lng: String = "",
    var imgData: ArrayList<String>? = null,
    var customData: ArrayList<CustomFiledEntity>? = null,
    var nettour_start_time: String = "",
    var nettour_end_time: String = "",
    var smsContent :String? = null,
    var time:Long = -1L, //收到短信的时间
    var event_related_id: String? = "",
    var nettour_tool: String = ""
) : Parcelable
