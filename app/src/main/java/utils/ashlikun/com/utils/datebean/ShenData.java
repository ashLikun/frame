package utils.ashlikun.com.utils.datebean;

import com.ashlikun.wheelview3d.adapter.ILoopShowData;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/6　15:07
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class ShenData implements ILoopShowData {
    private int id;
    private String shen;

    public ShenData(int id, String shen) {
        this.id = id;
        this.shen = shen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShen() {
        return shen;
    }

    public void setShen(String shen) {
        this.shen = shen;
    }

    @Override
    public String getShowText() {
        return shen;
    }
}
