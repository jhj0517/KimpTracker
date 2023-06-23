package com.librarydevloperjo.cointracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.adapters.BinanceAdapter
import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.databinding.FragmentBinanceBinding
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BinanceFragment : Fragment() {

    private var _binding: FragmentBinanceBinding?=null
    private val binding get() = _binding!!
    private val viewmodel: CoinsViewModel by activityViewModels()
    private val uiViewmodel: UIViewModel by activityViewModels()
    private val adapter = BinanceAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBinanceBinding.inflate(inflater, container, false)
        binding.apply {
            rvBinance.layoutManager = LinearLayoutManager(requireActivity())
            rvBinance.adapter = adapter

            rootTabprice.setOnClickListener {
                val i = viewmodel.binanceSortState.value!!
                when(i){
                    PRICE_ASCENDING -> {sortRV(PRICE_DESCENDING, adapter.currentList.sortedByDescending { it.price })
                        ivSortprice.setImageResource(R.drawable.sortdescending)}
                    PRICE_DESCENDING -> {sortRV(PRICE_ASCENDING, adapter.currentList.sortedBy { it.price })
                        ivSortprice.setImageResource(R.drawable.sortascending)}
                    else -> {sortRV(PRICE_DESCENDING, adapter.currentList.sortedByDescending { it.price })
                        ivSortprice.setImageResource(R.drawable.sortdescending)}
                }
            }

            btnSetting.setOnClickListener {
                uiViewmodel.isInfoFragment.value = true
            }
        }
        subscribeUI()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sortRV(state:Int,sorted:List<BinanceCoin>){
        viewmodel.binanceSortState.value = state
        adapter.submitList(sorted){binding.rvBinance.scrollToPosition(0)}
    }

    private fun subscribeUI(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.coinPricesTickFlow.collectLatest {
                    val binance = it.binance
                    when(viewmodel.binanceSortState.value){
                        PRICE_ASCENDING -> binance.sortBy { it.price }
                        PRICE_DESCENDING -> binance.sortByDescending { it.price }
                    }

                    viewmodel.binanceList.value = binance
                }
            }
        }

        viewmodel.binanceList.observe(viewLifecycleOwner) {
            binding.isLoaded = !it.isNullOrEmpty()
            adapter.submitList(it)
            binding.tvTotalnum.text = "(${it.size})"
        }
    }

    companion object {
        private const val PRICE_ASCENDING = 0
        private const val PRICE_DESCENDING = 1
    }
}