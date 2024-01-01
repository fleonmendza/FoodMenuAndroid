package com.eflm.foodmenu.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlimentoDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val nombre: String? = null,
    @SerializedName("img")
    val imagen: String? = null,
    @SerializedName("description")
    val descripcion: String? = null,
    @SerializedName("dificult")
    val dificultad: String? = null,
    @SerializedName("time_preparation")
    val tiempo_preparacion: Int? = null,
    @SerializedName("dish_type")
    val tipo_plato: String? = null,
    @SerializedName("baseDish")
    val basePlatillo: String? = null,
    @SerializedName("style_food")
    val estilo_cocina: String? = null,
    @SerializedName("componentsDisc")
    var restricciones_dieteticas: List<String>? = null,
    @SerializedName("ingredients")
    val ingredientes: List<String>? = null

)