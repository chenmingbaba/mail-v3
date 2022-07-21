package net.basicmodel.widget

import android.content.Context
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_pop.*
import net.basicmodel.R
import net.basicmodel.utils.OptionClickListener

class OptionDialog(context: Context) : BaseDialog<OptionDialog>(context) {

    var listener: OptionClickListener? = null

    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_pop, null)
    }

    override fun setUiBeforShow() {
        filed.setOnClickListener {
            listener?.OptionClick(0)
            dismiss()
        }
        add.setOnClickListener {
            listener?.OptionClick(1)
            dismiss()
        }
        delete.setOnClickListener {
            listener?.OptionClick(2)
            dismiss()
        }
        save.setOnClickListener {
            listener?.OptionClick(3)
            dismiss()
        }
        submit.setOnClickListener {
            listener?.OptionClick(4)
            dismiss()
        }
        mailSubmit.setOnClickListener {
            listener?.OptionClick(5)
            dismiss()
        }
        email.setOnClickListener {
            listener?.OptionClick(6)
            dismiss()
        }
        userName.setOnClickListener {
            listener?.OptionClick(7)
            dismiss()
        }

        submit2.setOnClickListener {
            listener?.OptionClick(8)
            dismiss()
        }

    }
}