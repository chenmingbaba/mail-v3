package net.basicmodel.utils;

import com.tencent.mmkv.MMKV;

import net.basicmodel.entity.NetInfoEntityNew;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MMKVUtils {

    public static void saveKeys(String key, String keyValues) {
        Set<String> keys = MMKV.defaultMMKV().decodeStringSet(key);
        if (keys == null) {
            keys = new HashSet<>();
        }
        keys.add(keyValues);
        MMKV.defaultMMKV().encode(key, keys);
    }

    public static void deleteKey(String key, String mainKey) {
        Set<String> keySet = MMKV.defaultMMKV().decodeStringSet(mainKey);
        if (keySet != null) {
            keySet.remove(key);
        }
        MMKV.defaultMMKV().encode(mainKey, keySet);
    }

    public static ArrayList<String> getKeys(String key) {
        ArrayList<String> data = new ArrayList<>();
        Set<String> keys = MMKV.defaultMMKV().decodeStringSet(key);
        if (keys != null) {
            data.addAll(keys);
        }
        return data;
    }

    public static ArrayList<NetInfoEntityNew> getNetDatas(String key) {
        ArrayList<NetInfoEntityNew> result = new ArrayList<>();
        Set<String> keySet = MMKV.defaultMMKV().decodeStringSet(key);
        if (keySet != null) {
            for (String item : keySet) {
                NetInfoEntityNew entity = MMKV.defaultMMKV().decodeParcelable(item, NetInfoEntityNew.class);
                result.add(entity);
            }
        }
        return result;
    }
}
