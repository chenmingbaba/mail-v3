package net.basicmodel.widget

import android.content.Context
import android.text.InputType
import android.text.TextUtils
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_dialog_net.*
import net.basicmodel.R
import net.basicmodel.utils.OnDialogClickListener

class NetDetailsDialog(context: Context) : BaseDialog<NetDetailsDialog>(context) {

    var listenter: OnDialogClickListener? = null

    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_net, null)
    }

    override fun setUiBeforShow() {
        boardNum.setHint("单板个数")
        boardNum.view!!.inputType = InputType.TYPE_CLASS_NUMBER
        portNum.setHint("端口个数")
        portNum.setEditTextContent("2")
        portNum.view!!.isEnabled = false
        dialog_cancel_add_net.setOnClickListener {
            dismiss()
        }
        dialog_confirm_add_net.setOnClickListener {
            if (TextUtils.isEmpty(boardNum.getEditTextContent()))
                return@setOnClickListener
            listenter!!.confirmClick(boardNum.getEditTextContent())
            dismiss()
        }
    }
}