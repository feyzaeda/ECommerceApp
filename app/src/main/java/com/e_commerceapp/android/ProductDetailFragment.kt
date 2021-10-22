package com.e_commerceapp.android

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.e_commerceapp.android.databinding.FragmentProductDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.io.File

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.apply {
            val args: ProductDetailFragmentArgs by navArgs()
            val getProductCategory = args.productCategory
            val getProductId = args.productId

            var firebase =
                FirebaseDatabase.getInstance().getReference("Product").child(getProductCategory).child(getProductId)
            var firebaseStorage = FirebaseStorage.getInstance().getReference("product").child(getProductId)
            firebase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val productCategory = snapshot.child("productCategory").value
                        val productExplanation = snapshot.child("productExplanation").value
                        val productName = snapshot.child("productName").value
                        val productPrice = snapshot.child("productPrice").value
                        val productImg = snapshot.child("productImg").value

                        tvProductCategory.text = productCategory.toString()
                        tvProductExplanation.text = productExplanation.toString()
                        tvProductName.text = productName.toString()
                        tvProductPrice.text = productPrice.toString()

                        Glide.with(binding.root.context).load(productImg).into(imgProduct)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            btnUpdateProduct.setOnClickListener {
                val action = ProductDetailFragmentDirections.actionProductDetailFragmentToUpdateProductFragment(getProductId,getProductCategory)
                findNavController().navigate(action)
            }
            btnDeleteProduct.setOnClickListener {
                firebase.removeValue().addOnSuccessListener {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_productDetailFragment_to_homeFragment)
                }
            }
        }
    }

}