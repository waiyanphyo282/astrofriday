package com.waiyanphyo.astrofriday.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waiyanphyo.astrofriday.databinding.ItemLocationBinding
import com.waiyanphyo.astrofriday.domain.model.SearchLocation

class SearchAdapter: ListAdapter<SearchLocation, SearchAdapter.SearchViewHolder>(DIFF) {

    companion object {
        val DIFF = object: DiffUtil.ItemCallback<SearchLocation>() {
            override fun areItemsTheSame(oldItem: SearchLocation, newItem: SearchLocation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SearchLocation, newItem: SearchLocation): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class SearchViewHolder(val binding: ItemLocationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(location: SearchLocation) {
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