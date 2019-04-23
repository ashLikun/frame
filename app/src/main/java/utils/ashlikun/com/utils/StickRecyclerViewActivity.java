package utils.ashlikun.com.utils;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ashlikun.adapter.ViewHolder;
import com.ashlikun.adapter.recyclerview.CommonAdapter;
import com.ashlikun.stickyrecyclerview.CommonHeaderAdapter;
import com.ashlikun.stickyrecyclerview.OnHeaderClickListener;
import com.ashlikun.stickyrecyclerview.StickyHeadersBuilder;
import com.ashlikun.utils.ui.ToastUtils;
import com.ashlikun.wheelview3d.adapter.LoopViewData;
import com.ashlikun.xrecycleview.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/8/31　10:16
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class StickRecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommonHeaderAdapter headerAdapter;
    private List<LoopViewData> listDatas = new ArrayList<>();

    private CommonAdapter<LoopViewData> adapter = new CommonAdapter<LoopViewData>(this, R.layout.item_stick, listDatas) {
        @Override
        public void convert(ViewHolder holder, LoopViewData loopViewData) {
            holder.setText(R.id.textView, loopViewData.getShowText());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
        recyclerView = findViewById(R.id.recycleView);

        for (int i = 0; i < 50; i++) {
            listDatas.add(new LoopViewData(i, i + ""));
        }


        headerAdapter = new CommonHeaderAdapter<LoopViewData>
                (this, R.layout.head_item_list, listDatas) {
            @Override
            public boolean isHeader(int position) {
                return position == 3 || position == 8 || position == 10 || position == 20 || position == 15;
            }

            @Override
            public void convert(StickyViewHolder holder, LoopViewData loopViewData) {
                TextView tv = holder.getView(R.id.textView);
                tv.setText(loopViewData.getShowText());
            }

        };

        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.addItemDecoration(new StickyHeadersBuilder()
                .setRecyclerView(recyclerView)
                .setSticky(true)
                .setOnHeaderClickListener(new OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, long headerId) {
                        ToastUtils.showLong(getApplication(), headerId + "");
                    }
                })
                .setAdapter(adapter)
                .setStickyHeadersAdapter(headerAdapter, false)
                .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
