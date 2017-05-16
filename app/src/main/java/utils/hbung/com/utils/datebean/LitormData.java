package utils.hbung.com.utils.datebean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/27 0027　12:30
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
@Table("LitormData")
public class LitormData {
    @PrimaryKey(value = AssignType.AUTO_INCREMENT)
    private int id;
    private int columA;
    private int columB;
}
