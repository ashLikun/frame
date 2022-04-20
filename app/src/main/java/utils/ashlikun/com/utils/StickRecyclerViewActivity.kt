package utils.ashlikun.com.utils

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.recyclerview.common.CommonAdapter
import com.ashlikun.stickyrecyclerview.CommonHeaderAdapter
import com.ashlikun.stickyrecyclerview.StickyHeadersBuilder
import com.ashlikun.utils.ui.ToastUtils
import com.ashlikun.wheelview3d.adapter.LoopViewData
import com.ashlikun.xrecycleview.divider.HorizontalDividerItemDecoration

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/31　10:16
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class StickRecyclerViewActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var headerAdapter: CommonHeaderAdapter<*>? = null
    private val listDatas: MutableList<LoopViewData> = ArrayList()
    private val adapter = CommonAdapter(this, layoutId = R.layout.item_stick, initDatas = listDatas) {
        setText(R.id.textView, it.showText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stick)
        recyclerView = findViewById(R.id.recycleView)
        for (i in 0..49) {
            listDatas.add(LoopViewData(i, i.toString() + ""))
        }
        headerAdapter = object : CommonHeaderAdapter<LoopViewData>(this, R.layout.head_item_list, listDatas) {
            override fun isHeader(position: Int, data: LoopViewData): Boolean {
                return position == 3 || position == 8 || position == 10 || position == 20 || position == 15
            }

            override fun convert(holder: StickyViewHolder, loopViewData: LoopViewData) {
                val tv = holder.getView<TextView>(R.id.textView)
                val iv = holder.getView<ImageView>(R.id.imageView)
                tv.text = loopViewData.showText
                recyclerView!!.postDelayed(Runnable {
                    iv.setImageResource(R.mipmap.ic_cj_clock)
                    holder.invalidate()
                }, 1000)
            }
        }
        recyclerView!!.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        recyclerView!!.addItemDecoration(StickyHeadersBuilder()
            .setRecyclerView(recyclerView)
            .setSticky(true)
            .setOnHeaderClickListener { header, headerId -> ToastUtils.showLong(application, headerId.toString() + "") }
            .setAdapter(adapter)
            .setStickyHeadersAdapter(headerAdapter, false)
            .build())
        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
        recyclerView!!.setAdapter(adapter)
    }
}