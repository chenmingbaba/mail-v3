package net.basicmodel.utils

import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import net.basicmodel.entity.NetInfoEntityNew
import net.basicmodel.entity.ScramEntityNew
import net.basicmodel.entity.TourEntityNew

class DataHandleManager {
    companion object {
        private var i: DataHandleManager? = null
            get() {
                field ?: run {
                    field = DataHandleManager()
                }
                return field
            }

        @Synchronized
        fun get(): DataHandleManager {
            return i!!
        }
    }

    fun getCurData0(key: String): NetInfoEntityNew? {
        return MMKV.defaultMMKV()!!.decodeParcelable(key, NetInfoEntityNew::class.java)
    }

    fun getCurData1(key: String): ScramEntityNew? {
        return MMKV.defaultMMKV()!!.decodeParcelable(key, ScramEntityNew::class.java)
    }

    fun getCurData2(key: String): TourEntityNew? {
        return MMKV.defaultMMKV()!!.decodeParcelable(key, TourEntityNew::class.java)
    }

    fun getCurAllData0(key: String, curEntityNew: NetInfoEntityNew): ArrayList<NetInfoEntityNew> {
        val result = ArrayList<NetInfoEntityNew>()
        val entityNew = getCurData0(key)
        if (entityNew == null) {
            result.add(curEntityNew)
        }
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("net")
        keySet?.let {
            for (item in it) {
                MMKV.defaultMMKV()!!.decodeParcelable(item, NetInfoEntityNew::class.java)?.let {
                    result.add(it)
                }
            }
        }
        return result
    }

    fun handleData0(data: ArrayList<NetInfoEntityNew>): ArrayList<String> {
        val result = ArrayList<String>()
        for (item in data) {
            result.add(Gson().toJson(item))
        }
        return result
    }

    fun getCurAllData1(key: String, curEntityNew: ScramEntityNew): ArrayList<ScramEntityNew> {
        val result = ArrayList<ScramEntityNew>()
        val entityNew = getCurData1(key)
        if (entityNew == null) {
            result.add(curEntityNew)
        }
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("net")
        keySet?.let {
            for (item in it) {
                MMKV.defaultMMKV()!!.decodeParcelable(item, ScramEntityNew::class.java)?.let {
                    result.add(it)
                }
            }
        }
        return result
    }

    fun getCurAllData2(key: String, curEntityNew: TourEntityNew?): ArrayList<TourEntityNew> {
        val result = ArrayList<TourEntityNew>()
        val entityNew = getCurData2(key)
        if (entityNew == null) {
            if (curEntityNew!=null){
                result.add(curEntityNew)
            }
        }
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("tour")
        keySet?.let {
            for (item in it) {
                MMKV.defaultMMKV()!!.decodeParcelable(item, TourEntityNew::class.java)?.let {
                    result.add(it)
                }
            }
        }
        return result
    }

    fun handleData1(data: ArrayList<ScramEntityNew>): ArrayList<String> {
        val result = ArrayList<String>()
        for (item in data) {
            result.add(Gson().toJson(item))
        }
        return result
    }

    fun handleData2(data: ArrayList<TourEntityNew>): ArrayList<String> {
        val result = ArrayList<String>()
        for (item in data) {
            result.add(Gson().toJson(item))
        }
        return result
    }

    fun deleteData(key: String) {
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet(key)
        if (keySet != null) {
            for (item in keySet) {
                MMKV.defaultMMKV()!!.removeValueForKey(item)
            }
            keySet.clear()
            MMKV.defaultMMKV()!!.encode(key, keySet)
        }
    }
}