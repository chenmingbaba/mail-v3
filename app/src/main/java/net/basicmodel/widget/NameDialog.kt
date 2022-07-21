package net.basicmodel.widget

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.flyco.dialog.widget.base.BaseDialog
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_dialog_name.*
import net.basicmodel.R
import net.basicmodel.adapter.NameAdapter
import net.basicmodel.entity.SMSEntity
import net.basicmodel.entity.TourEntityNew
import net.basicmodel.event.MessageEvent
import org.greenrobot.eventbus.EventBus

class NameDialog(
    context: Context, val data: ArrayList<String>, val index: Int,
    val tagType: Int = 0, val tagTime: Long = -1L
) :
    BaseDialog<NameDialog>(context) {
    override fun onCreateView(): View {
        widthScale(0.85f)
        return View.inflate(context, R.layout.layout_dialog_name, null)
    }

    override fun setUiBeforShow() {
        val mmkv = MMKV.defaultMMKV()
        val list = ArrayList<TourEntityNew>()
        val list1 = ArrayList<TourEntityNew>()
        data.map {
            mmkv?.decodeParcelable(it, TourEntityNew::class.java)?.let { it1 ->
                if (tagType != -1) {
                    if (tagType == 1) {
                        if (it1.smsContent != null) {
                            list.add(it1)
                        } else {
                        }
                    } else if (tagType == 0) {
                        if (it1.smsContent == null) {
                            list.add(it1)
                        } else {
                        }
                    } else {

                    }

                } else {
                    list.add(it1)
                }
            }
        }
        list.map { entity ->
            if (tagTime != -1L) {
                entity.smsContent?.let {
                    val sms = Gson().fromJson(it, SMSEntity::class.java)
                    if (sms.time > tagTime) {
                        list1.add(entity)
                    }
                } ?: kotlin.run {
                    list1.add(entity)
                }
            } else {
                list1.add(entity)
            }
        }
        if (list1.size < 1) {
            list1.add(TourEntityNew(nettour_stake = "无"))
        }
        val nameAdapter = NameAdapter(R.layout.layout_item_room, list1)
        dialog_recycler.layoutManager = LinearLayoutManager(context)
        dialog_recycler.adapter = nameAdapter
        nameAdapter.setOnItemClickListener { adapter, view, position ->
            EventBus.getDefault()
                .post(
                    MessageEvent(
                        "nameSelect",
                        index,
                        (adapter.data[position] as TourEntityNew).nettour_stake
                    )
                )
            dismiss()
        }
    }
}