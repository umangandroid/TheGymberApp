package com.umang.thegymberapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umang.thegymberapp.data.db.GymDb
import com.umang.thegymberapp.databinding.ItemGymMatchBinding

/**
 * Adapter class for GymsMatchesListFragment
 *
 */
class GymsMatchesListAdapter :
    ListAdapter<GymDb, GymsMatchesListAdapter.GymMatchViewHolder>(
        GymDiffCallback
    ) {


    class GymMatchViewHolder(private val binding: ItemGymMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gymDb: GymDb) {
            binding.apply {
                binding.gym = gymDb
            }
        }

    }

    override fun onBindViewHolder(holder: GymMatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymMatchViewHolder {
        val binding =
            ItemGymMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GymMatchViewHolder(binding)
    }

    object GymDiffCallback : DiffUtil.ItemCallback<GymDb>() {
        override fun areItemsTheSame(oldItem: GymDb, newItem: GymDb): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: GymDb, newItem: GymDb): Boolean {
            return oldItem == newItem
        }

    }
}




