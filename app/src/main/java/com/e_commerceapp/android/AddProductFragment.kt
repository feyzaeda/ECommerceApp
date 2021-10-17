package com.e_commerceapp.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.e_commerceapp.android.databinding.FragmentAddProductBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var imgUri: Uri
    private lateinit var productCategory: String
    private lateinit var productName: String
    private lateinit var productExplanetion: String
    private lateinit var productPrice: String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.apply {
            val category = resources.getStringArray(R.array.category)
            val arrayAdapter =
                ArrayAdapter(requireContext(), R.layout.item_dropdown_category, category)
            autoCompleteCategory.setAdapter(arrayAdapter)

            autoCompleteCategory.setOnItemClickListener { parent, view, position, id ->
                productCategory = parent.getItemAtPosition(position).toString()
            }




            btnAddProductImg.setOnClickListener {
                val intent = Intent()
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 100)
            }

            btnAddProduct.setOnClickListener {
                var database = FirebaseDatabase.getInstance().reference
                var productId = database.push().key.toString()
                productName = txtProductName.text.toString()
                productExplanetion = txtProductExplanation.text.toString()
                productPrice = txtProductPrice.text.toString()
                val product = Product(
                    productName,
                    productExplanetion,
                    productCategory,
                    productPrice,
                    productId,
                    imgUri.toString()
                )
                database.child(productCategory).child(productId)
                    .setValue(product)
                var storage = FirebaseStorage.getInstance().getReference("product").child(productId)
                storage.putFile(imgUri).addOnCompleteListener {
                    /*Toast.makeText(
                        binding.root.context,
                        "resim yükleme başarılı",
                        Toast.LENGTH_LONG
                    ).show()*/
                }

            }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            imgUri = data?.data!!
            binding.imgProduct.setImageURI(imgUri)
        }
    }


}
