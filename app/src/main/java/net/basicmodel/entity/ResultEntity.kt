package net.basicmodel.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultEntity(
    var group_name:String="",
    var pileDataInfoList:ArrayList<TourEntityNew>?=null
): Parcelable
