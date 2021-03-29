package com.cmps312.seniorproject.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainUser(
    var email: String = "",
    var uid: String = "",
    var id: String = ""
) : Parcelable