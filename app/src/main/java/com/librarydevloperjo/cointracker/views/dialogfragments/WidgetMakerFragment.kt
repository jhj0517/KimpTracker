package com.librarydevloperjo.cointracker.views.dialogfragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.databinding.FragmentWidgetMakerBinding
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.util.WIDGET_COIN_KEY
import com.librarydevloperjo.cointracker.viewmodels.CoinsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


private const val ARG_KEY_KPREMIUM = "kpremium"

@AndroidEntryPoint
class WidgetMakerFragment : DialogFragment() {

    private var _binding:FragmentWidgetMakerBinding? = null
    private val binding get() = _binding!!

    private var coin: KPremiumData? = null
    private val viewmodel:CoinsViewModel by activityViewModels()
    @Inject
    lateinit var preference: PreferenceManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            coin = it.getParcelable(ARG_KEY_KPREMIUM)
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            _binding =  FragmentWidgetMakerBinding.inflate(LayoutInflater.from(context))

            binding.apply {
                coin?.let { coin->
                    tvCoinname.text = coin.textview_name
                    tvUpbitprice.text = nFormat.format(coin.upbitPrice)
                    tvBinanceprice.text = nFormat.format(coin.binancePrice)
                    tvKimprate.text = nFormat.format(coin.kPremium)

                    if(coin.kPremium > 0){
                        tvUpbitprice.setTextColor(Color.parseColor("#E8B53333"))
                        tvKimprate.setTextColor(Color.parseColor("#E8B53333"))
                    }else if(coin.kPremium<0){
                        tvUpbitprice.setTextColor(Color.parseColor("#416DD8"))
                        tvKimprate.setTextColor(Color.parseColor("#416DD8"))
                    }

                    val data = viewmodel.searchKData(coin.coinName)
                    if(data.isNullOrEmpty()){
                        ivStar.setImageResource(R.drawable.ratingstar_empty)
                    }else{
                        ivStar.setImageResource(R.drawable.ratingstar_filled)
                    }
                }

                btnWidget.setOnClickListener {
                    preference.setString(WIDGET_COIN_KEY,coin!!.coinName)
                    Toast.makeText(requireActivity(),
                        resources.getString(R.string.widgetisadded,coin!!.textview_name),
                        Toast.LENGTH_LONG).show()
                }

                rootStar.setOnClickListener {
                    coin?.let {
                        val data = viewmodel.searchKData(it.coinName)
                        if(data.isEmpty()){
                            viewmodel.insertKData(it)
                            ivStar.setImageResource(R.drawable.ratingstar_filled)
                        }else{
                            viewmodel.deleteKData(it.coinName)
                            ivStar.setImageResource(R.drawable.ratingstar_empty)
                        }
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

    companion object {
        @JvmStatic
        fun newInstance(param1: Parcelable) =
            WidgetMakerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_KEY_KPREMIUM, param1)
                }
            }

        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}