package com.eflm.foodmenu.data.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import retrofit2.http.Path


data class FavService(
     @SerializedName("uid")
     val user_id: String?= null,
     @SerializedName("id")
     val idAliment: Int? = null

)
