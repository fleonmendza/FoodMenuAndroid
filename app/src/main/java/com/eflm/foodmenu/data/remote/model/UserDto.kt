package com.eflm.foodmenu.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("uid")
    val uid: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("age")
    val age: Int? = null,
    @SerializedName("sexo")
    val sexo: String? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("weight")
    val weight: Int? = null,
    @SerializedName("allergies")
    val allergies: List<String>? = null,
    @SerializedName("favorites")
    val favorites: List<Int>? = null
)
