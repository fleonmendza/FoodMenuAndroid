package com.eflm.foodmenu.data.remote

import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.data.remote.model.AlimentodetDto
import com.eflm.foodmenu.data.remote.model.FavService
import com.eflm.foodmenu.data.remote.model.LoginDto
import com.eflm.foodmenu.data.remote.model.RegisterDto
import com.eflm.foodmenu.data.remote.model.UserDto

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AlimentosApi {

    //Para Apiary
//    @GET("Alimentos/lista_alimentos")
//    fun getAllAliments(): Call<List<AlimentoDto>>

//    @GET("Alimentos/alimento_detail/{id}")
//    fun getAlimentDetail(
//        @Path("id") id: String?
//    ): Call<AlimentodetDto>


    @POST("login")
    suspend fun postDataLogin(@Body data:LoginDto): Response<JsonObject>

    @POST("register")
    suspend fun postRegister(@Body data:RegisterDto): Response<JsonObject>

    @GET("alimentsList")
    fun getAllAliments(): Call<List<AlimentoDto>>

    @GET("alimentsListFavorites/{user_uid}")
    fun getAlimentsFav(
        @Path("user_uid") user_uid: String?
    ): Call<List<AlimentoDto>>

    @GET("userData/{user_id}")
    fun getUserData(
        @Path("user_id") user_uid: String?
    ): Call<UserDto>

    @GET("searchAliments")
    fun searchAliment(
        @Query("query") query: String?
    ): Call<List<AlimentoDto>>

    @PUT("add_element")
    fun addElementFav(
        @Body data: FavService
        ): Call<JsonObject>

    @PUT("remove_element")
    fun delElementFav(
        @Body data: FavService
        ): Call<JsonObject>

}