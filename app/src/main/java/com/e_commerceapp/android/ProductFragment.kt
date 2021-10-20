package com.e_commerceapp.android

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.e_commerceapp.android.databinding.FragmentProductBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.io.File

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productList: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.apply {
            val args: HomeFragmentArgs by navArgs()
            val productCategory = args.productCategory
            rcyProduct.layoutManager = GridLayoutManager(binding.root.context, 2)
            rcyProduct.setHasFixedSize(true)
            productList = arrayListOf<Product>()
            var database =
                FirebaseDatabase.getInstance().getReference("Product").child(productCategory)
            database.addValueEventListener(object : ValueEventListener,
                ProductAdapter.onItemClickListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (productSnapShot in snapshot.children) {
                            val product = productSnapShot.getValue(Product::class.java)
                            productList.add(product!!)
                            Toast.makeText(
                                binding.root.context,
                                "başarıyla data geldi",
                                Toast.LENGTH_LONG
                            ).show()
                            val storage = FirebaseStorage.getInstance().getReference("product")
                                .child(product.productId.toString())
                            val localFile = File.createTempFile("tempFile", "jpg")
                            storage.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                imgProduct.setImageBitmap(bitmap)
                            }
                        }
                        var adapter = ProductAdapter(productList, this)
                        rcyProduct.adapter = adapter

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onItemClick(categoryName: String?, productId: String?) {
                    Toast.makeText(
                        binding.root.context,
                        categoryName + productId,
                        Toast.LENGTH_LONG
                    ).show()
                }

            })


        }
    }


}