package net.basicmodel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import net.basicmodel.R
import net.basicmodel.utils.OnOptionClickListener


class EditViewContainer : LinearLayout {
    private var inputEditText: InputEditText? = null
    private var imageView: ImageView? = null
    private var listener: OnOptionClickListener? = null
    private var tag: String = ""

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context): View {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_editcontainer, this, true)
        inputEditText = v.findViewById(R.id.inputEdit)
        imageView = v.findViewById(R.id.optionRight)
        this.imageView!!.setOnClickListener {
            if (listener == null)
                return@setOnClickListener
            listener!!.onClick(tag)
        }
        return v
    }

    fun setTag(string: String) {
        this.tag = string
    }

    @JvmName("setListener1")
    fun setListener(listener: OnOptionClickListener) {
        this.listener = listener
    }

    fun getInputView(): InputEditText {
        return inputEditText!!
    }

    fun getOptionView(): ImageView {
        return imageView!!
    }


}