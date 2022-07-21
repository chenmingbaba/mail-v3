package net.basicmodel.adapter

import android.text.Editable
import android.text.TextWatcher
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.basicmodel.entity.NetDetailsEntity
import net.basicmodel.widget.InputEditText

class NetDetailsAdapter(layoutResId: Int, data: ArrayList<NetDetailsEntity>, val isNew: Boolean) :
    BaseQuickAdapter<NetDetailsEntity, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: NetDetailsEntity) {
        val itemNetBoard = holder.getView<InputEditText>(R.id.itemNetBoard)
        val itemNetPort1 = holder.getView<InputEditText>(R.id.itemNetPort1)
        val itemNetPort2 = holder.getView<InputEditText>(R.id.itemNetPort2)
        itemNetBoard.setEditTextContent("同沟检测单板${item.index}")
        item.netDetailsBoardName = "同沟检测单板${item.index}"
        itemNetBoard.view!!.isEnabled = false
        itemNetPort1.setHint("端口对应光缆编号1")
        itemNetPort2.setHint("端口对应光缆编号2")
        if (!isNew) {
            itemNetBoard.setEditTextContent(item.netDetailsBoardName)
            itemNetPort1.setEditTextContent(item.netDetailsPort1)
            itemNetPort2.setEditTextContent(item.netDetailsPort2)
        }
        itemNetPort1.view!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                item.netDetailsPort1 = p0.toString()
            }
        })
        itemNetPort2.view!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                item.netDetailsPort2 = p0.toString()
            }
        })
    }

}