package com.farooqdev.basecomponents.base

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

abstract class BaseNavFragment : androidx.fragment.app.Fragment() {

    // Navigation options
    protected val singleTop: NavOptions by lazy {
        NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()
    }

    protected val popUpToRoot: NavOptions by lazy {
        NavOptions.Builder()
            .setPopUpTo(findNavController().graph.startDestinationId, true)
            .build()
    }

    // Navigation methods
    @MainThread
    protected fun navigateTo(action: NavDirections) = safeNavigation {
        findNavController().navigate(action)
    }

    @MainThread
    protected fun navigateTo(
        actionId: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: FragmentNavigator.Extras? = null
    ) = safeNavigation {
        findNavController().navigate(actionId, args, navOptions, extras)
    }

    @MainThread
    protected fun navigateUp() = safeNavigation {
        findNavController().navigateUp()
    }

    @MainThread
    protected fun popBackStack() = safeNavigation {
        findNavController().popBackStack()
    }

    @MainThread
    protected fun popBackStack(destinationId: Int, inclusive: Boolean) = safeNavigation {
        findNavController().popBackStack(destinationId, inclusive)
    }

    // Back press handling
    open fun onBackPressed() {
        popBackStack()
    }

    // Safe navigation wrapper
    private fun safeNavigation(block: () -> Unit) {
        if (!isAdded || isDetached) return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (isResumed) {
                    block()
                } else {
                    // Wait for fragment to be resumed if not already
                    repeatOnLifecycle(Lifecycle.State.RESUMED) {
                        block()
                    }
                }
            } catch (e: Exception) {
                // Handle navigation errors
            }
        }
    }

    // Modern lifecycle-aware coroutine launchers
    protected fun launchWhenCreated(block: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                block()
            }
        }
    }

    protected fun launchWhenStarted(block: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                block()
            }
        }
    }

    protected fun launchWhenResumed(block: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                block()
            }
        }
    }
}