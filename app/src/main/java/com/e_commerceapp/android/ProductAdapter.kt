package com.e_commerceapp.android

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e_commerceapp.android.databinding.ItemHomeBinding
import com.e_commerceapp.android.databinding.ItemProductBinding

class ProductAdapter(private val productList: ArrayList<Product>):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemBinding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(itemBinding)    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return  productList.size
    }

    inner class ProductViewHolder(private val itemProductBinding: ItemProductBinding): RecyclerView.ViewHolder(itemProductBinding.root) {
        fun bind(position: Int) {
            val item = productList[position]
            item.apply {
                itemProductBinding.apply {
                    tvProductName.text = item.productName
                    tvProductPrice.text = item.productPrice
                }
            }
        }

    }
}