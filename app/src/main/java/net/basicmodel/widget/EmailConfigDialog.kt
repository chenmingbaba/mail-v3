package net.basicmodel.widget

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import com.flyco.dialog.widget.base.BaseDialog
import com.luck.picture.lib.tools.ToastUtils
import com.tencent.mmkv.MMKV
import com.xxxxxxh.mailv2.utils.Constant
import kotlinx.android.synthetic.main.layout_dialog_config.*
import net.basicmodel.R
import net.basicmodel.sendmail.UsefulSTMP

class EmailConfigDialog(context: Context) : BaseDialog<EmailConfigDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_config, null)
    }

    override fun setUiBeforShow() {
        val user:String = MMKV.defaultMMKV()!!.decodeString("mailUser","").toString()
        user.let {
            mailUser.setEditTextContent(it)
        }
        val pwd:String = MMKV.defaultMMKV()!!.decodeString("mailPwd","").toString()
        pwd.let {
            mailPwd.setEditTextContent(it)
        }
        mailUser.setHint("邮箱地址")
        mailPwd.setHint("授权码")
        cancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            val str1 = mailUser.getEditTextContent()
            val str2 = mailPwd.getEditTextContent()
            if (TextUtils.isEmpty(str1)) {
                ToastUtils.s(context, "请输入邮箱地址")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(str1) && TextUtils.isEmpty(str2))
                return@setOnClickListener
            var stmp = ""

            when {
                str1.contains("@qq.com") -> {
                    stmp = UsefulSTMP.QQ
                }
                str1.contains("@163.com") -> {
                    stmp = UsefulSTMP._163
                }
                str1.contains("@sina.com") -> {
                    stmp = UsefulSTMP.SINA
                }
                str1.contains("@gmail.com") -> {
                    stmp = UsefulSTMP.GMAIL
                }
            }

            if (stmp == ""){
                ToastUtils.s(context,"请配置QQ,163,sina,gmail邮箱中的一种")
                return@setOnClickListener
            }
            MMKV.defaultMMKV()!!.encode("host", stmp)
            MMKV.defaultMMKV()!!.encode("mailUser", mailUser.getEditTextContent())
            MMKV.defaultMMKV()!!.encode("mailPwd", mailPwd.getEditTextContent())
            dismiss()
        }
    }
}