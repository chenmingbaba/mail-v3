package net.basicmodel.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils

object ClipUtils {
    fun copy(context: Context, content: String) {
        if (TextUtils.isEmpty(content))
            return
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.setPrimaryClip(ClipData.newPlainText(null, content))
    }
}