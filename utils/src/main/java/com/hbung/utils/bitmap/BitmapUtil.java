package com.hbung.utils.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import java.io.File;
import java.io.InputStream;

public class BitmapUtil {


    /*
     * 从资源中获取Bitmap   按照指定的宽高缩放
     */
    public static Bitmap decodeResource(Context context, int resourseId,
                                        int width, int height) {
        // 获取资源图片
        BitmapFactory.Options opts = null;
        if (width > 0 && height > 0) {
            opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resourseId, opts);
            // 计算图片缩放比例
            final int minSideLength = Math.min(width, height);
            opts.inSampleSize = computeSampleSize(opts, minSideLength, width
                    * height);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
        }
        try {
            return BitmapFactory.decodeResource(context.getResources(), resourseId, opts); // decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，无�?再使用java层的createBitmap，从而节省了java层的空间
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        if (degree % 360 == 0) {
            return bm;
        }
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
            BitmapUtil.recycleBitmap(bm);
        } catch (OutOfMemoryError e) {
        }
        return returnBm;
    }

    /***
     * 图片的缩放方
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放�?
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /*
     * /** 按照路径加载图片
     *
     */
    public static Bitmap decodeFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/11/28 17:57
     * <p>
     * 方法功能：加载元数据图片
     */

    public static Bitmap decodeByte(byte[] dst, int width, int height) {
        if (null != dst && dst.length > 0) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(dst, 0, dst.length, opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeByteArray(dst, 0, dst.length, opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 取得指定区域的图
     */
    public static Bitmap getBitmap(Bitmap source, int x, int y, int width,
                                   int height) {
        Bitmap bitmap = Bitmap.createBitmap(source, x, y, width, height);
        return bitmap;
    }

    /**
     * 从大图中截取小图
     */
    public static Bitmap getImage(Context context, Bitmap source, int row,
                                  int col, int rowTotal, int colTotal, float multiple,
                                  boolean isRecycle) {
        Bitmap temp = getBitmap(source, (col - 1) * source.getWidth()
                        / colTotal, (row - 1) * source.getHeight() / rowTotal,
                source.getWidth() / colTotal, source.getHeight() / rowTotal);

        if (isRecycle) {
            recycleBitmap(source);
        }
        if (multiple != 1.0) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
                    temp.getHeight(), matrix, true);
        }
        return temp;
    }

    /**
     * 从大图中截取小图
     */
    public static Drawable getDrawableImage(Context context, Bitmap source,
                                            int row, int col, int rowTotal, int colTotal, float multiple) {

        Bitmap temp = getBitmap(source, (col - 1) * source.getWidth()
                        / colTotal, (row - 1) * source.getHeight() / rowTotal,
                source.getWidth() / colTotal, source.getHeight() / rowTotal);
        if (multiple != 1.0) {
            Matrix matrix = new Matrix();
            matrix.postScale(multiple, multiple);
            temp = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(),
                    temp.getHeight(), matrix, true);
        }
        Drawable d = new BitmapDrawable(context.getResources(), temp);
        return d;
    }

    /**
     * drawable转换成bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {

            //获得drawable的基本信息
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                                    : Config.ARGB_8888);
            //建立对应的画布
            Canvas canvas = new Canvas(bitmap);
            //设置大小
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            //把drawable内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        } else {
            throw new IllegalArgumentException(
                    "can not support this drawable to bitmap now!!!");
        }
    }

    /*
     * 获取资源图片(res)
     */
    public static Bitmap decodeResource(Context context, int resourseId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true; // inPurgeable设置为true，否则被忽略
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resourseId);
        return BitmapFactory.decodeStream(is, null, opt); // decodeStream直接调用JNI>>nativeDecodeAsset()来完成decode，无�?再使用java层的createBitmap，从而节省了java层的空间
    }

    /**
     * TODO<图片圆角处理>
     *
     * @param srcBitmap 源图片的bitmap
     * @param ret       圆角的度数
     * @return Bitmap
     * @throw
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret) {

        if (null == srcBitmap) {
            return null;
        }

        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();

        BitmapShader bitmapShader = new BitmapShader(srcBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        RectF rectf = new RectF(0, 0, bitWidth, bitHight);

        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();

        return outBitmap;
    }

    /**
     * 回收不用的bitmap
     *
     * @param b
     */
    public static void recycleBitmap(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            b.recycle();
            b = null;
        }
    }
}
