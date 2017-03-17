package com.hbung.utils.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.hbung.utils.bitmap.BitmapUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.hbung.utils.Utils.myApp;


/**
 * 作者　　: 李坤
 * 创建时间: 13:49 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：对问价操作的类
 */

public class FileUtils {
    /**
     * 获取视频文件的一帧
     */
    public static Bitmap getVideoFrame(String path, int wDp, int hDp) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return BitmapUtil.zoomImage(media.getFrameAtTime(), DimensUtils.dip2px(myApp, wDp), DimensUtils.dip2px(myApp, hDp));
    }

    /**
     * 获取照片选择的文件路径
     */
    public static String getFileSelectPath(Intent data) {
        if (data == null || data.getData() == null) return null;
        return getPath(myApp, data.getData());
    }


    public static String startPaiZhao(Activity context, String appSDCachePath, int requestCode) {
        File fDir = new File(appSDCachePath);
        String cachePath = System.currentTimeMillis() + ".jpg";
        if (fDir.exists() || fDir.mkdirs()) {
            File cameraFile = new File(fDir, cachePath);
            if (!cameraFile.exists()) {
                try {
                    cameraFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            context.startActivityForResult(intent, requestCode);
        }
        return cachePath;
    }

    /**
     * 开启文件选择
     * <p>
     * intent.setType(“audio/*”); //选择音频
     * intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
     * intent.setType(“video/*;image/*”);//同时选择视频和图片
     */
    public static void startFileSelect(Activity context, String Type, int requestCode) {
        Intent intent = new Intent();
        intent.setType(Type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }

    public static void startFileSelect(Fragment context, String Type, int requestCode) {
        Intent intent = new Intent();
        intent.setType(Type);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        context.startActivityForResult(intent, requestCode);
    }


    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    public static String readInputStream(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            byte[] result = baos.toByteArray();
            return new String(result, "GB2312");
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }

    }

    public static String readInputStream(InputStream is, String coding) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            baos.close();
            byte[] result = baos.toByteArray();
            return new String(result, coding);
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }

    }

    /*
     * 读取assets文件目录下的文件，以字符串返回
     */
    public static String readAssets(Context context, String name) {
        String resultString = "";
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(name);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            resultString = new String(buffer);
        } catch (Exception e) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return resultString;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                FileInputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /*
        * 获取笨的的url的文件路径
        */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) {

        if (uri == null) {
            return "";
        }
        // DocumentProvider
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e("ssssss", "content");
            // Return the remote address
            if (isGooglePhotosUri(uri)) {

                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e("ssssss", "file");
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

}
