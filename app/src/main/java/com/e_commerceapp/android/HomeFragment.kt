package com.e_commerceapp.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.e_commerceapp.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), HomeAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        getData()
    }

    private fun initUi() {
        binding.apply {
            rcyViewHome.layoutManager = GridLayoutManager(binding.root.context, 2)
            rcyViewHome.setHasFixedSize(true)
        }
    }

    private fun getData() {
        val imageId = arrayOf(
            R.drawable.kozmetik,
            R.drawable.moda,
            R.drawable.elektronik,
            R.drawable.supermarket,
            R.drawable.kirtasiye
        )
        val categoryName = resources.getStringArray(R.array.category).toList().toTypedArray()
        val categoryList = arrayListOf<HomeCategory>()
        for (i in imageId.indices) {
            val category = HomeCategory(imageId[i], categoryName[i])
            categoryList.add(category)
        }
        val adapter = HomeAdapter(categoryList, this)
        binding.rcyViewHome.adapter = adapter
    }

    override fun onItemClick(categoryName: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(categoryName)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}