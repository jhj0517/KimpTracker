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
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.UpbitAdapterViewModel
import java.text.NumberFormat
import java.util.*


class UpbitAdapter(
    private val preferenceManager: PreferenceManager
):
ListAdapter<UpbitCoin, UpbitAdapter.ViewHolder>(diffUtil)
,Filterable {

    private var list = mutableListOf<UpbitCoin>()

    inner class ViewHolder(val binding: CellUpbitBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(items: UpbitCoin){
            with(binding){
                viewModel = UpbitAdapterViewModel(items, preferenceManager)
                executePendingBindings()
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
                    if(item.ticker!!.contains(filterPattern)){
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
                oldItem.ticker == newItem.ticker

            override fun areItemsTheSame(oldItem: UpbitCoin, newItem: UpbitCoin) =
                oldItem.ticker == newItem.ticker
        }
    }

}