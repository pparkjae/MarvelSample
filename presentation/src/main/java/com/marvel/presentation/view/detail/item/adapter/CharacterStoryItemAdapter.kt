package com.marvel.presentation.view.detail.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marvel.domain.entity.StoryItem
import com.marvel.presentation.databinding.ItemCharacterStoryItemBinding

class CharacterStoryItemAdapter :
    ListAdapter<StoryItem, CharacterStoryItemAdapter.ViewHolder>(ListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCharacterStoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    /**************************************************************************
     * view holder
     **************************************************************************/
    inner class ViewHolder(private val binding: ItemCharacterStoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryItem) {
            binding.apply {
                this.item = item

                executePendingBindings()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    object ListDiffCallback : DiffUtil.ItemCallback<StoryItem>() {
        override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
