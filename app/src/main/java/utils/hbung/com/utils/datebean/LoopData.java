package utils.ashlikun.com.utils.datebean;

import com.ashlikun.wheelview3d.adapter.ILoopShowData;

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/23 0023　19:45
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopData implements ILoopShowData{
    String title = "aaaaaa";
    @Override
    public String getShowText() {
        return title;
    }
}
