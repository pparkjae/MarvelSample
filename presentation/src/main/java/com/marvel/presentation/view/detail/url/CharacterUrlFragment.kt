package com.marvel.presentation.view.detail.url

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.activityViewModels
import com.marvel.presentation.R
import com.marvel.presentation.base.BaseNavigationFragment
import com.marvel.presentation.databinding.FragmentCharacterUrlBinding
import com.marvel.presentation.view.detail.CharacterDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterUrlFragment : BaseNavigationFragment(R.layout.fragment_character_url) {
    lateinit var fragmentCharacterUrlBinding: FragmentCharacterUrlBinding
    private val viewModel: CharacterDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCharacterUrlBinding =
            FragmentCharacterUrlBinding.inflate(inflater, container, false).apply {
                vm = viewModel
                characterWebView.apply {
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()

                    settings.also {
                        it.loadWithOverviewMode = true
                        it.useWideViewPort = true
                    }

                    setOnKeyListener { _, keyCode, event ->
                        if (event.action != KeyEvent.ACTION_DOWN) return@setOnKeyListener true

                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (characterWebView.canGoBack()) {
                                characterWebView.goBack()
                            } else {
                                requireActivity().onBackPressed()
                            }
                            return@setOnKeyListener true
                        }

                        return@setOnKeyListener false
                    }
                }
                lifecycleOwner = this@CharacterUrlFragment
            }

        return fragmentCharacterUrlBinding.root
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            fragmentCharacterUrlBinding.characterUrlProgressBar.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            fragmentCharacterUrlBinding.characterUrlProgressBar.visibility = View.GONE
        }
    }
}