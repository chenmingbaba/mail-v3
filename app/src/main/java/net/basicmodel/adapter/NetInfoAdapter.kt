package net.basicmodel.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.basicmodel.entity.NetDetailsEntity

class NetInfoAdapter(layoutResId: Int, data: MutableList<NetDetailsEntity>?) :
    BaseQuickAdapter<NetDetailsEntity, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: NetDetailsEntity) {
        holder.setText(R.id.netInfoTv, item.netDetailsBoardName)
    }
}