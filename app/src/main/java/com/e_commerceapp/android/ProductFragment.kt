package com.e_commerceapp.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e_commerceapp.android.databinding.FragmentProductBinding
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_product.*
import kotlinx.android.synthetic.main.fragment_add_product.imgProduct
import kotlinx.android.synthetic.main.item_product.*
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
            rcyProduct.layoutManager = GridLayoutManager(binding.root.context,2)
            rcyProduct.setHasFixedSize(true)
            productList = arrayListOf<Product>()
            var database = FirebaseDatabase.getInstance().getReference("Product").child(productCategory)
            database.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (productSnapShot in snapshot.children){
                            val product = productSnapShot.getValue(Product::class.java)
                            productList.add(product!!)
                            Toast.makeText(binding.root.context,"başarıyla data geldi", Toast.LENGTH_LONG).show()
                            val storage = FirebaseStorage.getInstance().getReference("product").child(product.productId.toString())
                            val localFile = File.createTempFile("tempFile","jpg")
                            storage.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                imgProduct.setImageBitmap(bitmap)
                            }.addOnFailureListener{
                                Toast.makeText(binding.root.context,"failed to load",Toast.LENGTH_LONG).show()
                            }
                        }
                        rcyProduct.adapter = ProductAdapter(productList)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }


}