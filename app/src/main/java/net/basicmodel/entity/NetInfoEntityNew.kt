package net.basicmodel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetInfoEntityNew(
    var name: String = "",
    var location: String = "",
    var room: String = "",
    var point: String = "",
    var infos: ArrayList<NetDetailsEntity>?,
    var imsg: ArrayList<String>?,
    var customFiled: ArrayList<CustomFiledEntity>?
) : Parcelable
