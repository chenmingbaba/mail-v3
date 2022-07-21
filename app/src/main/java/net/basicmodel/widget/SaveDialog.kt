package net.basicmodel.widget

import android.content.Context
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_dialog_save.*
import net.basicmodel.R
import net.basicmodel.event.MessageEvent
import org.greenrobot.eventbus.EventBus


class SaveDialog(context: Context, val index: Int) : BaseDialog<SaveDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_save, null)
    }

    override fun setUiBeforShow() {
        ok.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("saveCurrentData", index))
            dismiss()
        }
        cancel.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("dontsave", index))
            dismiss()
        }

    }
}