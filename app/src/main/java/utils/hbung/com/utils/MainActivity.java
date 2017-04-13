package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.hbung.adapter.ViewHolder;
import com.hbung.adapter.recyclerview.CommonAdapter;
import com.hbung.loadingandretrymanager.ContextData;
import com.hbung.loadingandretrymanager.LoadingAndRetryManager;
import com.hbung.loadingandretrymanager.MyOnLoadingAndRetryListener;
import com.hbung.utils.Utils;
import com.hbung.utils.ui.divider.HorizontalDividerItemDecoration;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.xrecycleview.OnLoaddingListener;
import com.hbung.xrecycleview.RefreshLayout;
import com.hbung.xrecycleview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {
    LoopView loopView;
    List<LoopViewData> listDatas = new ArrayList<>();
    CommonAdapter adapter;
    boolean chang = true;
    LoadingAndRetryManager manager;
    private SuperRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.myApp = getApplication();

        manager = LoadingAndRetryManager.getLoadingAndRetryManager(findViewById(R.id.swipe),
                new MyOnLoadingAndRetryListener(this, null));

        recyclerView = (SuperRecyclerView) findViewById(R.id.recycleView);
        recyclerView.setOnRefreshListener(this);
        recyclerView.setOnLoaddingListener(this);
        recyclerView.setAdapter(adapter = new CommonAdapter<LoopViewData>(this, R.layout.item_list, listDatas) {
            @Override
            public void convert(ViewHolder holder, LoopViewData o) {
                holder.setText(R.id.text, o.getShowText());
            }
        });
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshing(true);
    }

    private void showDialog() {
        manager.showLoading(new ContextData("是是是是是是"));
    }

    private List<LoopViewData> getShenData() {
        List<LoopViewData> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add(new LoopViewData(i, "省" + i));
        }
        return datas;
    }


    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listDatas.clear();
                listDatas.addAll(getShenData());
                adapter.notifyDataSetChanged();
                recyclerView.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onLoadding() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listDatas.addAll(getShenData());
                adapter.notifyDataSetChanged();
                recyclerView.getRecyclerView().complete();
            }
        }, 5000);
    }
}
