package com.librarydevloperjo.cointracker.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin
import com.librarydevloperjo.cointracker.databinding.CellUpbitBinding
import java.text.NumberFormat
import java.util.*


class UpbitAdapter:
    ListAdapter<UpbitCoin, UpbitAdapter.ViewHolder>(diffUtil),Filterable {

    private var list = mutableListOf<UpbitCoin>()

    inner class ViewHolder(val binding: CellUpbitBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(items: UpbitCoin){
            with(binding){
                tvTicker.text = items.textview_name
                tvUpbitprice.text = nFormat.format(items.trade_price)
                var direction = ""
                if(items.change_rate>0){
                    tvChangerate.setTextColor(Color.parseColor("#E8B53333"))
                    tvUpbitprice.setTextColor(Color.parseColor("#E8B53333"))
                    direction = "+"
                }else if (items.change_rate<0){
                    tvChangerate.setTextColor(Color.parseColor("#416DD8"))
                    tvUpbitprice.setTextColor(Color.parseColor("#416DD8"))
                    direction = "-"
                }
                tvChangerate.text = direction + nFormat.format(items.change_rate) + " %"
            }
        }

        init{
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = CellUpbitBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    override fun getFilter(): Filter {
        return SearchFilter
    }

    fun setData(list: MutableList<UpbitCoin>?){
        this.list = list!!
        submitList(list)
    }

    private val SearchFilter : Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {

            val filteredList: ArrayList<UpbitCoin> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(list)
            } else {
                val filterPattern = constraint.toString().lowercase().trim { it <= ' '}

                for (item in list) {
                    if(item.coin!!.contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            submitList(results.values as MutableList<UpbitCoin>)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UpbitCoin>() {
            override fun areContentsTheSame(oldItem: UpbitCoin, newItem: UpbitCoin) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: UpbitCoin, newItem: UpbitCoin) =
                oldItem.coin == newItem.coin
        }

        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }

    init {
        nFormat.maximumFractionDigits = 3
    }

}