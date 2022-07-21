package net.basicmodel.widget

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_dialog_custom_item.*
import net.basicmodel.R
import net.basicmodel.utils.OnOptionClickListener

class CustomDialog(context: Context) : BaseDialog<CustomDialog>(context) {

    var listener: OnOptionClickListener? = null

    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_custom_item, null)
    }

    override fun setUiBeforShow() {
        fieldName.setHint("字段名")
        fieldContent.setHint("内容")
        cancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            val name = fieldName.getEditTextContent()
            val content = fieldContent.getEditTextContent()
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(content)) {
                dismiss()
            } else {
                listener!!.onClick("customOk", name, content)
                dismiss()
            }
        }
    }
}