package com.hbung.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/8/4.
 */
public class JsonTypeAdapter {

    public static class IntegerTypeAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        // json转为对象时调用,实现JsonDeserializer<>接口
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.getAsString() == "" || json.getAsString() == null) {
                return 0;
            } else {
                return json.getAsJsonPrimitive().getAsInt();
            }
        }

        // 对象转为Json时调用,实现JsonSerializer<>接口
        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

    public static class LongTypeAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
        // json转为对象时调用,实现JsonDeserializer<>接口
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.getAsString() == "" || json.getAsString() == null) {
                return 0l;
            } else {
                return json.getAsLong();
            }
        }

        // 对象转为Json时调用,实现JsonSerializer<>接口
        @Override
        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

    public static class DoubleTypeAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        // json转为对象时调用,实现JsonDeserializer<>接口
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.getAsString() == "" || json.getAsString() == null) {
                return 0d;
            } else {
                return json.getAsDouble();
            }
        }

        // 对象转为Json时调用,实现JsonSerializer<>接口
        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

    public static class FloatTypeAdapter implements JsonSerializer<Float>, JsonDeserializer<Float> {
        // json转为对象时调用,实现JsonDeserializer<>接口
        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.getAsString() == "" || json.getAsString() == null) {
                return 0f;
            } else {
                return json.getAsFloat();
            }
        }

        // 对象转为Json时调用,实现JsonSerializer<>接口
        @Override
        public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

}
