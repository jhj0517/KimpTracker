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
    private val viewModel: CoinsViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by activityViewModels()

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
                when(viewModel.binanceSortState.value){
                    PRICE_ASCENDING -> {
                        sortPriceByDesc = true
                        viewModel.binanceSortState.value = PRICE_DESCENDING
                    }
                    else -> {
                        sortPriceByDesc = false
                        viewModel.binanceSortState.value = PRICE_ASCENDING
                    }
                }
            }

            btnSetting.setOnClickListener {
                uiViewModel.isInfoFragment.value = true
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
        viewModel.binanceList.observe(viewLifecycleOwner){
            binding.isLoaded = it.isNotEmpty()
            binding.tvTotalnum.text = "(${it.size})"

            val sorted = viewModel.sortBinanceByState(viewModel.binanceSortState.value!!, it)
            adapter.submitList(sorted)
        }

        viewModel.binanceSortState.observe(viewLifecycleOwner){
            val sorted = viewModel.sortBinanceByState(it, viewModel.binanceList.value!!)
            adapter.submitList(sorted){ binding.rvBinance.scrollToPosition(0) }
        }
    }

}