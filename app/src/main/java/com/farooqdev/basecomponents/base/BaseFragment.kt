package com.farooqdev.basecomponents.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>(
    private val backPressEnabled: Boolean = false
) : Fragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    protected val safeContext: Context
        get() = _binding?.root?.context ?: requireContext()

    /**
     * Abstract method to inflate binding
     */
    protected abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    /**
     * Optional callback for back press handling
     */
    open fun onBackPressed() {}

    /**
     * Optional setup after view creation
     */
    open fun setupView() {}

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupBackPressHandler()
    }

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupBackPressHandler() {
        if (backPressEnabled) {
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        onBackPressed()
                    }
                }
            )
        }
    }
}