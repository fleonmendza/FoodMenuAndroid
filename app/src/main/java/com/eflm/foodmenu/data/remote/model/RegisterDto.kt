package com.eflm.foodmenu.data.remote.model

import com.google.gson.annotations.SerializedName

data class RegisterDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("age")
    val age: String? = null,
    @SerializedName("sexo")
    val sexo: String? = null
)
