package com.e_commerceapp.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.e_commerceapp.android.DatabaseConstants.PRODUCT_DB
import com.e_commerceapp.android.databinding.FragmentProductBinding
import com.google.firebase.database.*

class ProductFragment : Fragment(), ProductAdapter.OnItemClickListener {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private var productList: ArrayList<Product> = arrayListOf()
    private lateinit var adapter: ProductAdapter
    private var productCategory: String? = null
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getParams()
        initDatabase()
        initUi()
        getProducts()
    }

    private fun getParams() {
        val args: HomeFragmentArgs by navArgs()
        productCategory = args.productCategory
    }

    private fun initDatabase() {
        database = FirebaseDatabase.getInstance().getReference(PRODUCT_DB)
            .child(productCategory!!)
    }

    private fun initUi() {
        adapter = ProductAdapter(productList, this)
        binding.apply {
            rcyProduct.adapter = adapter
            rcyProduct.layoutManager = GridLayoutManager(binding.root.context, 2)
            rcyProduct.setHasFixedSize(true)
        }
    }

    private fun getProducts(){
        database.child(productCategory!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    productList.clear()
                    for (productSnapShot in snapshot.children) {
                        val product = productSnapShot.getValue(Product::class.java)
                        productList.add(product!!)
                    }
                    productList.sortByDescending {
                        it.productName
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onItemClick(categoryName: String?, productId: String?) {
        if (productId.isNullOrEmpty().not() && categoryName.isNullOrEmpty().not()) {
            val action = ProductFragmentDirections.actionProductFragmentToProductDetailFragment(
                productId!!,
                categoryName!!
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}