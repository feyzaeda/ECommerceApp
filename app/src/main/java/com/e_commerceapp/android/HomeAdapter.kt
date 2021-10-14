package com.e_commerceapp.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_commerceapp.android.databinding.ItemHomeBinding

class HomeAdapter(private val categoryList : ArrayList<HomeCategory>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(private val itemHomeBinding: ItemHomeBinding) : RecyclerView.ViewHolder(itemHomeBinding.root) {

        fun bind(position: Int){
            val item = categoryList[position]
            item.apply {
                itemHomeBinding.apply {
                    imgHome.setImageResource(item.categoryImg)
                    tvCategoryName.text = item.categoryName
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemBinding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}