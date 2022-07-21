package net.basicmodel.utils

import android.content.Context
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import java.util.*


class TimePickerManager {
    companion object {
        private var i: TimePickerManager? = null
            get() {
                field ?: run {
                    field = TimePickerManager()
                }
                return field
            }

        @Synchronized
        fun get(): TimePickerManager {
            return i!!
        }
    }

    fun createTimePicker(
        context: Context,
        flag: String,
        listener: TimeSelectListener
    ): TimePickerView {
        var pvTime: TimePickerView? = null
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate[2021, 0] = 1
        endDate[2099, 11] = 31
        pvTime = TimePickerBuilder(
            context
        ) { date, v ->
            listener.timeSelect(flag, MyLocationManager.get().formatDate(date)!!)
        }.setType(booleanArrayOf(true, true, true, true, true, false)).setLabel(
            "年", "月", "日",
            "时", "分", "秒"
        ).setDate(Calendar.getInstance()).isDialog(true).setRangDate(startDate, endDate).build()
        pvTime!!.setOnDismissListener {
            listener.dialogDismiss()
        }
        return pvTime
    }

    fun createTimePicker2(
        context: Context,
        flag: String,
        listener: TimeSelectListener
    ): TimePickerView {
        var pvTime: TimePickerView? = null
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        startDate[2021, 0] = 1
        endDate[2099, 11] = 31
        pvTime = TimePickerBuilder(
            context
        ) { date, v ->
            listener.timeSelect(flag, MyLocationManager.get().formatDate2(date)!!)
        }.setType(booleanArrayOf(true, true, true, false, false, false)).setLabel(
            "年", "月", "日",
            "时", "分", "秒"
        ).setDate(Calendar.getInstance()).isDialog(true).setRangDate(startDate, endDate).build()
        pvTime!!.setOnDismissListener {
            listener.dialogDismiss()
        }
        return pvTime
    }
}