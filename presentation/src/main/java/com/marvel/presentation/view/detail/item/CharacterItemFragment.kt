package com.marvel.presentation.view.detail.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.marvel.presentation.R
import com.marvel.presentation.base.BaseNavigationFragment
import com.marvel.presentation.databinding.FragmentCharacterItemBinding
import com.marvel.presentation.view.detail.CharacterDetailViewModel
import com.marvel.presentation.view.detail.ItemType
import com.marvel.presentation.view.detail.item.adapter.CharacterItemAdapter
import com.marvel.presentation.view.detail.item.adapter.CharacterStoryItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterItemFragment : BaseNavigationFragment(R.layout.fragment_character_item) {
    lateinit var fragmentCharacterItemBinding: FragmentCharacterItemBinding
    private val viewModel: CharacterDetailViewModel by activityViewModels()
    private val args: CharacterItemFragmentArgs by navArgs()

    private val characterItemAdapter: CharacterItemAdapter by lazy {
        CharacterItemAdapter()
    }

    private val characterStoryItemAdapter: CharacterStoryItemAdapter by lazy {
        CharacterStoryItemAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCharacterItemBinding =
            FragmentCharacterItemBinding.inflate(inflater, container, false).apply {
                vm = viewModel
                itemType = args.itemType
                lifecycleOwner = this@CharacterItemFragment
            }

        return fragmentCharacterItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentCharacterItemBinding.characterItemRecyclerView.adapter = when(args.itemType) {
            ItemType.STORIES -> {
                characterStoryItemAdapter
            }

            else -> {
                characterItemAdapter
            }
        }
    }
}