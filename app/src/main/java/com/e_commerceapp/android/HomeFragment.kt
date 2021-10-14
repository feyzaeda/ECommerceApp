package com.e_commerceapp.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e_commerceapp.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

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
            categoryName = arrayOf("Kozmetik", "Moda", "Eletronik", "Süpermarket", "Kırtasiye")

            binding.rcyViewHome.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rcyViewHome.setHasFixedSize(true)
            categoryList = arrayListOf<HomeCategory>()
            getData()
        }
    }

    private fun getData() {
        for (i in imageId.indices){
            val category = HomeCategory(imageId[i],categoryName[i])
            categoryList.add(category)
        }
        binding.rcyViewHome.adapter = HomeAdapter(categoryList)
    }


}