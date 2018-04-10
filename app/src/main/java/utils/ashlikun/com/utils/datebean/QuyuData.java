package utils.ashlikun.com.utils.datebean;

import com.ashlikun.wheelview3d.adapter.ILoopShowData;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/6　15:07
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class QuyuData implements ILoopShowData {
    private int id;
    private String quyu;

    public QuyuData(int id, String quyu) {
        this.id = id;
        this.quyu = quyu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuyu() {
        return quyu;
    }

    public void setQuyu(String quyu) {
        this.quyu = quyu;
    }

    @Override
    public String getShowText() {
        return quyu;
    }
}
