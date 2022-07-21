package net.basicmodel.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class KeyboardManager {
    companion object {
        private var i: KeyboardManager? = null
            get() {
                field ?: run {
                    field = KeyboardManager()
                }
                return field
            }

        @Synchronized
        fun get(): KeyboardManager {
            return i!!
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive && activity.currentFocus != null) {
            if (activity.currentFocus!!.windowToken != null) {
                imm.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}