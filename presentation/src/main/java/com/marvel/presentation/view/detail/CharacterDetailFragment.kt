package com.marvel.presentation.view.detail

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.marvel.presentation.R
import com.marvel.presentation.base.BaseNavigationFragment
import com.marvel.presentation.databinding.FragmentCharacterDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment : BaseNavigationFragment(R.layout.fragment_character_detail) {
    lateinit var fragmentCharacterDetailBinding: FragmentCharacterDetailBinding
    private val viewModel: CharacterDetailViewModel by activityViewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCharacterDetailBinding =
            FragmentCharacterDetailBinding.inflate(inflater, container, false).apply {
                vm = viewModel
                lifecycleOwner = this@CharacterDetailFragment

                characterDetailThumbnail.setOnClickListener {
                    checkPermission()
                }

                characterDetailUrls.setOnClickListener {
                    navigate(R.id.actionCharacterDetailUrl)
                }

                characterDetailComics.setOnClickListener {
                    navigate(
                        R.id.actionCharacterDetailItem,
                        bundleOf("itemType" to ItemType.COMICS)
                    )
                }

                characterDetailStories.setOnClickListener {
                    navigate(
                        R.id.actionCharacterDetailItem,
                        bundleOf("itemType" to ItemType.STORIES)
                    )
                }

                characterDetailEvents.setOnClickListener {
                    navigate(
                        R.id.actionCharacterDetailItem,
                        bundleOf("itemType" to ItemType.EVENTS)
                    )
                }

                characterDetailSeries.setOnClickListener {
                    navigate(
                        R.id.actionCharacterDetailItem,
                        bundleOf("itemType" to ItemType.SERIES)
                    )
                }
            }

        return fragmentCharacterDetailBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.requestCharactersId(args.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                .show()

            navigateUp()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsResultCallback.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            viewModel.getImagePath()?.run {
                download(this)
            }
        }
    }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                viewModel.getImagePath()?.run {
                    download(this)
                }
            }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun download(url: String) {
        Toast.makeText(requireContext(), getText(R.string.download_start), Toast.LENGTH_SHORT).show()

        val manager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request =
            DownloadManager.Request(Uri.parse(url))
                .apply {
                    setTitle(getString(R.string.download_start))
                    setMimeType("jpg")
                    setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "thumbnail.jpg"
                    )
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setRequiresCharging(false)
                    setAllowedOverMetered(true)
                    setAllowedOverRoaming(true)
                }

        manager.enqueue(request)
    }
}