package com.e_commerceapp.android

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.e_commerceapp.android.databinding.FragmentProductDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

            firebase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val productCategory = snapshot.child("productCategory").value
                        val productExplanation = snapshot.child("productExplanation").value
                        val productName = snapshot.child("productName").value
                        val productPrice = snapshot.child("productPrice").value

                        tvProductCategory.text = productCategory.toString()
                        tvProductExplanation.text = productExplanation.toString()
                        tvProductName.text = productName.toString()
                        tvProductPrice.text = productPrice.toString()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}