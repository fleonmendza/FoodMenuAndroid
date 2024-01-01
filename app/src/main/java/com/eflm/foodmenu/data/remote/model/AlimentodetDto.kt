package com.eflm.foodmenu.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlimentodetDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("imagen")
    val imagen: String? = null,
    @SerializedName("nombre")
    val nombre: String? = null,
    @SerializedName("descripcion")
    val descripcion: String? = null,
    @SerializedName("basePlatillo")
    val basePlatillo: String? = null,
    @SerializedName("cocinaTipo")
    val cocinaTipo: String? = null,
    @SerializedName("dificultad")
    val dificultad: String? = null,
    @SerializedName("estilo_cocina")
    val estilo_cocina: String? = null,
    @SerializedName("ingredientes")
    val ingredientes: List<String>? = null,
    @SerializedName("region_geografica")
    val region_geografica: String? = null,
    @SerializedName("tiempo_preparacion")
    val tiempo_preparacion: Int? = null,
    @SerializedName("tipo_plato")
    val tipo_plato: String? = null,
    @SerializedName("restricciones_dieteticas")
    var restricciones_dieteticas: List<String>? = null
)
