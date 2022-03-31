package com.marvel.presentation.view.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.marvel.domain.entity.ResultStatus
import com.marvel.presentation.R
import com.marvel.presentation.base.BaseNavigationFragment
import com.marvel.presentation.databinding.FragmentBookMarkBinding
import com.marvel.presentation.view.bookmark.adapter.BookMarkAdapter
import com.marvel.presentation.view.character.CharacterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookMarkFragment : BaseNavigationFragment(R.layout.fragment_book_mark) {
    lateinit var fragmentBookMarkBinding: FragmentBookMarkBinding
    private val viewModel: CharacterViewModel by activityViewModels()

    @Inject
    lateinit var bookMarkAdapter: BookMarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBookMarkBinding =
            FragmentBookMarkBinding.inflate(inflater, container, false).apply {
                vm = viewModel
                lifecycleOwner = this@BookMarkFragment
            }

        return fragmentBookMarkBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentBookMarkBinding.characterRecyclerView.run {
            adapter = bookMarkAdapter.apply {
                onClickBookMark = {
                    lifecycleScope.launch {
                        viewModel.deleteBookMark(it)
                        getBookMark()
                    }
                }
            }
        }

        getBookMark()
    }

    private fun getBookMark() {
        lifecycleScope.launch {
            viewModel.getCharacterLocal().collect {
                when (it) {
                    is ResultStatus.Success -> {
                        fragmentBookMarkBinding.bookMarkProgressBar.visibility = View.GONE
                        bookMarkAdapter.submitList(it.data)
                    }

                    is ResultStatus.Loading -> {
                        fragmentBookMarkBinding.bookMarkProgressBar.visibility = View.VISIBLE
                    }

                    is ResultStatus.Error -> {
                        fragmentBookMarkBinding.bookMarkProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "${it.throwable?.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}