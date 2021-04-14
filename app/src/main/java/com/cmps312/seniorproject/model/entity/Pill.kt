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
    var mainUserFlag: Boolean = true,
    var mainUserEmail: String = "",
    var requestKey: Int = 0,
    var readFromMain: Boolean = false,
    var editFromMain: Boolean = false,
    var percentage: Double = 100.0,
    var demanded: Int = 0

) : Parcelable