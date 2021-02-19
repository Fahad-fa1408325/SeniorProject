package com.cmps312.seniorproject.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String = "",
    var password: String = "",
    var uId: String = "",
    var userType: String = ""
)
