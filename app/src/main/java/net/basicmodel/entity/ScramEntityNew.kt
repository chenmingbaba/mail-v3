package net.basicmodel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ScramEntityNew(
    var id: String = "",
    var childName: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var rate: String = "",
    var code: String = "",
    var location: String = "",
    var imgData: ArrayList<String>?,
    var customData: ArrayList<CustomFiledEntity>?

) : Parcelable
