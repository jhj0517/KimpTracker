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
import com.librarydevloperjo.cointracker.util.KIMP_ASCENDING
import com.librarydevloperjo.cointracker.util.KIMP_DESCENDING
import com.librarydevloperjo.cointracker.util.PRICE_ASCENDING
import com.librarydevloperjo.cointracker.util.PRICE_DESCENDING
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import com.librarydevloperjo.cointracker.views.dialogfragments.WidgetMakerFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KimpFragment : Fragment(),KpremiumAdapter.ClickCallback {

    private var _binding: FragmentKimpBinding? =null
    private val binding get() = _binding!!
    private val viewmodel: CoinsViewModel by activityViewModels()
    private val uiViewmodel: UIViewModel by activityViewModels()

    @Inject
    lateinit var preference: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentKimpBinding.inflate(inflater, container, false)

        val adapter = KpremiumAdapter(this,preference)
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

        viewmodel.kPremiumList.observe(viewLifecycleOwner){
            binding.isLoaded = !it.isEmpty()
            binding.count = "(${it.size})"

            val sorted = viewmodel.sortKimpByState(viewmodel.kPremiumSortState.value!!, it)
            adapter.submitList(sorted)
        }

        viewmodel.kPremiumSortState.observe(viewLifecycleOwner){
            val sorted = viewmodel.sortKimpByState(it, viewmodel.kPremiumList.value!!)
            adapter.submitList(sorted){binding.rvKimp.scrollToPosition(0)}
        }
    }

}