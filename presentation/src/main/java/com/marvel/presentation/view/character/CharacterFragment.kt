package com.marvel.presentation.view.character

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.marvel.domain.entity.ResultStatus
import com.marvel.presentation.R
import com.marvel.presentation.base.BaseNavigationFragment
import com.marvel.presentation.databinding.FragmentCharacterBinding
import com.marvel.presentation.view.character.adapter.CharacterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharacterFragment : BaseNavigationFragment(R.layout.fragment_character) {
    lateinit var mainFragmentBinding: FragmentCharacterBinding
    private val viewModel: CharacterViewModel by activityViewModels()

    @Inject
    lateinit var characterAdapter: CharacterAdapter

    private var offset = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainFragmentBinding = FragmentCharacterBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = this@CharacterFragment

            characterRecyclerView.run {
                adapter = characterAdapter.apply {
                    onClickDetail = {
                        navigate(R.id.actionDetailFragment, bundleOf("id" to it))
                    }

                    onClickBookMark = {
                        lifecycleScope.launch {
                            if (it.isBookMark) {
                                viewModel.addBookMark(it)
                            } else {
                                viewModel.deleteBookMark(it.id)
                            }
                        }
                    }
                }

                setOnEndOfScrollListener {
                    lifecycleScope.launch {
                        viewModel.requestCharacters(offset = offset++)
                    }
                }
            }
        }

        setHasOptionsMenu(true)

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            showExitDialog(it)
        }

        return mainFragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.getCharacterLocal().collect {
                when (it) {
                    is ResultStatus.Success -> {
                        lifecycleScope.launch {
                            viewModel.requestCharacters(offset = offset++)
                        }
                    }

                    is ResultStatus.Loading -> {
                        mainFragmentBinding.characterProgressBar.visibility = View.VISIBLE
                    }

                    is ResultStatus.Error -> {
                        showExitDialog(it.throwable?.message!!)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showExitDialog(errorCause: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.info))
            .setMessage(getString(R.string.exit_desc) + "\n" + errorCause)
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, id ->
                requireActivity().finishAffinity()
            }

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuBookMark -> {
                navigate(R.id.actionBookMarkFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}