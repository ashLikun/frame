package utils.hbung.com.utils.datebean;

/**
 * 作者　　: 李坤
 * 创建时间:2017/5/20 0020　9:45
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class GsonTest {

    /**
     * key1 : 2
     * key2 : 2
     * key3 : 0.6
     * key4 : true
     */
    private String key1 = "key1";
    private int key2 = 333;
    private double key3 = 222.2;
    private boolean key4 = true;

    @Override
    public String toString() {
        return "GsonTest{" +
                "key1='" + key1 + '\'' +
                ", key2=" + key2 +
                ", key3=" + key3 +
                ", key4=" + key4 +
                '}';
    }
}
