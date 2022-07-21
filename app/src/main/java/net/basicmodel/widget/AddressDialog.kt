package net.basicmodel.widget

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_dialog_address.*
import net.basicmodel.R
import net.basicmodel.event.MessageEvent
import net.basicmodel.sendmail.EmailUtil
import org.greenrobot.eventbus.EventBus

class AddressDialog(context: Context, val index: Int) : BaseDialog<AddressDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_address, null)
    }

    override fun setUiBeforShow() {
        address.setHint("接收邮箱")
        val addressStr = MMKV.defaultMMKV()!!.decodeString("address", "")
        if (!TextUtils.isEmpty(addressStr)) {
            address.setEditTextContent(addressStr!!)
        }
        cancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            if (TextUtils.isEmpty(address.getEditTextContent())) {
                ToastUtils.s(context, "请输入邮箱地址")
                return@setOnClickListener
            }
            if (!address.getEditTextContent().toString().contains("@")) {
                ToastUtils.s(context, "请输入正确的邮箱地址")
                return@setOnClickListener
            }
            MMKV.defaultMMKV()!!.encode("address", address.getEditTextContent())
            EventBus.getDefault().post(MessageEvent("send", address.getEditTextContent(), index))
            dismiss()
        }
    }
}