package com.e_commerceapp.android

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e_commerceapp.android.databinding.ItemProductBinding
import com.google.firebase.database.ValueEventListener

class ProductAdapter(private val productList: ArrayList<Product>, private val listener: ProductAdapter.onItemClickListener):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface onItemClickListener{
        fun onItemClick(categoryName: String?, productId: String?)
    }

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
                    Glide.with(itemProductBinding.root.context).load(item.productImg).into(imgProductItem)
                }
            }
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(productList[adapterPosition].productCategory, productList[adapterPosition].productId)

            }
        }

    }
}