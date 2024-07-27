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
import com.librarydevloperjo.cointracker.adapters.UpbitAdapter
import com.librarydevloperjo.cointracker.databinding.FragmentUpbitBinding
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.SortState
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import com.librarydevloperjo.cointracker.viewmodels.UpbitSortCriteria
import com.librarydevloperjo.cointracker.viewmodels.UpbitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpbitFragment : Fragment() {

    private var _binding:FragmentUpbitBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UpbitViewModel by viewModels()
    private val uiViewModel: UIViewModel by activityViewModels()
    @Inject
    lateinit var prefrence:PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpbitBinding.inflate(inflater, container, false)
        val adapter = UpbitAdapter(prefrence)
        binding.apply {
            rvUpbit.layoutManager = LinearLayoutManager(requireActivity())
            rvUpbit.adapter = adapter

            rootTabprice.setOnClickListener {
                viewModel.toggleSortState(UpbitSortCriteria.PRICE)
            }
            rootTabChangerate.setOnClickListener {
                viewModel.toggleSortState(UpbitSortCriteria.CHANGE_RATE)
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
            if (it.isNotEmpty()){
                binding.count = "(${it.size})"
                adapter.submitList(it)
            }
        }

        viewModel.sortState.observe(viewLifecycleOwner){ state ->
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                binding.rvUpbit.scrollToPosition(0)
            }

            binding.apply {
                when(state){
                    SortState.PRICE_ASCENDING -> {
                        sortChangeRateByDesc = null
                        sortPriceByDesc = false
                    }
                    SortState.PRICE_DESCENDING -> {
                        sortChangeRateByDesc = null
                        sortPriceByDesc = true
                    }
                    SortState.CHANGE_RATE_ASCENDING -> {
                        sortPriceByDesc = null
                        sortChangeRateByDesc = false
                    }
                    SortState.CHANGE_RATE_DESCENDING -> {
                        sortPriceByDesc = null
                        sortChangeRateByDesc = true
                    }
                    else -> {
                        sortChangeRateByDesc = null
                        sortPriceByDesc = null
                    }
                }
            }
        }
    }
}