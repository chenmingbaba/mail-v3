package net.basicmodel.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.Gson
import net.basicmodel.R
import net.basicmodel.entity.SMSEntity
import net.basicmodel.entity.TourEntityNew

class NameAdapter(layoutResId: Int, data: ArrayList<TourEntityNew>?) :
    BaseQuickAdapter<TourEntityNew, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: TourEntityNew) {
        holder.setText(R.id.item_room_name, item.nettour_stake)
        item.smsContent?.let {
            val sms = Gson().fromJson(it, SMSEntity::class.java)
            if (sms.tag == 1) {
                holder.setTextColor(R.id.item_room_name, Color.RED)
            } else {
                holder.setTextColor(R.id.item_room_name, Color.BLACK)
            }
        } ?: kotlin.run {
            holder.setTextColor(R.id.item_room_name, Color.BLACK)
        }

    }
}