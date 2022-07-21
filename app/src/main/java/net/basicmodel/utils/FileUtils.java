package net.basicmodel.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.xxxxxxh.mailv2.utils.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;

public class FileUtils {

    public static void saveFile(Context context, String content, String fileName) {

        if (TextUtils.isEmpty(content))
            return;

        File folder = new File(Constant.INSTANCE.getFilePath());

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(Constant.INSTANCE.getFilePath() + File.separator + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            deleteFile(context,fileName);
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(context, "保存成功,路径为：手机存储/netinfov2/", Toast.LENGTH_SHORT).show();
//            ClipUtils.INSTANCE.copy(context, Constant.INSTANCE.getFilePath() + File.separator + fileName);
            Constant.INSTANCE.setFILE_PATH("");
            Constant.INSTANCE.setFILE_PATH("手机存储/netinfov2/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            Constant.INSTANCE.setFILE_PATH("");
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteFile(Context context,String fileName){
        File f = new File(Constant.INSTANCE.getFilePath() + File.separator + fileName);
        if (f.exists()){
            f.delete();
            //Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
        }
    }

    public static String readrFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "";
        } else {
            try {
                FileReader reader = new FileReader(filePath);
                BufferedReader r = new BufferedReader(reader);
                return r.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void writeTxt2File(String content, String filePath,String fileName) throws Exception{

        deletOldFile(filePath + File.separator + fileName);

        makeFilePath(filePath,fileName);

        String strFilePath = filePath + File.separator + fileName;

        File file = new File(strFilePath);

        if (!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(file,"rwd");
        raf.seek(file.length());
        raf.write(content.getBytes());
        raf.close();
    }

    public static void deletOldFile(String path){
        File file = new File(path);
        if (file.exists()){
            file.delete();
        }
    }

    public static void makeFilePath(String filePath,String fileName)throws Exception{
        File file = null;
        file = new File(filePath + File.separator + fileName);

        if (!file.exists()){
            file.createNewFile();
        }
    }
}
