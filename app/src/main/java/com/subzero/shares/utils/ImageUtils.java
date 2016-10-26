package com.subzero.shares.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.subzero.shares.activity.Photo.SpaceImageDetailActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 处理图片
 * Created by zzy on 5/5/2016.
 */
public class ImageUtils {

    // 文件存放的路径
    private static String shareIconUrl = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/share.png";

    /**
     * 显示图片
     *
     * @param iv
     * @param avatarUrl
     */
    public static void displayAvatar(Context context, ImageView iv, String avatarUrl) {

        Picasso.with(context).load(avatarUrl).into(iv);

    }

    /**
     * 保存图片到内存卡
     *
     * @return
     */
    public static String saveMyBitmap(Context context, Bitmap mBitmap) {
        String filePath = FileUtils.getIconDir(context) + "/" + getOutTradeNo() + ".jpg";
        File f = new File(filePath);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 15, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * 图片名称
     */
    private static String getOutTradeNo() {
        return System.currentTimeMillis() + "";
    }

    /**
     * @param is      图片的输入流
     * @param context 获取share图片
     */
    private static Bitmap getAssetsImg(Context context, InputStream is) {

        try {

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            is.close();
            return BitmapFactory.decodeByteArray(outStream.toByteArray(), 0,
                    outStream.toByteArray().length);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 比例压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 1280f;//这里设置高度为1280
        float ww = 720f;//这里设置宽度为720
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        return bitmap;
    }


    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {    //循环判断如果压缩后图片是否大于1m,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 查看大图
     *
     * @param context
     * @param imageUrl
     */
    public static void startPhotoActivity(Activity context, String imageUrl, ImageView imageView) {
        Intent intent = new Intent(context, SpaceImageDetailActivity.class);
        intent.putExtra("imagesUrl", imageUrl);
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        intent.putExtra("locationX", location[0]);
        intent.putExtra("locationY", location[1]);

        intent.putExtra("width", imageView.getWidth());
        intent.putExtra("height", imageView.getHeight());
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

}
