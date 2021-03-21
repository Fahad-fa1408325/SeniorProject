package com.cmps312.seniorproject.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FirebaseUser(
    var email: String = "",
    var uid: String = ""
) : Parcelable