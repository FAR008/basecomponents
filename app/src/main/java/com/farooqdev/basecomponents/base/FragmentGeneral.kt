package com.farooqdev.basecomponents.base
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.farooqdev.basecomponents.ExtensionUtils.recordException
import com.farooqdev.basecomponents.ExtensionUtils.safeRun

open class FragmentGeneral : Fragment() {

    protected val baseTAG = "BaseTAG"

    protected fun withDelay(delay: Long = 1000, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(block, delay)
    }

    protected fun getResString(stringId: Int): String {
        return context?.getString(stringId).orEmpty()
    }

    protected fun showToast(message: String) {
        activity?.runOnUiThread {
            try {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
                Log.e("TOAST", message)
            } catch (ex: Exception) {
                ex.recordException()
            }
        }
    }


    protected fun showToast(stringId: Int) {
        showToast(getResString(stringId))
    }


    protected fun showKeyboard(v: View) {
        safeRun {
            val imm = context?.getSystemService(InputMethodManager::class.java)
            imm?.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    protected fun hideKeyboard() {
        safeRun {
            activity?.currentFocus?.windowToken?.let { token ->
                val imm = activity?.getSystemService(InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(token, 0)
            }
        }
    }

    protected fun hideKeyboard(view: View) {
        safeRun {
            val imm = context?.getSystemService(InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    val dismissKeyboardOnTouchListener = View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            v.requestFocus()
            hideKeyboard(v)
        }
        false
    }
}
