package com.e_commerceapp.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.e_commerceapp.android.databinding.FragmentAddProductBinding
import com.e_commerceapp.android.databinding.FragmentUpdateProductBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UpdateProductFragment : Fragment() {

    private var _binding: FragmentUpdateProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var imgUriUpdate: Uri
    private lateinit var productCategory: String
    private lateinit var productName: String
    private lateinit var productExplanetion: String
    private lateinit var productPrice: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.apply {
            val args: UpdateProductFragmentArgs by navArgs()
            val getProductCategory = args.productCategory
            val getProductId = args.productId

            val category = resources.getStringArray(R.array.category)
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.item_dropdown_category, category)
            autoCompleteCategoryUpdate.setAdapter(arrayAdapter)

            autoCompleteCategoryUpdate.setOnItemClickListener { parent, view, position, id ->
                productCategory = parent.getItemAtPosition(position).toString()
            }


            btnAddProductImgUpdate.setOnClickListener {
                val intent = Intent()
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 100)
            }

            btnUpdate.setOnClickListener {
                var database = FirebaseDatabase.getInstance().getReference("Product")
                //var productId = database.push().key.toString()
                productName = txtProductNameUpdate.text.toString()
                productExplanetion = txtProductExplanationUpdate.text.toString()
                productPrice = txtProductPriceUpdate.text.toString()
                val product = Product(
                    productName,
                    productExplanetion,
                    productCategory,
                    productPrice,
                    getProductId,
                    imgUriUpdate.toString()
                )
                database.child(getProductCategory).child(getProductId)
                    .setValue(product)
                var storage = FirebaseStorage.getInstance().getReference("product").child(getProductId)
                storage.putFile(imgUriUpdate).addOnCompleteListener {
                }

            }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            imgUriUpdate = data?.data!!
            binding.imgProductUpdate.setImageURI(imgUriUpdate)
        }
    }


}