package com.e_commerceapp.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.e_commerceapp.android.DatabaseConstants.PRODUCT_DB
import com.e_commerceapp.android.databinding.FragmentProductDetailBinding
import com.google.firebase.database.*

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private var productId: String? = null
    private var productCategory: String? = null
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getParams()
        initDatabase()
        initUi()
        getProductDetail()
    }

    private fun getParams() {
        val args: ProductDetailFragmentArgs by navArgs()
        productCategory = args.productCategory
        productId = args.productId
    }

    private fun initDatabase() {
        database = FirebaseDatabase.getInstance().getReference(PRODUCT_DB).child(productCategory!!)
            .child(productId!!)
    }

    private fun initUi() {
        binding.apply {
            btnUpdateProduct.setOnClickListener {
                val action =
                    ProductDetailFragmentDirections.actionProductDetailFragmentToUpdateProductFragment(
                        productId!!,
                        productCategory!!
                    )
                findNavController().navigate(action)
            }
            btnDeleteProduct.setOnClickListener { deleteProduct() }
        }
    }

    private fun getProductDetail() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val productCategory = snapshot.child("productCategory").value
                    val productExplanation = snapshot.child("productExplanation").value
                    val productName = snapshot.child("productName").value
                    val productPrice = snapshot.child("productPrice").value
                    val productImg = snapshot.child("productImg").value

                    binding.apply {
                        tvProductCategory.text = productCategory.toString()
                        tvProductExplanation.text = productExplanation.toString()
                        tvProductName.text = productName.toString()
                        tvProductPrice.text = productPrice.toString()
                        Glide.with(binding.root.context).load(productImg).into(imgProduct)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun deleteProduct() {
        database.removeValue().addOnSuccessListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_productDetailFragment_to_homeFragment)
        }
    }
}