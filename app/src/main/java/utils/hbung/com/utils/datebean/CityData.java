package utils.hbung.com.utils.datebean;

import com.hbung.wheelview3d.adapter.ILoopShowData;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/6　15:07
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class CityData implements ILoopShowData {
    private int id;
    private String city;

    public CityData(int id, String city) {
        this.id = id;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getShowText() {
        return city;
    }
}
