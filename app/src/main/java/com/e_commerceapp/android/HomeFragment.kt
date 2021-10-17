package com.e_commerceapp.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e_commerceapp.android.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.*

class HomeFragment : Fragment(), HomeAdapter.onItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var categoryList : ArrayList<HomeCategory>
    lateinit var imageId: Array<Int>
    lateinit var categoryName: Array<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.apply {
            imageId = arrayOf(
                R.drawable.kozmetik,
                R.drawable.moda,
                R.drawable.elektronik,
                R.drawable.supermarket,
                R.drawable.kirtasiye
            )
            categoryName = resources.getStringArray(R.array.category).toList().toTypedArray()

            rcyViewHome.layoutManager = GridLayoutManager(binding.root.context,2)
            rcyViewHome.setHasFixedSize(true)
            categoryList = arrayListOf<HomeCategory>()
            getData()
        }
    }

    private fun getData() {
        for (i in imageId.indices){
            val category = HomeCategory(imageId[i],categoryName[i])
            categoryList.add(category)
        }
        var adapter = HomeAdapter(categoryList,this)
        binding.rcyViewHome.adapter = adapter


    }

    override fun onItemClick(categoryName: String) {
        Toast.makeText(binding.root.context,categoryName,Toast.LENGTH_LONG).show()
        //val sendData = categoryName.toString()
        val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(categoryName)
        //Navigation.findNavController(bottomNavigationView).navigate(action)
        findNavController().navigate(action)
    }

}