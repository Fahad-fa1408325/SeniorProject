package com.cmps312.seniorproject.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String = "",
    var password: String = "",
    var uId: String = "",
    var userType: String = ""
) : Parcelable
