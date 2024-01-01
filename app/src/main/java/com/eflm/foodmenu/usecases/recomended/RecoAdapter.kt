package com.eflm.foodmenu.usecases.recomended

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eflm.foodmenu.R
import com.eflm.foodmenu.data.remote.model.AlimentoDto

class RecoAdapter(private val alimentosList:List<AlimentoDto>) : RecyclerView.Adapter<RecomendedViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecomendedViewHolder(layoutInflater.inflate(R.layout.item_recomended_aliment, parent, false, ))
    }

    override fun onBindViewHolder(holder: RecomendedViewHolder, position: Int) {
        val item = alimentosList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = alimentosList.size

}