package net.basicmodel.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.basicmodel.R
import net.basicmodel.entity.MarkerEntity

class MarkersPopupWindow(context: Context, builder: MarkersPopupWindowBuilder? = null) :
    PopupWindow() {

    init {
        val inflater = LayoutInflater.from(context)
        this.contentView = inflater.inflate(R.layout.popup_markers, null)
        this.width = RelativeLayout.LayoutParams.MATCH_PARENT //父布局减去padding
        this.height = RelativeLayout.LayoutParams.MATCH_PARENT
        this.animationStyle = R.style.pop_animation  //进入和退出动画效果
        this.isOutsideTouchable = true //是否可以
        this.isClippingEnabled = true //背景透明化可以铺满全屏
        val colorDrawable = ColorDrawable(Color.parseColor("#80000000"))
        this.setBackgroundDrawable(colorDrawable) //设置背景

    }

    class MarkersPopupWindowBuilder(val context: Context) {
        companion object {
            fun init(context: Context): MarkersPopupWindowBuilder {
                return MarkersPopupWindowBuilder(context)
            }
        }

        private val window: MarkersPopupWindow = MarkersPopupWindow(context, this)

        fun build(): MarkersPopupWindow {
            window.contentView.findViewById<RecyclerView>(R.id.marker_list).visibility =
                View.VISIBLE
            return window
        }


        fun setData(
            data: ArrayList<MarkerEntity>,
            callback: (MarkerEntity) -> Unit
        ): MarkersPopupWindowBuilder {
            val listView = window.contentView.findViewById<RecyclerView>(R.id.marker_list)
            listView.layoutManager = LinearLayoutManager(context)
            val adapter = object :
                BaseQuickAdapter<MarkerEntity, BaseViewHolder>(R.layout.item_marker, data) {
                override fun convert(holder: BaseViewHolder, item: MarkerEntity) {
                    holder.setText(R.id.title_tv, item.title)
                        .setText(R.id.longitude_tv, "经度：${item.lgt}")
                        .setText(R.id.latitude_tv, "纬度：${item.lat}")
                }
            }
            adapter.setOnItemClickListener { _, _, position ->
                callback(adapter.data[position])
                window.dismissSelf()
            }
            listView.adapter = adapter
            return this
        }
    }

    fun show(view: View) {
        showAsDropDown(view, 0, 0)
    }

    fun dismissSelf() {
        this.dismiss()
    }
}