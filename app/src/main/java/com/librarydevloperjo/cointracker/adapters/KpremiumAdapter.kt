package com.librarydevloperjo.cointracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import com.librarydevloperjo.cointracker.databinding.CellKimpBinding
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.KPremiumAdapterViewModel
import kotlin.collections.ArrayList


class KpremiumAdapter(
    private val clickcallback:ClickCallback,
    private val pref:PreferenceManager
):ListAdapter<KPremiumEntity,KpremiumAdapter.ViewHolder>(diffUtil),
Filterable {

    private var list = mutableListOf<KPremiumEntity>()

    inner class ViewHolder(val binding:CellKimpBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(items: KPremiumEntity){
            with(binding) {
                viewModel = KPremiumAdapterViewModel(items, pref)
                executePendingBindings()
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
        val diffUtil = object : DiffUtil.ItemCallback<KPremiumEntity>() {
            override fun areContentsTheSame(oldItem: KPremiumEntity, newItem: KPremiumEntity) =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: KPremiumEntity, newItem: KPremiumEntity) =
                oldItem == newItem
        }
    }

    fun setData(list: MutableList<KPremiumEntity>?){
        this.list = list!!
        submitList(list)
    }

    private val SearchFilter : Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {

            val filteredList: ArrayList<KPremiumEntity> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(list)
            } else {
                val filterPattern = constraint.toString().lowercase().trim { it <= ' '}

                for (item in list) {
                    if(item.ticker.contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            submitList(results.values as ArrayList<KPremiumEntity>)
        }
    }
    interface ClickCallback{
        fun onItemClicked(items: KPremiumEntity)
    }
}