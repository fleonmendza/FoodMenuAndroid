package com.eflm.foodmenu.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val pass: String? = null
)
