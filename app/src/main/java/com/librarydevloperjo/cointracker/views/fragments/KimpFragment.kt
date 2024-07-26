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
import com.librarydevloperjo.cointracker.adapters.KpremiumAdapter
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import com.librarydevloperjo.cointracker.databinding.FragmentKimpBinding
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.KPremiumSortCriteria
import com.librarydevloperjo.cointracker.viewmodels.KPremiumViewModel
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import com.librarydevloperjo.cointracker.views.dialogfragments.WidgetMakerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class KimpFragment : Fragment(),KpremiumAdapter.ClickCallback {

    private var _binding: FragmentKimpBinding? =null
    private val binding get() = _binding!!
    private val viewModel: KPremiumViewModel by activityViewModels()
    private val uiViewModel: UIViewModel by activityViewModels()

    private val scrollJob = Job()
    private val scrollScope = CoroutineScope(Dispatchers.Main + scrollJob)

    @Inject
    lateinit var preference: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentKimpBinding.inflate(inflater, container, false)

        val adapter = KpremiumAdapter(this,preference)
        binding.apply {
            rvKimp.layoutManager = LinearLayoutManager(requireActivity())
            rvKimp.adapter = adapter

            rootTabprice.setOnClickListener {
                viewModel.toggleSortState(KPremiumSortCriteria.KIMP)
            }

            rootTabkp.setOnClickListener {
                viewModel.toggleSortState(KPremiumSortCriteria.PRICE)
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

    override fun onItemClicked(items: KPremiumEntity) {
        val dialog = WidgetMakerFragment.newInstance(items)
        dialog.show(requireActivity().supportFragmentManager,"dialog")
    }

    private fun subscribeUI(adapter:KpremiumAdapter){
        viewModel.kPremiumList.observe(viewLifecycleOwner){
            binding.isLoaded = !it.isNullOrEmpty()

            if (it.isNotEmpty()){
                binding.count = "(${it.size})"
                binding.exc = it.first().exchangeRate.toString()
                adapter.submitList(it)
            }
        }

        viewModel.sortState.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                binding.rvKimp.scrollToPosition(0)
            }
        }
    }

}