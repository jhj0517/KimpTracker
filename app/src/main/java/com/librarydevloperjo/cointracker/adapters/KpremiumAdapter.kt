package com.librarydevloperjo.cointracker.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.databinding.CellKimpBinding
import java.text.NumberFormat
import java.util.*


class KpremiumAdapter (private val clickcallback:ClickCallback):
    ListAdapter<KPremiumData,KpremiumAdapter.ViewHolder>(diffUtil),Filterable {

    private var list = mutableListOf<KPremiumData>()

    inner class ViewHolder(val binding:CellKimpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(items: KPremiumData){
            with(binding) {
                if(items.kPremium < 0){
                    tvKp.setTextColor(Color.parseColor("#416DD8")) // R.color.red
                    tvUpbitprice.setTextColor(Color.parseColor("#416DD8"))
                }else if(items.kPremium>0){
                    tvKp.setTextColor(Color.parseColor("#E8B53333"))
                    tvUpbitprice.setTextColor(Color.parseColor("#E8B53333"))
                }
                if(items.isBookmark!!) ivStar.setImageDrawable(ivStar.context.getDrawable(R.drawable.ratingstar_filled)) else ivStar.setImageDrawable(ivStar.context.getDrawable(R.drawable.ratingstar_empty))

                tvTicker.text = items.textview_name
                tvUpbitprice.text = nFormat.format(items.upbitPrice)
                tvBinanceprice.text = nFormat.format(items.binancePrice)
                tvKp.text = nFormat.format(items.kPremium) + " %"
            }
        }

        init{
            binding.ltRoot.setOnClickListener {
                val data = currentList.get(adapterPosition)
                clickcallback.onItemClicked(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = CellKimpBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getFilter(): Filter {
        return SearchFilter
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<KPremiumData>() {
            override fun areContentsTheSame(oldItem: KPremiumData, newItem: KPremiumData) =
                oldItem.coinName == newItem.coinName

            override fun areItemsTheSame(oldItem: KPremiumData, newItem: KPremiumData) =
                oldItem.coinName == newItem.coinName
        }

        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }

    fun setData(list: MutableList<KPremiumData>?){
        this.list = list!!
        submitList(list)
    }

    private val SearchFilter : Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {

            val filteredList: ArrayList<KPremiumData> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(list)
            } else {
                val filterPattern = constraint.toString().lowercase().trim { it <= ' '}

                for (item in list) {
                    if(item.coinName!!.contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            submitList(results.values as MutableList<KPremiumData>)
        }
    }

    init {
        nFormat.maximumFractionDigits = 3
    }

    interface ClickCallback{
        fun onItemClicked(items: KPremiumData)
    }
}