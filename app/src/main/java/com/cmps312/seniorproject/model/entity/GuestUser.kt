package com.cmps312.seniorproject.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GuestUser(
    var email: String = "",
    var mainUserEmail: String = "",
    var uid: String = "",
    var id: String = ""
) : Parcelable
