package com.marvel.presentation.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

open class BaseNavigationFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    protected fun navigateUp() {
        if (parentFragmentManager.backStackEntryCount == 0) {
            requireActivity().finish()
        } else {
            findNavController().navigateUp()
        }
    }

    protected fun navigateUp(destinationId: Int, inclusive: Boolean) {
        if (parentFragmentManager.backStackEntryCount == 0) {
            requireActivity().finish()
        } else {
            findNavController().popBackStack(destinationId, inclusive)
        }
    }

    protected fun navigate(@IdRes actionResId: Int, bundle: Bundle? = null) {
        findNavController().navigate(actionResId, bundle)
    }

    protected fun navigate(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }
}