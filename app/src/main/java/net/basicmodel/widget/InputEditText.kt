package net.basicmodel.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout
import net.basicmodel.R
import net.basicmodel.event.MessageEvent
import org.greenrobot.eventbus.EventBus

class InputEditText : LinearLayout {
    var view: EditText? = null
    var input: TextInputLayout? = null

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
        val v = LayoutInflater.from(context).inflate(R.layout.layout_editext, this, true)
        view = v.findViewById(R.id.editText)
        input = v.findViewById(R.id.inputLayout)
        view!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                EventBus.getDefault().post(MessageEvent("closeInput"))
            }
            false
        }
        return v
    }

    fun setMargin(top: Int, bottom: Int, left: Int, right: Int) {
        input!!.let {
            it.layoutParams = it.layoutParams.apply {
                (this as LinearLayout.LayoutParams).setMargins(left, top, right, bottom)
            }
        }
    }

    fun getInputLayout(): TextInputLayout {
        return input!!
    }

    fun getEditTextView(): EditText {
        return view!!
    }

    fun getEditTextContent(): String {
        return view!!.text.toString()
    }

    fun setEditTextContent(s: String) {
        view!!.setText(s)
    }

    fun setHint(s: String) {
        input!!.hint = s
    }

    fun getHint(): String {
        return input!!.hint.toString()
    }
}