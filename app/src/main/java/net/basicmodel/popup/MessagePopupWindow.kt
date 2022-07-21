package net.basicmodel.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import net.basicmodel.R

class MessagePopupWindow(context: Context, builder: MessagePopupWindowBuilder? = null) :
    PopupWindow() {

    init {
        val inflater = LayoutInflater.from(context)
        this.contentView = inflater.inflate(R.layout.popup_fliter_options, null)
        this.width = 300 //父布局减去padding
        this.height = LinearLayout.LayoutParams.WRAP_CONTENT
        this.animationStyle = R.style.pop_animation  //进入和退出动画效果
        this.isOutsideTouchable = true //是否可以
        this.isClippingEnabled = false //背景透明化可以铺满全屏
        val colorDrawable = ColorDrawable(Color.parseColor("#00000000"))
        this.setBackgroundDrawable(colorDrawable) //设置背景

    }

    class MessagePopupWindowBuilder(val context: Context) {
        companion object {
            fun init(context: Context): MessagePopupWindowBuilder {
                return MessagePopupWindowBuilder(context)
            }
        }

        private val window: MessagePopupWindow = MessagePopupWindow(context, this)

        fun build(): MessagePopupWindow {
            window.contentView.findViewById<TextView>(R.id.option1).visibility = View.VISIBLE
            window.contentView.findViewById<TextView>(R.id.option2).visibility = View.VISIBLE
            return window
        }


        fun setEnsureListener(callback: (Int) -> Unit): MessagePopupWindowBuilder {
            window.contentView.findViewById<TextView>(R.id.option1).setOnClickListener {
                window.dismissSelf()
                callback(1)
            }
            window.contentView.findViewById<TextView>(R.id.option2).setOnClickListener {
                window.dismissSelf()
                callback(0)
            }
            window.contentView.findViewById<TextView>(R.id.option0).setOnClickListener {
                window.dismissSelf()
                callback(-1)
            }
            return this
        }
    }

    fun show(view: View) {
        showAsDropDown(view,100,0)
//        showAtLocation(contentView, Gravity.CENTER, 0, 0)
    }

    fun dismissSelf(){
        this.dismiss()
    }
}