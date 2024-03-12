package com.librarydevloperjo.cointracker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MediatorLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.librarydevloperjo.cointracker.adapters.KpremiumAdapter
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.databinding.FragmentKimpBinding
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import com.librarydevloperjo.cointracker.views.dialogfragments.WidgetMakerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KimpFragment : Fragment(),KpremiumAdapter.ClickCallback {

    private var _binding: FragmentKimpBinding? =null
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
        val adapter = KpremiumAdapter(this)
        _binding = FragmentKimpBinding.inflate(inflater, container, false)
        binding.apply {
            rvKimp.layoutManager = LinearLayoutManager(requireActivity())
            rvKimp.adapter = adapter

            rootTabprice.setOnClickListener {
                when(viewmodel.kPremiumSortState.value){
                    PRICE_ASCENDING -> {
                        sortKimpByDesc = null
                        sortPriceByDesc = true
                        viewmodel.kPremiumSortState.value = PRICE_DESCENDING
                    }
                    else -> {
                        sortKimpByDesc = null
                        sortPriceByDesc = false
                        viewmodel.kPremiumSortState.value = PRICE_ASCENDING
                    }
                }
            }

            rootTabkp.setOnClickListener {
                when(viewmodel.kPremiumSortState.value){
                    KIMP_ASCENDING -> {
                        sortPriceByDesc = null
                        sortKimpByDesc = true
                        viewmodel.kPremiumSortState.value = KIMP_DESCENDING
                    }
                    else -> {
                        sortPriceByDesc = null
                        sortKimpByDesc = false
                        viewmodel.kPremiumSortState.value = KIMP_ASCENDING
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

    override fun onItemClicked(items: KPremiumData) {
        val dialog = WidgetMakerFragment.newInstance(items)
        dialog.show(requireActivity().supportFragmentManager,"dialog")
    }

    private fun subscribeUI(adapter:KpremiumAdapter){
        viewmodel.excRate.observe(viewLifecycleOwner){ exc->
            binding.exc = exc
        }

        val combined = MediatorLiveData<Pair<ArrayList<KPremiumData>, Int>>()
        combined.addSource(viewmodel.kPremiumList){newValue ->
            combined.value = Pair(newValue, viewmodel.kPremiumSortState.value!!)
        }
        combined.addSource(viewmodel.kPremiumSortState){newValue->
            combined.value = Pair(viewmodel.kPremiumList.value!!, newValue)
        }

        combined.observe(viewLifecycleOwner){ (data, state) ->
            binding.isLoaded = !data.isEmpty()
            binding.count = "(${data.size})"

            val unSorted = ArrayList(data)
            val sorted = viewmodel.sortByState(state, unSorted)
            adapter.submitList(sorted)
        }
    }

}
const val PRICE_ASCENDING = 0
const val PRICE_DESCENDING = 1
const val KIMP_ASCENDING = 2
const val KIMP_DESCENDING = 3