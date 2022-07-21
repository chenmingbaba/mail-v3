package net.basicmodel.widget

import android.app.Activity
import android.content.Context
import android.view.View
import com.flyco.dialog.widget.base.BaseDialog
import kotlinx.android.synthetic.main.layout_dialog_select_pic.*
import net.basicmodel.R
import net.basicmodel.utils.PhotoCallBack
import net.basicmodel.utils.PhotoManager

class SelectDialog(context: Context, val activity: Activity, val callBack: PhotoCallBack) :
    BaseDialog<SelectDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_select_pic, null)
    }

    override fun setUiBeforShow() {
        gallery.setOnClickListener {
            PhotoManager.get().openAlbum(activity, callBack)
            dismiss()
        }
        camera.setOnClickListener {
            PhotoManager.get().takePhoto(activity, callBack)
            dismiss()
        }
    }
}