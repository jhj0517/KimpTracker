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
import com.librarydevloperjo.cointracker.adapters.UpbitAdapter
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin
import com.librarydevloperjo.cointracker.databinding.FragmentUpbitBinding
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpbitFragment : Fragment() {

    private var _binding:FragmentUpbitBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: CoinsViewModel by activityViewModels()
    private val uiViewmodel: UIViewModel by activityViewModels()
    private val adapter = UpbitAdapter()
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
        binding.apply {
            rvUpbit.layoutManager = LinearLayoutManager(requireActivity())
            rvUpbit.adapter = adapter

            rootTabprice.setOnClickListener {
                val sortState = viewmodel.upbitSortState.value!!
                when(sortState){
                    PRICE_ASCENDING -> {sortRV(PRICE_DESCENDING, adapter.currentList.sortedByDescending { it.tradePrice })
                        ivSortprice.setImageResource(R.drawable.sortdescending)
                        ivSortkp.setImageResource(R.drawable.sort)}
                    PRICE_DESCENDING -> {sortRV(PRICE_ASCENDING, adapter.currentList.sortedBy { it.tradePrice })
                        ivSortprice.setImageResource(R.drawable.sortascending)
                        ivSortkp.setImageResource(R.drawable.sort)}
                    else -> {sortRV(PRICE_DESCENDING, adapter.currentList.sortedByDescending { it.tradePrice })
                        ivSortprice.setImageResource(R.drawable.sortdescending)
                        ivSortkp.setImageResource(R.drawable.sort)}
                }
            }
            rootTabChangerate.setOnClickListener {
                val sortState = viewmodel.upbitSortState.value!!
                when(sortState){
                    CHANGE_RATE_ASCENDING -> {sortRV(CHANGE_RATE_DESCENDING, adapter.currentList.sortedByDescending { it.changeRate })
                        ivSortkp.setImageResource(R.drawable.sortdescending)
                        ivSortprice.setImageResource(R.drawable.sort)}
                    CHANGE_RATE_DESCENDING -> {sortRV(CHANGE_RATE_ASCENDING, adapter.currentList.sortedBy { it.changeRate })
                        ivSortkp.setImageResource(R.drawable.sortascending)
                        ivSortprice.setImageResource(R.drawable.sort)}
                    else -> {sortRV(CHANGE_RATE_DESCENDING, adapter.currentList.sortedByDescending { it.changeRate })
                        ivSortkp.setImageResource(R.drawable.sortdescending)
                        ivSortprice.setImageResource(R.drawable.sort)}
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

    private fun sortRV(state:Int,sorted:List<UpbitCoin>){
        viewmodel.upbitSortState.value = state
        adapter.submitList(sorted){binding.rvUpbit.scrollToPosition(0)}
    }

    private fun subscribeUI(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.coinPricesTickFlow.collectLatest {
                    val upbit = it.upbit
//                    upbit.forEach {
//                        it. = if(prefrence.getInt(LOCALE_KEY)== LOCALE_KOREAN) it.korean_name else it.english_name
//                        if(it.textview_name.isNullOrEmpty()) it.textview_name = it.coin.replace("KRW-","")
//                    }
                    viewmodel.upbitList.value = upbit
                }
            }
        }
        viewmodel.upbitList.observe(viewLifecycleOwner){ upbit->
            binding.isLoaded = !upbit.isNullOrEmpty()

            when(viewmodel.upbitSortState.value){
                PRICE_ASCENDING -> upbit.sortBy { it.tradePrice }
                PRICE_DESCENDING -> upbit.sortByDescending { it.tradePrice }
                CHANGE_RATE_ASCENDING -> upbit.sortBy { it.changeRate }
                CHANGE_RATE_DESCENDING -> upbit.sortByDescending { it.changeRate }
            }
            adapter.submitList(upbit)
            binding.tvTotalnum.text = "(${upbit.size})"
        }
    }

    companion object {
        private const val PRICE_ASCENDING = 0
        private const val PRICE_DESCENDING = 1
        private const val CHANGE_RATE_ASCENDING = 10
        private const val CHANGE_RATE_DESCENDING = 11
    }
}