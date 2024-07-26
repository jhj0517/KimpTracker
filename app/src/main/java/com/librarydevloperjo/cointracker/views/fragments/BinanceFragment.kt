package com.librarydevloperjo.cointracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.librarydevloperjo.cointracker.adapters.BinanceAdapter
import com.librarydevloperjo.cointracker.databinding.FragmentBinanceBinding
import com.librarydevloperjo.cointracker.viewmodels.BinanceViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BinanceFragment : Fragment() {

    private var _binding: FragmentBinanceBinding?=null
    private val binding get() = _binding!!
    private val viewModel: BinanceViewModel by viewModels()
    private val uiViewModel: UIViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val adapter = BinanceAdapter()
        _binding = FragmentBinanceBinding.inflate(inflater, container, false)
        binding.apply {
            rvBinance.layoutManager = LinearLayoutManager(requireActivity())
            rvBinance.adapter = adapter

            rootTabprice.setOnClickListener {
                viewModel.toggleSortState()
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
            binding.isLoaded = !it.isNullOrEmpty()

            if (it.isNotEmpty()){
                binding.tvTotalnum.text = "(${it.size})"
                adapter.submitList(it)
            }
        }

        viewModel.sortState.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                binding.rvBinance.scrollToPosition(0)
            }
        }
    }

}