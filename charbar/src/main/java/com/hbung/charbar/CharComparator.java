package com.hbung.charbar;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * .CharComparator接口用来对ListView中的数据根据A-Z进行排序，前面两个if判断主要是将不是以汉字开头的数据放在后面
 */
public class CharComparator<M> implements Comparator<M> {

    public int compare(M o1, M o2) {
        String olStr = getTargetField(o1);
        String o2Str = getTargetField(o2);


        if (olStr == null || o2Str == null) return 1;
        if (!olStr.matches("[A-Z]+") || !o2Str.matches("[A-Z]+")) {
            if (olStr.equals("#")) {
                return 1;
            } else if (o2Str.equals("#")) {
                return -1;
            }
        }
        //每个都比较
        return olStr.compareTo(o2Str);
    }

    private String getTargetField(M m) {
        Class mClass = m.getClass();
        Field[] fields = mClass.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);//启用或禁用安全检查  ，可以访问私有变量
            if (f.getAnnotation(TargetField.class) != null) {//字段是否加了TargetField注解
                try {
                    Object o = f.get(m);
                    if (o instanceof String) {
                        return (String) o;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
