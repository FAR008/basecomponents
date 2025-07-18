package com.farooqdev.basecomponents.base
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.farooqdev.basecomponents.base.FragmentGeneral
import kotlinx.coroutines.launch

abstract class BaseNavFragment : FragmentGeneral() {

    open fun navIconBackPressed() {
        onBackPressed()
    }

    open fun onBackPressed() {
        // Override in child if needed
    }

    val singleTop by lazy {
        NavOptions.Builder().setLaunchSingleTop(true).build()
    }

    val popUpTo by lazy {
        NavOptions.Builder()
            .setPopUpTo(findNavController().graph.startDestinationId, true)
            .build()
    }

    protected fun navigateTo(fragmentId: Int, action: Int, args: Bundle? = null, navOptions: NavOptions? = null, extras: FragmentNavigator.Extras? = null) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action, args, navOptions, extras)
            }
        }
    }

    protected fun navigateTo(fragmentId: Int, action: NavDirections) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().navigate(action)
            }
        }
    }

    protected fun navigateUp() {
        launchWhenCreated {
            findNavController().navigateUp()
        }
    }

    protected fun popFrom(fragmentId: Int) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().popBackStack()
            }
        }
    }

    protected fun popFrom(fragmentId: Int, destinationFragmentId: Int, inclusive: Boolean = false) {
        launchWhenCreated {
            if (isAdded && isCurrentDestination(fragmentId)) {
                findNavController().popBackStack(destinationFragmentId, inclusive)
            }
        }
    }

    private fun isCurrentDestination(fragmentId: Int): Boolean {
        return findNavController().currentDestination?.id == fragmentId
    }

    protected fun launchWhenCreated(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withCreated(callback) }
    }

    protected fun launchWhenStarted(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withStarted(callback) }
    }

    protected fun launchWhenResumed(callback: () -> Unit) {
        lifecycleScope.launch { lifecycle.withResumed(callback) }
    }
}
