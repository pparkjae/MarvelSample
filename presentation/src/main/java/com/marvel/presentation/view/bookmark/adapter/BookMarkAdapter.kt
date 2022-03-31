package com.marvel.presentation.view.bookmark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marvel.domain.entity.MarvelCharacterLocal
import com.marvel.presentation.databinding.ItemBookmarkBinding
import javax.inject.Inject

class BookMarkAdapter @Inject constructor() :
    ListAdapter<MarvelCharacterLocal, BookMarkAdapter.ViewHolder>(ListDiffCallback) {
    var onClickBookMark: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookmarkBinding.inflate(
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
    inner class ViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(marvelCharacterLocal: MarvelCharacterLocal) {
            binding.apply {
                this.marvelCharacterLocal = marvelCharacterLocal

                binding.itemBookMarkDelete.setOnClickListener {
                    onClickBookMark?.invoke(marvelCharacterLocal.id)
                }

                executePendingBindings()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    object ListDiffCallback : DiffUtil.ItemCallback<MarvelCharacterLocal>() {
        override fun areItemsTheSame(oldItem: MarvelCharacterLocal, newItem: MarvelCharacterLocal): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MarvelCharacterLocal, newItem: MarvelCharacterLocal): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
