package net.basicmodel.utils

import android.content.Context
import net.basicmodel.widget.LoadingDialog

class LoadingDialogManager {
    companion object {
        private var i: LoadingDialogManager? = null
            get() {
                field ?: run {
                    field = LoadingDialogManager()
                }
                return field
            }

        @Synchronized
        fun get(): LoadingDialogManager {
            return i!!
        }
    }

    var dialog: LoadingDialog? = null

    fun show(context: Context) {
        dialog = LoadingDialog(context)
        dialog!!.show()
    }

    fun close() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }
}