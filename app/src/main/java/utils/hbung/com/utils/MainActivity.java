package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.hbung.adapter.ViewHolder;
import com.hbung.adapter.recyclerview.CommonAdapter;
import com.hbung.adapter.recyclerview.CommonHeaderAdapter;
import com.hbung.adapter.recyclerview.click.OnItemClickListener;
import com.hbung.liteorm.LiteOrmUtil;
import com.hbung.loadingandretrymanager.ContextData;
import com.hbung.loadingandretrymanager.LoadingAndRetryManager;
import com.hbung.loadingandretrymanager.MyOnLoadingAndRetryListener;
import com.hbung.stickyrecyclerview.StickyHeadersBuilder;
import com.hbung.utils.Utils;
import com.hbung.utils.ui.ToastUtils;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.xrecycleview.OnLoaddingListener;
import com.hbung.xrecycleview.RefreshLayout;
import com.hbung.xrecycleview.SuperRecyclerView;
import com.hbung.xrecycleview.divider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import utils.hbung.com.utils.databinding.HeadItemListBinding;
import utils.hbung.com.utils.databinding.ItemListBinding;
import utils.hbung.com.utils.datebean.LitormData;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {
    LoopView loopView;
    List<LoopViewData> listDatas = new ArrayList<>();
    List<LoopViewData> headDatas = new ArrayList<>();
    CommonAdapter adapter;
    CommonHeaderAdapter headerAdapter;
    boolean chang = true;
    LoadingAndRetryManager manager;
    private SuperRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.myApp = getApplication();
        LinkedHashMap ll = new LinkedHashMap();
        LiteOrmUtil.getLiteOrm().save(new LitormData());

        manager = LoadingAndRetryManager.getLoadingAndRetryManager(findViewById(R.id.swipe),
                new MyOnLoadingAndRetryListener(this, null));

        recyclerView = (SuperRecyclerView) findViewById(R.id.recycleView);
        recyclerView.setOnRefreshListener(this);
        recyclerView.setOnLoaddingListener(this);
        recyclerView.setAdapter(adapter = new CommonAdapter<LoopViewData, ItemListBinding>
                (this, R.layout.item_list, listDatas) {
            @Override
            public void convert(ViewHolder<ItemListBinding> holder, LoopViewData o) {
                holder.dataBind.setData(o);
            }
        });
        headerAdapter = new CommonHeaderAdapter<LoopViewData, HeadItemListBinding>
                (this, R.layout.head_item_list, listDatas) {
            @Override
            public void convert(ViewHolder<HeadItemListBinding> holder, LoopViewData o) {
                holder.dataBind.setData(o);
                holder.dataBind.executePendingBindings();
            }

            @Override
            public long getHeaderId(int position) {
                return listDatas.get(position).getShowText().substring(0, 1).hashCode();
            }
        };
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        recyclerView.addItemDecoration(new StickyHeadersBuilder()
                .setRecyclerView(recyclerView.getRecyclerView())
                .setAdapter(adapter)
                .setStickyHeadersAdapter(headerAdapter, true)
                .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setRefreshing(true);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object data, int position) {
                ToastUtils.showShort(MainActivity.this, " aaa " + position);
            }
        });
    }


    private void showDialog() {
        manager.showLoading(new ContextData("是是是是是是"));
    }

    private char head = 'a';

    private List<LoopViewData> getShenData() {
        List<LoopViewData> datas = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            datas.add(new LoopViewData(i, head + "省" + i));
        }
        setHeadData();
        head++;
        return datas;
    }

    private void setHeadData() {
        headDatas.add(new LoopViewData(head, String.valueOf(head)));
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
