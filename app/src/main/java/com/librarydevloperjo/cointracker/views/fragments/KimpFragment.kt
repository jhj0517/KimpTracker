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
import com.librarydevloperjo.cointracker.adapters.KpremiumAdapter
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.databinding.FragmentKimpBinding
import com.librarydevloperjo.cointracker.util.MatchCoins
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import com.librarydevloperjo.cointracker.views.dialogfragments.WidgetMakerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class KimpFragment : Fragment(),KpremiumAdapter.ClickCallback {

    private var adapter:KpremiumAdapter = KpremiumAdapter(this)
    private var _binding: FragmentKimpBinding? =null
    private val binding get() = _binding!!
    private val viewmodel: CoinsViewModel by activityViewModels()
    private val uiViewmodel: UIViewModel by activityViewModels()
    @Inject
    lateinit var matchcoins:MatchCoins

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentKimpBinding.inflate(inflater, container, false)
        binding.apply {
            rvKimp.layoutManager = LinearLayoutManager(requireActivity())
            rvKimp.adapter = adapter

            rootTabprice.setOnClickListener {
                val i = viewmodel.kPremiumSortState.value!!
                when(i){
                    PRICE_ASCENDING -> {sortRV(PRICE_DESCENDING, adapter.currentList.sortedByDescending { it.upbitPrice })
                        ivSortprice.setImageResource(R.drawable.sortdescending)
                        ivSortkp.setImageResource(R.drawable.sort)}
                    PRICE_DESCENDING -> {sortRV(PRICE_ASCENDING, adapter.currentList.sortedBy { it.upbitPrice })
                        ivSortprice.setImageResource(R.drawable.sortascending)
                        ivSortkp.setImageResource(R.drawable.sort)}
                    else -> {sortRV(PRICE_DESCENDING, adapter.currentList.sortedByDescending { it.upbitPrice })
                        ivSortprice.setImageResource(R.drawable.sortdescending)
                        ivSortkp.setImageResource(R.drawable.sort)}
                }
            }
            rootTabkp.setOnClickListener {
                val i = viewmodel.kPremiumSortState.value!!
                when(i){
                    CHANGE_RATE_ASCENDING -> {sortRV(CHANGE_RATE_DESCENDING, adapter.currentList.sortedByDescending { it.kPremium })
                            ivSortkp.setImageResource(R.drawable.sortdescending)
                            ivSortprice.setImageResource(R.drawable.sort)}
                    CHANGE_RATE_DESCENDING -> {sortRV(CHANGE_RATE_ASCENDING, adapter.currentList.sortedBy { it.kPremium })
                            ivSortkp.setImageResource(R.drawable.sortascending)
                            ivSortprice.setImageResource(R.drawable.sort)}
                    else -> {sortRV(CHANGE_RATE_DESCENDING, adapter.currentList.sortedByDescending { it.kPremium })
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

    override fun onItemClicked(items: KPremiumData) {
        val dialog = WidgetMakerFragment.newInstance(items)
        dialog.show(requireActivity().supportFragmentManager,"dialog")
    }

    private fun sortRV(state:Int, sorted:List<KPremiumData>){
        viewmodel.kPremiumSortState.value = state
        adapter.submitList(sorted){binding.rvKimp.scrollToPosition(0)}
    }

    private fun subscribeUI(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.coinPricesTickFlow.collectLatest {
                    val exc = it.exc.get(0).deal_bas_r
                    val binance = it.binance
                    val upbits = it.upbit
                    val list = matchcoins.match(upbits,binance,exc)
                    viewmodel.kPremiumList.value = list
                    viewmodel.excRate.value = NumberFormat.getNumberInstance(Locale.US).format(exc)
                }
            }
        }

        viewmodel.excRate.observe(viewLifecycleOwner){ exc->
            binding.tvExcrate.text = getString(R.string.ExcRate) + " : " + exc
        }
        viewmodel.kPremiumList.observe(viewLifecycleOwner){ res ->
            binding.isLoaded = !res.isNullOrEmpty()

            val list = mutableListOf<KPremiumData>()
            list.addAll(res)

            when(viewmodel.kPremiumSortState.value){
                PRICE_ASCENDING -> list.sortBy { it.upbitPrice }
                PRICE_DESCENDING -> list.sortByDescending { it.upbitPrice }
                CHANGE_RATE_ASCENDING -> list.sortBy { it.kPremium }
                CHANGE_RATE_DESCENDING -> list.sortByDescending { it.kPremium }
            }

            val bookmarks = viewmodel.getAllKData().toMutableList()

            list.forEachIndexed { index,data ->
                var isSame = false
                bookmarks.forEach { b ->
                    if(data.coinName == b.coinName){
                        isSame = true
                    }
                }
                data.isBookmark = isSame
                list[index] = data
            }
            list.sortByDescending { it.isBookmark }

            adapter.submitList(list)
            binding.rvKimp.adapter!!.notifyDataSetChanged()
            binding.tvTotalnum.text = "(${list.size})"
        }
    }

    companion object {
        private const val PRICE_ASCENDING = 0
        private const val PRICE_DESCENDING = 1
        private const val CHANGE_RATE_ASCENDING = 10
        private const val CHANGE_RATE_DESCENDING = 11
    }
}