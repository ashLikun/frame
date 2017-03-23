package com.hbung.http.response;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/23　15:12
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class ResponseSimeple {
    private final Request request;
    private final int code;
    private final String message;
    private final Headers headers;
    private final ResponseBody body;
    private final long sentRequestAtMillis;
    private final long receivedResponseAtMillis;

    private ResponseSimeple(Builder builder) {
        this.request = builder.request;
        this.code = builder.code;
        this.message = builder.message;
        this.headers = builder.headers;
        this.body = builder.body;
        this.sentRequestAtMillis = builder.sentRequestAtMillis;
        this.receivedResponseAtMillis = builder.receivedResponseAtMillis;
    }

    public Request getRequest() {
        return request;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Headers headers() {
        return headers;
    }

    public ResponseBody body() {
        return body;
    }

    public long getSentRequestAtMillis() {
        return sentRequestAtMillis;
    }

    public long getReceivedResponseAtMillis() {
        return receivedResponseAtMillis;
    }

    public static class Builder {
        private Request request;
        private int code = -1;
        private String message;
        private Headers headers;
        private ResponseBody body;
        private long sentRequestAtMillis;
        private long receivedResponseAtMillis;

        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(ResponseBody body) {
            this.body = body;
            return this;
        }

        public Builder sentRequestAtMillis(long sentRequestAtMillis) {
            this.sentRequestAtMillis = sentRequestAtMillis;
            return this;
        }

        public Builder receivedResponseAtMillis(long receivedResponseAtMillis) {
            this.receivedResponseAtMillis = receivedResponseAtMillis;
            return this;
        }

        public ResponseSimeple build() {
            return new ResponseSimeple(this);
        }

    }
}
