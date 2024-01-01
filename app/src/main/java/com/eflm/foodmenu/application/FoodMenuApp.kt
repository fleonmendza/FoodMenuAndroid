package com.eflm.foodmenu.application

import android.app.Application
import android.widget.Toast
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.RetrofitHelper

class FoodMenuApp : Application() {

//    override fun onCreate() {
//        super.onCreate()
//        Toast.makeText(this, "Validando sesion", Toast.LENGTH_SHORT).show()
//    }

    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy{
        AlimentRepository(retrofit)
    }

}