package com.waiyanphyo.astrofriday.ui.sport

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waiyanphyo.astrofriday.R
import com.waiyanphyo.astrofriday.databinding.ItemSportBinding
import com.waiyanphyo.astrofriday.domain.model.Sport

class SportAdapter: ListAdapter<Sport, SportAdapter.SportViewHolder>(SportDiffUtil) {

    companion object {
        object SportDiffUtil : DiffUtil.ItemCallback<Sport>() {
            override fun areItemsTheSame(oldItem: Sport, newItem: Sport): Boolean {
                return oldItem.match == newItem.match

            }

            override fun areContentsTheSame(oldItem: Sport, newItem: Sport): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class SportViewHolder(private val binding: ItemSportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sport: Sport) {
            binding.apply {
                tvMatch.text = sport.match
                tvTournament.text = sport.tournament
                tvStadiumCountry.text = itemView.context.getString(
                    R.string.stadium_country,
                    sport.stadium,
                    sport.country
                )
                tvStartTime.text = sport.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportViewHolder {
        val binding = ItemSportBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
