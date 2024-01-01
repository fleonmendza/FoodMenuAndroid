package com.eflm.foodmenu.usecases.alimentDetail

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.bumptech.glide.Glide
import com.eflm.foodmenu.R
import com.eflm.foodmenu.application.FoodMenuApp
import com.eflm.foodmenu.data.AlimentRepository
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.data.remote.model.AlimentodetDto
import com.eflm.foodmenu.data.remote.model.FavService
import com.eflm.foodmenu.databinding.ActivityAlimentDetailBinding
import com.eflm.foodmenu.databinding.ActivityHomeBinding
import com.eflm.foodmenu.usecases.favorites.favoritesAlimentAdapter
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlimentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlimentDetailBinding

    private lateinit var sharedPreferences: EncryptedSharedPreferences

    private var alimenFav: Boolean? = null
    private var uid: String? = null
    private var id: Int? = null

    private lateinit var repository: AlimentRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlimentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de las EncryptedSharedPreferences
        val masterKeyAlias = MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            this,
            "account",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences


        val filledColor = ContextCompat.getColor(this@AlimentDetailActivity, R.color.black)
        val unfilledColor = ContextCompat.getColor(this@AlimentDetailActivity, R.color.white)

        val uidWithQuotes = sharedPreferences.getString("uidSp", "0")
        uid = uidWithQuotes?.replace("\"", "")

        id = intent.getIntExtra("ID", 0)
        val name = intent.getStringExtra("NAME")
        val img = intent.getStringExtra("IMG")
        val description = intent.getStringExtra("DESCRIP")
        val dificult = intent.getStringExtra("DIFICULT")
        val typeDish = intent.getStringExtra("TIPOPLA")
        val baseDish = intent.getStringExtra("BASEPLA")
        val style = intent.getStringExtra("ESTILO")
        val time = intent.getStringExtra("TIME")
        val ingre = intent.getStringArrayListExtra("INGRE")

        val ingredientesConGuion = ingre?.map { "- $it" } // Agrega un guion antes de cada ingrediente
        val ingredientesConcatenados = ingredientesConGuion?.joinToString("\n") // Concatena los ingredientes con saltos de línea


        repository = (application as FoodMenuApp).repository

        lifecycleScope.launch{
            val call: Call<List<AlimentoDto>> = repository.getAlimentsFav(uid)
            call.enqueue(object: Callback<List<AlimentoDto>> {
                override fun onResponse(
                    call: Call<List<AlimentoDto>>,
                    response: Response<List<AlimentoDto>>
                ) {
                    response.body()?.let { favoritos ->

                        val alimentoActual: Int? = id // Obtén el alimento actual de alguna manera

                        val isAlimentoFavorito = favoritos.any { it.id == alimentoActual }
                        alimenFav = isAlimentoFavorito
                        // Configura el color del botón basado en si el alimento está en la lista de favoritos
                        val drawable = ContextCompat.getDrawable(
                            this@AlimentDetailActivity,
                            R.drawable.baseline_favorite_24
                        )
                        drawable?.colorFilter =
                            PorterDuffColorFilter(
                                if (isAlimentoFavorito) filledColor else unfilledColor,
                                PorterDuff.Mode.SRC_IN
                            )

                        binding.fabAddFav.setImageDrawable(drawable)
                    }
                }

                override fun onFailure(call: Call<List<AlimentoDto>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }



        binding.tvIngre.text = ingredientesConcatenados

        Glide.with(binding.ivImage.context).load(img).into(binding.ivImage)
        binding.tvTitle.text = name
        binding.tvLongDesc.text = description
        binding.tvbasePlatillo.text = baseDish
        binding.tvTime.text = time.plus(" min")

        binding.fabAddFav.setOnClickListener{
            if(alimenFav == true) delFav() else addFav()
        }



    }

    private fun addFav(){
        lifecycleScope.launch{
            val data = FavService(uid, id)
            val call: Call<JsonObject> = repository.addElementFav(data)
            call.enqueue(object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("LOGSa", "Agregado${response}")
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("LOGSa", "erorrrr${t}")
                }

            })

        }
    }

    private fun delFav(){
        lifecycleScope.launch{
            val data = FavService(uid, id)
            val call: Call<JsonObject> = repository.delElementFav(data)
            call.enqueue(object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.d("LOGSa", "eliminado${response}")
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("LOGSa", "erorrrr${t}")
                }

            })
        }
    }


}