package com.eflm.foodmenu.data

import com.eflm.foodmenu.data.remote.AlimentosApi
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.data.remote.model.AlimentodetDto
import com.eflm.foodmenu.data.remote.model.FavService
import com.eflm.foodmenu.data.remote.model.LoginDto
import com.eflm.foodmenu.data.remote.model.RegisterDto
import com.eflm.foodmenu.data.remote.model.UserDto
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit


class AlimentRepository(private val retrofit: Retrofit) {

    private val alimentApi: AlimentosApi = retrofit.create(AlimentosApi::class.java)

    fun getAllAliments(): Call<List<AlimentoDto>> =
        alimentApi.getAllAliments()

    fun getAlimentsFav(user_uid: String?): Call<List<AlimentoDto>> =
        alimentApi.getAlimentsFav(user_uid)

    fun getUserData(user_uid: String?): Call<UserDto> =
        alimentApi.getUserData(user_uid)


//    fun getAlimentDetail(id: String): Call<AlimentodetDto> =
//        alimentApi.getAlimentDetail(id)

    suspend fun postDataLogin(data: LoginDto): Response<JsonObject> =
        alimentApi.postDataLogin(data)

    suspend fun postRegisterUser(data: RegisterDto): Response<JsonObject> =
        alimentApi.postRegister(data)

    fun searchAliment(query: String): Call<List<AlimentoDto>> =
        alimentApi.searchAliment(query)

    suspend fun addElementFav(data: FavService): Call<JsonObject> =
        alimentApi.addElementFav(data)

    suspend fun delElementFav(data: FavService): Call<JsonObject> =
        alimentApi.delElementFav(data)



}