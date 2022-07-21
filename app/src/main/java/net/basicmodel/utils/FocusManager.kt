package net.basicmodel.utils

import android.view.ViewGroup
import net.basicmodel.widget.EditViewContainer
import net.basicmodel.widget.InputEditText


class FocusManager {
    companion object {
        private var i: FocusManager? = null
            get() {
                field ?: run {
                    field = FocusManager()
                }
                return field
            }

        @Synchronized
        fun get(): FocusManager {
            return i!!
        }
    }

    fun clearFocus(parent: ViewGroup) {
        for (index in 0 until parent.childCount) {
            val child1 = parent.getChildAt(index)
            if (child1 is EditViewContainer) {
                child1.getInputView().getEditTextView().clearFocus()
            }
            if (child1 is InputEditText) {
                child1.getEditTextView().clearFocus()
            }
        }
    }
}