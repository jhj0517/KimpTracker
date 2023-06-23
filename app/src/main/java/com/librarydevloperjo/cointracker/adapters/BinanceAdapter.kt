package com.librarydevloperjo.cointracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.databinding.CellBinanceBinding
import java.text.NumberFormat
import java.util.*

class BinanceAdapter:
    ListAdapter<BinanceCoin, BinanceAdapter.ViewHolder>(diffUtil),Filterable {

    private var list = mutableListOf<BinanceCoin>()

    inner class ViewHolder(val binding: CellBinanceBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(items: BinanceCoin){
            with(binding) {
                tvTicker.text = items.coin
                val numberWithComma = nFormat.format(items.price)
                if(numberWithComma.equals("0")) nFormat.maximumFractionDigits = 9 else nFormat.maximumFractionDigits = 3
                tvPrice.text = numberWithComma
            }
        }

        init{
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = CellBinanceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getFilter(): Filter {
        return SearchFilter
    }

    fun setData(list: MutableList<BinanceCoin>?){
        this.list = list!!
        submitList(list)
    }

    private val SearchFilter : Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {

            val filteredList: ArrayList<BinanceCoin> = ArrayList()
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
            submitList(results.values as MutableList<BinanceCoin>)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BinanceCoin>() {
            override fun areContentsTheSame(oldItem: BinanceCoin, newItem: BinanceCoin) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: BinanceCoin, newItem: BinanceCoin) =
                oldItem.coin == newItem.coin
        }

        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }

    init {
        nFormat.maximumFractionDigits = 3
    }
}