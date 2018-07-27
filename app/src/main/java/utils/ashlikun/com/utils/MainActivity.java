package utils.ashlikun.com.utils;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashlikun.animcheckbox.AnimCheckBox;
import com.ashlikun.supertoobar.ImageAction;
import com.ashlikun.supertoobar.SupperToolBar;
import com.ashlikun.supertoobar.TextAction;
import com.ashlikun.utils.Utils;
import com.ashlikun.utils.ui.ToastUtils;
import com.ashlikun.wheelview3d.listener.OnItemSelectListener;
import com.ashlikun.wheelview3d.view.DialogOptions;
import com.ashlikun.xrecycleview.OnLoaddingListener;
import com.ashlikun.xrecycleview.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import utils.ashlikun.com.utils.datebean.LoopData;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {

    RecyclerView recycleView;
    SupperToolBar supperToolBar;
    List<Boolean> listData = new ArrayList<>();
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(new Utils.OnNeedListener() {
            @Override
            public Application getApplication() {
                return getApplication();
            }

            @Override
            public boolean isDebug() {
                return true;
            }
        });
        for (int i = 0; i < 100; i++) {
            listData.add(true);
        }
        setContentView(R.layout.activity_main);
        recycleView = findViewById(R.id.recycleView);
        supperToolBar = findViewById(R.id.toolBar);
        supperToolBar.setBack(this);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setChangeDuration(0);
        recycleView.setItemAnimator(itemAnimator);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter = new RecyclerView.Adapter<MyHolder>() {
            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.anim_checkbox_item, parent, false));
            }

            @Override
            public void onBindViewHolder(MyHolder holder, int position) {
                holder.checkBox.setChecked(listData.get(position), true);
            }

//            @Override
//            public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
//                if (payloads.isEmpty()) {
//                    onBindViewHolder(holder, position);
//                } else {
//                    holder.checkBox.setChecked(listData.get(position), true);
//                }
//            }

            @Override
            public int getItemCount() {
                return listData.size();
            }
        });
        findViewById(R.id.actionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 10; i++) {
                    listData.set(i, !listData.get(i));
                }
                adapter.notifyItemRangeChanged(0, 10, "aaa");
            }
        });

        supperToolBar.setTitle("标题");


        supperToolBar.addAction(new ImageAction(supperToolBar, R.mipmap.ic_cj_clock));


        supperToolBar.addAction(new ImageAction(supperToolBar, R.mipmap.ic_cj_clock)
                .set());
        supperToolBar.setNotification(1, 7);


        supperToolBar.addAction(new TextAction(supperToolBar, "分享")
                .setNotificationStrokeColor(0xff458788)
                .setNotification(10)
                .set());

        supperToolBar.setOnActionClickListener((index, action) ->
                ToastUtils.showShort(getApplicationContext(), "aaaaaaa" + index));

    }

    class MyHolder extends RecyclerView.ViewHolder {
        AnimCheckBox checkBox;

        public MyHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public void showLoopView() {
        new DialogOptions.Builder(this)
                .layoutId(R.layout.view_pickerview_options)
                .onItemSelectListener(new OnItemSelectListener<LoopData, LoopData, LoopData>() {
                    @Override
                    public List<LoopData> getOneData() {
                        List<LoopData> data = new ArrayList<>();
                        for (int i = 0; i < 100; i++) {
                            data.add(new LoopData());
                        }
                        return data;
                    }
                })
                .builder().show();
    }

    @Override
    public void onLoadding() {

    }

    @Override
    public void onRefresh() {

    }
}
