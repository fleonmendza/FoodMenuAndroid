package com.eflm.foodmenu.usecases.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eflm.foodmenu.R
import com.eflm.foodmenu.data.remote.model.AlimentoDto

class SearchAdapter(private val alimentosList: List<AlimentoDto>): RecyclerView.Adapter<SearchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchViewHolder(layoutInflater.inflate(R.layout.item_recomended_aliment, parent, false))
    }

    override fun getItemCount(): Int = alimentosList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = alimentosList[position]
        holder.render(item)
    }

}