package com.farooqdev.basecomponents.base

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class FragmentGeneral : Fragment() {

    // Coroutine scope tied to fragment lifecycle
    protected val fragmentScope = CoroutineScope(Dispatchers.Main.immediate)

    /* ------------------------- */
    /*        Delayed Tasks      */
    /* ------------------------- */

    protected fun withDelay(delay: Long = 1000, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(block, delay)
    }

    /* ------------------------- */
    /*         Toast             */
    /* ------------------------- */

    protected fun showToast(message: String) {
        fragmentScope.launch {
            context?.let {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected fun showToast(@StringRes stringId: Int) {
        fragmentScope.launch {
            context?.let {
                Toast.makeText(it, stringId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /* ------------------------- */
    /*        Keyboard           */
    /* ------------------------- */

    protected fun showKeyboard(view: View) {
        fragmentScope.launch {
            val imm = context?.getSystemService(InputMethodManager::class.java)
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    protected fun hideKeyboard() {
        fragmentScope.launch {
            activity?.currentFocus?.let { view ->
                val imm = context?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    protected fun hideKeyboard(view: View) {
        fragmentScope.launch {
            val imm = context?.getSystemService(InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}