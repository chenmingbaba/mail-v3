package net.basicmodel.widget

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_dialog_user.*
import net.basicmodel.R
import net.basicmodel.utils.MMKVUtils

class UserNameConfigDialog(context: Context):BaseDialog<UserNameConfigDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_user, null)
    }

    override fun setUiBeforShow() {
        val u: String = MMKV.defaultMMKV()!!.decodeString("user","")!!
        if (!TextUtils.isEmpty(u)){
            userName.setEditTextContent(u)
        }else{
            userName.setHint("用户名")
        }
        cancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            val user = userName.getEditTextContent()
            if (TextUtils.isEmpty(user)){
                ToastUtils.s(context,"请输入用户名")
            }else{
                MMKV.defaultMMKV()!!.encode("user",user)
                dismiss()
            }
        }
    }
}