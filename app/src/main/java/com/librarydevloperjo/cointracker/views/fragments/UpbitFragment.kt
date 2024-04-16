package com.librarydevloperjo.cointracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.librarydevloperjo.cointracker.adapters.UpbitAdapter
import com.librarydevloperjo.cointracker.databinding.FragmentUpbitBinding
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.SortState
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UpbitFragment : Fragment() {

    private var _binding:FragmentUpbitBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoinsViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by activityViewModels()
    @Inject
    lateinit var prefrence:PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUpbitBinding.inflate(inflater, container, false)
        val adapter = UpbitAdapter(prefrence)
        binding.apply {
            rvUpbit.layoutManager = LinearLayoutManager(requireActivity())
            rvUpbit.adapter = adapter

            rootTabprice.setOnClickListener {
                when(viewModel.sortState.value!!){
                    SortState.PRICE_ASCENDING -> {
                        sortPriceByDesc = true
                        sortChangeRateByDesc = null
                        viewModel.sortState.value = SortState.PRICE_DESCENDING
                    }
                    else -> {
                        sortPriceByDesc = false
                        sortChangeRateByDesc = null
                        viewModel.sortState.value = SortState.PRICE_ASCENDING
                    }
                }
            }
            rootTabChangerate.setOnClickListener {
                when(viewModel.sortState.value!!){
                    SortState.CHANGE_RATE_ASCENDING -> {
                        sortChangeRateByDesc = true
                        sortPriceByDesc = null
                        viewModel.sortState.value = SortState.CHANGE_RATE_DESCENDING
                    }
                    else -> {
                        sortChangeRateByDesc = false
                        sortPriceByDesc = null
                        viewModel.sortState.value = SortState.CHANGE_RATE_ASCENDING
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

    private fun subscribeUI(adapter: UpbitAdapter){
        viewModel.upbitList.observe(viewLifecycleOwner){
            binding.isLoaded = !it.isNullOrEmpty()
            binding.count = "(${it.size})"

            adapter.submitList(it)
        }

        viewModel.sortState.observe(viewLifecycleOwner){
            val sorted = viewModel.sortUpbitByState(it, viewModel.upbitList.value!!)
            adapter.submitList(sorted){ binding.rvUpbit.scrollToPosition(0) }
        }
    }
}