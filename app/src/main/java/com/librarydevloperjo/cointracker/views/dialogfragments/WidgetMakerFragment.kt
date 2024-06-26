package com.librarydevloperjo.cointracker.views.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.coinwidget.WidgetUpdateService
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.databinding.FragmentWidgetMakerBinding
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.util.WIDGET_COIN_KEY
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import com.librarydevloperjo.cointracker.viewmodels.DialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val ARG_KEY_KPREMIUM = "kpremium"

@AndroidEntryPoint
class WidgetMakerFragment : DialogFragment() {

    private var _binding:FragmentWidgetMakerBinding? = null
    private val binding get() = _binding!!

    private var coin: KPremiumData? = null
    private val coinsViewModel:CoinsViewModel by activityViewModels()
    private val viewmodel:DialogViewModel by viewModels()
    @Inject
    lateinit var preference: PreferenceManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            coin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_KEY_KPREMIUM, KPremiumData::class.java)
            } else {
                it.getParcelable(ARG_KEY_KPREMIUM)
            }
            viewmodel.setCoinData(coin!!)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            _binding =  FragmentWidgetMakerBinding.inflate(layoutInflater)

            binding.apply {
                viewModel = viewmodel
                isStar = coinsViewModel.queryBookMarks(coin?.ticker?:"").isNotEmpty()

                btnWidget.setOnClickListener {
                    startService()
                    Toast.makeText(requireActivity(),
                        resources.getString(R.string.widgetisadded,coin!!.ticker),
                        Toast.LENGTH_LONG).show()
                }

                rootStar.setOnClickListener {
                    val data = coinsViewModel.queryBookMarks(coin?.ticker?:"")
                    if(data.isEmpty()){
                        coinsViewModel.insertBookMark(coin!!)
                        isStar = true
                    }else{
                        coinsViewModel.deleteBookMark(coin!!.ticker)
                        isStar = false
                    }
                }

                ivBack.setOnClickListener {
                    dismiss()
                }

            }
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startService(){
        preference.setString(WIDGET_COIN_KEY,coin!!.ticker)

        Intent(context, WidgetUpdateService::class.java).also { intent ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // `startForegroundService` is possible only on API 26+
                requireActivity().startForegroundService(intent)
            } else {
                // In this case, the service will stop when the app is in idle state because it's not a ForegroundService.
                requireActivity().startService(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Parcelable) =
            WidgetMakerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_KEY_KPREMIUM, param1)
                }
            }
    }
}