package com.hbung.charbar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/5　9:12
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
@Target(ElementType.FIELD)//应用于字段
@Retention(RetentionPolicy.RUNTIME)//存在时间   运行时
public @interface TargetField {
}
