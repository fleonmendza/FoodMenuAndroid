package com.eflm.foodmenu.usecases.search

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.databinding.ItemRecomendedAlimentBinding
import com.eflm.foodmenu.usecases.alimentDetail.AlimentDetailActivity
import java.util.ArrayList

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view){
    val binding = ItemRecomendedAlimentBinding.bind(view)

    fun render(aliment: AlimentoDto){

        binding.nameAliReco.text = aliment.nombre
        binding.descAliReco.text = aliment.tipo_plato
        Glide.with(binding.imgAliReco.context).load(aliment.imagen).into(binding.imgAliReco)
        itemView.setOnClickListener {

            val dataIntent = Intent(itemView.context , AlimentDetailActivity::class.java).apply {
                putExtra("ID", aliment.id)
                putExtra("NAME", aliment.nombre)
                putExtra("IMG", aliment.imagen)
                putExtra("DESCRIP", aliment.descripcion)
                putExtra("DIFICULT", aliment.dificultad)
                putExtra("TIPOPLA", aliment.tipo_plato)
                putExtra("BASEPLA", aliment.basePlatillo)
                putExtra("ESTILO", aliment.estilo_cocina)
                putExtra("TIME", aliment.tiempo_preparacion.toString())
                putStringArrayListExtra("INGRE", aliment.ingredientes as ArrayList<String>?)


            }
            itemView.context.startActivity(dataIntent)
        }
    }
}