package com.marvel.presentation.view.character.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marvel.domain.entity.Result
import com.marvel.presentation.databinding.ItemCharacterBinding
import javax.inject.Inject

class CharacterAdapter @Inject constructor() :
    ListAdapter<Result, CharacterAdapter.ViewHolder>(ListDiffCallback) {
    var onClickDetail: ((Int) -> Unit)? = null
    var onClickBookMark: ((Result) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCharacterBinding.inflate(
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
    inner class ViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.apply {
                this.result = result

                root.setOnClickListener {
                    onClickDetail?.invoke(result.id)
                }

                itemCharacterBookMark.setOnClickListener {
                    this.result = result.also {
                        it.isBookMark = it.isBookMark.not()
                    }

                    executePendingBindings()
                    onClickBookMark?.invoke(result)
                }

                executePendingBindings()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    object ListDiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }
    }
}
