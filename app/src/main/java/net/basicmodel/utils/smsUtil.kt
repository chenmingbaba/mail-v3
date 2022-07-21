package net.basicmodel.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import net.basicmodel.entity.SMSEntity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

fun Context.getTodayAllSMS(block: (Boolean, ArrayList<SMSEntity>) -> Unit) {
    val list = ArrayList<SMSEntity>()
    //这里可以加个协程在子线程里做耗时
    readSMS(list, block)

}

fun Context.readSMS(list: ArrayList<SMSEntity>, block: (Boolean, ArrayList<SMSEntity>) -> Unit) {
    val cursor: Cursor?
    try {
        val projection = arrayOf("_id", "address", "person", "body", "date", "type")
        cursor = contentResolver.query(
            Uri.parse("content://sms/"),
            projection,
            null,
            null,
            null
        )
        cursor?.let {
            while (cursor.moveToNext()) {
                val body: String = cursor.getString(cursor.getColumnIndex("body"))
                val date: Long = cursor.getLong(cursor.getColumnIndex("date"))
                if (date.isToday()) {
                    if (body.typeRule()) {
                        list.add(SMSEntity(body, date))
                    }
                }
            }
            block(list.isEmpty().not(), list)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getStartOfDay(): Date {
    val startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
    return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant())
}

//获取今日午夜23点59分59秒的时间
fun getTonightTime(): Date {
    val calendar = Calendar.getInstance()
    //今日0点0分0秒
    calendar.time = Date()
    calendar[Calendar.HOUR_OF_DAY] = 23
    calendar[Calendar.MINUTE] = 59
    calendar[Calendar.SECOND] = 59
    return calendar.time
}

fun getTodayStart(): Date {
    val calendar = Calendar.getInstance()
    //今日0点0分0秒
    calendar.time = Date()
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    return calendar.time
}

@RequiresApi(Build.VERSION_CODES.O)
fun getEndOfDay(): Date {
    val endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
    return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant())
}

fun Long.isToday(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getStartOfDay().time < this && this < getEndOfDay().time
    } else {
        getTodayStart().time < this && this < getTonightTime().time
    }
}

fun Long.between(start: Long, end: Long): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this in (start + 1) until end
    } else {
        false
    }
}

fun String.typeRule(): Boolean {
    return contains("标定事件")||contains("Calibration Event")
}

fun String.dateRule(): ArrayList<String>? {
    val regex =
        "[0-9]{4}[\\-][0-9]{1,2}[\\-][0-9]{1,2}[\\s+]([0-9]{1,2}+:)([0-9]{1,2}+:)[0-9]{1,2}"
    val p = Pattern.compile(regex)
    val m = p.matcher(this)
    val timeList = ArrayList<String>()
    while (m.find()) {
        Log.e("time", m.group())
        timeList.add(m.group())
    }
    return if (timeList.isEmpty() || timeList.size != 2) null else timeList
}

@SuppressLint("SimpleDateFormat")
fun String.getTimeToLong(): Long {
    val simple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date? = simple.parse(this)
    date?.let {
        return it.time
    } ?: kotlin.run {
        return -1L
    }
}

@SuppressLint("SimpleDateFormat")
fun String.getTimeToLong2(): Long {
    val simple = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: Date? = simple.parse(this)
    date?.let {
        return it.time
    } ?: kotlin.run {
        return -1L
    }
}
//1647851118000-this
//1647851155000
infix fun Long.offset(other: Long): Boolean {
    return minus(other) in (-360000..360000)
}
