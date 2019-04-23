package com.ashlikun.glideutils.okhttp;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.GlideUrl;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by likun on 2016/8/3.
 * 加载图片进度封装
 */

class ProgressResponseBody extends ResponseBody {
    private static final int READ_PROGRESS_TIME = 100;
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    GlideUrl url;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    long totalBytesRead = 0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ProgressManage.onProgress(url, totalBytesRead, contentLength(), totalBytesRead == contentLength());
            if (totalBytesRead != contentLength()) {
                mainHandler.postDelayed(runnable, READ_PROGRESS_TIME);
            } else {
                runnable = null;
                mainHandler = null;
            }
        }
    };

    public ProgressResponseBody(GlideUrl url, ResponseBody responseBody) {
        this.url = url;
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        mainHandler.postDelayed(runnable, READ_PROGRESS_TIME);
        return new ForwardingSource(source) {
            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += (bytesRead == -1) ? 0 : bytesRead;
                return bytesRead;
            }
        };
    }
}
