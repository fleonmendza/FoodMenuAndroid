package com.eflm.foodmenu.usecases.favorites

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.databinding.ItemFavAlimentBinding
import com.eflm.foodmenu.databinding.ItemRecomendedAlimentBinding
import com.eflm.foodmenu.model.Alimento
import com.eflm.foodmenu.usecases.alimentDetail.AlimentDetailActivity
import java.util.ArrayList

class favoritesViewHolder ( view : View) : RecyclerView.ViewHolder(view){
    val binding = ItemFavAlimentBinding.bind(view)

    fun render(aliment: AlimentoDto){
        val typrDish = "Tipo de platillo: ${aliment.tipo_plato}"
        binding.nameAliFav.text = aliment.nombre
        binding.typeAliFav.text = typrDish
        Glide.with(binding.imgvAliFav.context).load(aliment.imagen).into(binding.imgvAliFav)
        itemView.setOnClickListener {

            val dataIntent = Intent(itemView.context,  AlimentDetailActivity::class.java).apply {
                putExtra("IMG", aliment.imagen)
                putExtra("NAME", aliment.nombre)
                putExtra("TIME", aliment.tiempo_preparacion.toString())
                putStringArrayListExtra("INGRE", aliment.ingredientes as ArrayList<String>?)
                putExtra("DESCRIP", aliment.descripcion)
                putExtra("ID", aliment.id)

            }
            itemView.context.startActivity(dataIntent)
        }
    }
}