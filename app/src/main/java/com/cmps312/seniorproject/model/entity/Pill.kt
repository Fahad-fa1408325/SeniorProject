package com.cmps312.seniorproject.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pill(
    var name: String = "",
    var time: String = "",
    var dosage: Int = 0,
    var repeadtly: Int = 0,
    var pillId: String = "",
    var uid: String = "",
    var requestKey: Int = 0
) : Parcelable