package com.e_commerceapp.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.e_commerceapp.android.DatabaseConstants.PRODUCT_DB
import com.e_commerceapp.android.databinding.FragmentUpdateProductBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class UpdateProductFragment : Fragment() {

    private var _binding: FragmentUpdateProductBinding? = null
    private val binding get() = _binding!!
    private var imgUriUpdate: Uri? = null
    private var currentProduct: Product? = null
    private lateinit var database: DatabaseReference
    private var productId: String? = null
    private var productCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getParams()
        setAutoComplete()
        initUi()
        getProduct()
    }

    private fun getParams() {
        val args: UpdateProductFragmentArgs by navArgs()
        productCategory = args.productCategory
        productId = args.productId
    }

    private fun setAutoComplete() {
        val category = resources.getStringArray(R.array.category)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_category, category)
        binding.autoCompleteCategoryUpdate.setAdapter(arrayAdapter)
        binding.autoCompleteCategoryUpdate.setOnItemClickListener { parent, view, position, id ->
            currentProduct?.let {
                it.productCategory = parent.getItemAtPosition(position).toString()
            }
        }
    }

    private fun initUi() {
        binding.apply {
            btnAddProductImgUpdate.setOnClickListener { selectImageFromFiles() }
            btnUpdate.setOnClickListener {
                if (currentProduct?.productCategory.equals(productCategory)) {
                    updateProduct()
                } else {
                    database.child(productCategory!!).child(productId!!).removeValue()
                        .addOnSuccessListener { updateProduct() }
                }
            }
        }
    }

    private fun selectImageFromFiles() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    private fun getProduct() {
        database = FirebaseDatabase.getInstance().getReference(PRODUCT_DB)
        database.child(productCategory!!).child(productId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (currentProduct == null) {
                        val productExplanation =
                            snapshot.child("productExplanation").value.toString()
                        val productName = snapshot.child("productName").value.toString()
                        val productPrice = snapshot.child("productPrice").value.toString()
                        val imgUri = snapshot.child("productImg").value.toString()
                        val productCategory = snapshot.child("productCategory").value.toString()
                        currentProduct = Product(
                            productName,
                            productExplanation,
                            productCategory,
                            productPrice,
                            productId,
                            imgUri
                        )
                        binding.apply {
                            txtProductExplanationUpdate.setText(productExplanation)
                            txtProductNameUpdate.setText(productName)
                            txtProductPriceUpdate.setText(productPrice)
                            Glide.with(binding.root.context).load(imgUri).into(imgProductUpdate)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun updateProduct() {
        val storage = FirebaseStorage.getInstance().getReference(PRODUCT_DB)
            .child(currentProduct!!.productCategory!!).child(currentProduct!!.productId!!)
        currentProduct!!.productName = binding.txtProductNameUpdate.text.toString()
        currentProduct!!.productExplanation = binding.txtProductExplanationUpdate.text.toString()
        currentProduct!!.productPrice = binding.txtProductPriceUpdate.text.toString()
        if (imgUriUpdate != null) {
            storage.putFile(imgUriUpdate!!).addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    updateDatabase()
                }
            }
        } else {
            updateDatabase()
        }
    }

    private fun updateDatabase() {
        database.child(currentProduct!!.productCategory!!)
            .child(currentProduct!!.productId!!).setValue(currentProduct).addOnSuccessListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_updateProductFragment_to_homeFragment)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            imgUriUpdate = data?.data!!
            binding.imgProductUpdate.setImageURI(imgUriUpdate)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}