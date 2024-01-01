package com.eflm.foodmenu.usecases.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eflm.foodmenu.R
import com.eflm.foodmenu.data.remote.model.AlimentoDto
import com.eflm.foodmenu.model.Alimento


class favoritesAlimentAdapter(private val AlimentosList: List<AlimentoDto>): RecyclerView.Adapter<favoritesViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return favoritesViewHolder(layoutInflater.inflate(R.layout.item_fav_aliment, parent, false))
    }

    override fun onBindViewHolder(holder: favoritesViewHolder, position: Int) {
        val item = AlimentosList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = AlimentosList.size


}