package com.waiyanphyo.astrofriday.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waiyanphyo.astrofriday.databinding.ItemLocationBinding
import com.waiyanphyo.astrofriday.domain.model.Location

class SearchAdapter: ListAdapter<Location, SearchAdapter.SearchViewHolder>(DIFF) {

    companion object {
        val DIFF = object: DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class SearchViewHolder(val binding: ItemLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(location: Location) {
            binding.tvName.text = location.fullName
            binding.tvLocationData.text = location.formattedLocation()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}