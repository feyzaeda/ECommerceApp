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
import androidx.navigation.Navigation
import com.e_commerceapp.android.databinding.FragmentAddProductBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private var imgUri: Uri? = null
    private var productCategory: String? = null
    private var productName: String? = null
    private var productExplanetion: String? = null
    private var productPrice: String? = null
    private var strUri: String? = null

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
                val database = FirebaseDatabase.getInstance().getReference("Product")
                val productId = database.push().key.toString()


                if (productCategory.isNullOrEmpty().not() &&
                    txtProductName.text.isNullOrEmpty().not() &&
                    txtProductExplanation.text.isNullOrEmpty().not() &&
                    txtProductPrice.text.isNullOrEmpty().not() &&
                    imgUri.toString().isEmpty().not()
                ) {
                    val storage =
                        FirebaseStorage.getInstance().getReference("Product").child(productId)
                    storage.putFile(imgUri!!).addOnSuccessListener {
                        storage.downloadUrl.addOnSuccessListener {
                            strUri = it.toString()
                            productName = txtProductName.text.toString()
                            productExplanetion = txtProductExplanation.text.toString()
                            productPrice = txtProductPrice.text.toString()
                            val product = Product(
                                productName,
                                productExplanetion,
                                productCategory,
                                productPrice,
                                productId,
                                strUri
                            )
                            database.child(productCategory!!).child(productId)
                                .setValue(product)

                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_addProductFragment_to_homeFragment)
                        }
                    }

                } else {
                    Toast.makeText(
                        binding.root.context,
                        "Lütfen tüm alanları doldurunuz.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            imgUri = data?.data!!
            binding.imgAddProduct.setImageURI(imgUri)
        }
    }


}
