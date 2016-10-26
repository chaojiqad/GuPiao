package com.subzero.shares.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 压缩bitmap
 * Created by The_p on 2016/5/4.
 */
public class BitmapUtils {
    public static Bitmap getBitmap(String imageUrl) {


        // 图片的选项
        BitmapFactory.Options opts = new BitmapFactory.Options();

        // 获取图片的属性 但是不去加载图片的方法

        opts.inJustDecodeBounds = true;
        // bitmap工厂
        BitmapFactory.decodeFile(imageUrl, opts);

        int scale = 8;

        // 设置缩放比例
        // 采样
        opts.inSampleSize = scale;

        opts.inJustDecodeBounds = false;

        // 重新去加载图片
        return BitmapFactory.decodeFile(imageUrl, opts);
    }


    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static String saveTempIamgeFile(Bitmap bm, String fileName) throws IOException {
        String path = Environment.getExternalStorageDirectory() + "/Share/Temp/";

        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(dirFile, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
        return path + fileName;
    }
}
