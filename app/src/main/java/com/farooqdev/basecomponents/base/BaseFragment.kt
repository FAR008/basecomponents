package com.farooqdev.basecomponents.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.core.graphics.Insets
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.farooqdev.basecomponents.ExtensionUtils.safeReturn
import com.farooqdev.basecomponents.ExtensionUtils.windowInsets

abstract class BaseFragment<T : ViewBinding>(
    private val backPressEnabled: Boolean
) : BaseNavFragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    val globalContext: Context by lazy { safeReturn(requireContext()) { binding.root.context } }
    private val globalActivity by lazy { globalContext as Activity }
    val activityContext: Activity get() = globalActivity

    abstract fun layoutResource(inflater: LayoutInflater, container: ViewGroup?): T
    abstract fun onBackPressedCallback()

    open fun onCreateView() {}

    open fun handleInsets(insets: Insets) {
        binding.root.setPadding(insets.left, insets.top, insets.right, insets.bottom)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safeReturn {
            val animation = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
            animation?.duration = 350L
            animation?.interpolator = AccelerateDecelerateInterpolator()
            sharedElementEnterTransition = animation
            sharedElementReturnTransition = animation
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = layoutResource(inflater, container)
        safeReturn { onCreateView() }
        windowInsets?.let { handleInsets(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (backPressEnabled) {
            (activityContext as? OnBackPressedDispatcherOwner)?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner, true
            ) {
                onBackPressedCallback()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
