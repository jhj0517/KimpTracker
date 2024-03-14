package com.librarydevloperjo.cointracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.librarydevloperjo.cointracker.adapters.BinanceAdapter
import com.librarydevloperjo.cointracker.databinding.FragmentBinanceBinding
import com.librarydevloperjo.cointracker.util.PRICE_ASCENDING
import com.librarydevloperjo.cointracker.util.PRICE_DESCENDING
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BinanceFragment : Fragment() {

    private var _binding: FragmentBinanceBinding?=null
    private val binding get() = _binding!!
    private val viewmodel: CoinsViewModel by activityViewModels()
    private val uiViewmodel: UIViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val adapter = BinanceAdapter()
        _binding = FragmentBinanceBinding.inflate(inflater, container, false)
        binding.apply {
            rvBinance.layoutManager = LinearLayoutManager(requireActivity())
            rvBinance.adapter = adapter

            rootTabprice.setOnClickListener {
                when(viewmodel.binanceSortState.value){
                    PRICE_ASCENDING -> {
                        sortPriceByDesc = true
                        viewmodel.binanceSortState.value = PRICE_DESCENDING
                    }
                    else -> {
                        sortPriceByDesc = false
                        viewmodel.binanceSortState.value = PRICE_ASCENDING
                    }
                }
            }

            btnSetting.setOnClickListener {
                uiViewmodel.isInfoFragment.value = true
            }
        }
        subscribeUI(adapter)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeUI(adapter: BinanceAdapter){
        viewmodel.binanceList.observe(viewLifecycleOwner){
            binding.isLoaded = it.isNotEmpty()
            binding.tvTotalnum.text = "(${it.size})"

            val sorted = viewmodel.sortBinanceByState(viewmodel.binanceSortState.value!!, it)
            adapter.submitList(sorted)
        }

        viewmodel.binanceSortState.observe(viewLifecycleOwner){
            val sorted = viewmodel.sortBinanceByState(it, viewmodel.binanceList.value!!)
            adapter.submitList(sorted){ binding.rvBinance.scrollToPosition(0) }
        }
    }

}