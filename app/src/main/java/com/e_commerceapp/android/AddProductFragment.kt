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
import com.e_commerceapp.android.DatabaseConstants.PRODUCT_DB
import com.e_commerceapp.android.databinding.FragmentAddProductBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private var imgUri: Uri? = null
    private var productCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        setAutoComplete()
    }

    private fun initUi() {
        binding.apply {
            btnAddProductImg.setOnClickListener { selectImageFromFiles() }
            btnAddProduct.setOnClickListener { addProduct() }
        }
    }

    private fun addProduct() {
        val database = FirebaseDatabase.getInstance().getReference("Product")
        val productId = database.push().key.toString()
        val productName = binding.txtProductName.text.toString()
        val productExplanation = binding.txtProductExplanation.text.toString()
        val productPrice = binding.txtProductPrice.text.toString()

        if (productCategory.isNullOrEmpty().not() &&
            productName.isEmpty().not() &&
            productExplanation.isEmpty().not() &&
            productPrice.isEmpty().not() &&
            imgUri.toString().isEmpty().not()
        ) {
            val storage = FirebaseStorage.getInstance().getReference(PRODUCT_DB).child(productId)
            storage.putFile(imgUri!!).addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    val product = Product(
                        productName,
                        productExplanation,
                        productCategory,
                        productPrice,
                        productId,
                        it.toString()
                    )
                    database.child(productCategory!!).child(productId).setValue(product)
                        .addOnSuccessListener {
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_addProductFragment_to_homeFragment)
                        }
                }
            }
        } else {
            Toast.makeText(requireContext(), R.string.empty_fields_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun selectImageFromFiles() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    private fun setAutoComplete() {
        val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_category, category)
        binding.autoCompleteCategory.setAdapter(arrayAdapter)
        binding.autoCompleteCategory.setOnItemClickListener { parent, view, position, id ->
            productCategory = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            imgUri = data?.data!!
            binding.imgAddProduct.setImageURI(imgUri)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}