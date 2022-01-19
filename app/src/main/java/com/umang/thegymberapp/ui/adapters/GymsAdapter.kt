package com.umang.thegymberapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umang.thegymberapp.data.db.GymDb
import com.umang.thegymberapp.databinding.ItemGymBinding
import com.umang.thegymberapp.ui.adapters.GymsAdapter.GymViewHolder

/**
 * Adapter class for GymsFragment
 *
 */
class GymsAdapter :
    ListAdapter<GymDb, GymViewHolder>(
        GymDiffCallback
    ) {


    class GymViewHolder(private val binding: ItemGymBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gymDb: GymDb) {
            binding.apply {
                binding.gym = gymDb
            }
        }

    }

    override fun onBindViewHolder(holder: GymViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymViewHolder {
        val binding = ItemGymBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GymViewHolder(binding)
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




