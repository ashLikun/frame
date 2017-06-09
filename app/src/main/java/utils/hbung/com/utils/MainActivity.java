package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hbung.adapter.recyclerview.CommonAdapter;
import com.hbung.adapter.recyclerview.CommonHeaderAdapter;
import com.hbung.http.OkHttpUtils;
import com.hbung.http.request.RequestParam;
import com.hbung.http.response.HttpResponse;
import com.hbung.http.response.HttpResult;
import com.hbung.loadingandretrymanager.ContextData;
import com.hbung.loadingandretrymanager.LoadingAndRetryManager;
import com.hbung.segmentcontrol.SegmentControlInterior;
import com.hbung.utils.Utils;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.xrecycleview.OnLoaddingListener;
import com.hbung.xrecycleview.RefreshLayout;
import com.hbung.xrecycleview.SuperRecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.hbung.com.utils.datebean.GsonTest;
import utils.hbung.com.utils.datebean.HttpTestData;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {
    LoopView loopView;
    List<LoopViewData> listDatas = new ArrayList<>();
    List<LoopViewData> headDatas = new ArrayList<>();
    CommonAdapter adapter;
    CommonHeaderAdapter headerAdapter;
    boolean chang = true;
    LoadingAndRetryManager manager;
    private SuperRecyclerView recyclerView;
    float position = 0;
     SegmentControlInterior controlInterior = null;
    private void aaa(){
        controlInterior.postDelayed(new Runnable() {
            @Override
            public void run() {
                position = (position + 0.001f);
                if (position >= 1) {
                    position = 0;
                }
                controlInterior.setSelectMove(false, position);
                aaa();
            }
        }, 20);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(getApplication(), false, null);
        setContentView(R.layout.activity_main);
        findViewById(R.id.actionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        controlInterior = (SegmentControlInterior) findViewById(R.id.controlInterior);
        controlInterior.setCurrentIndex(1);
        controlInterior.postDelayed(new Runnable() {
            @Override
            public void run() {
                aaa();
            }
        }, 2000);
//        Utils.myApp = getApplication();
//        LinkedHashMap ll = new LinkedHashMap();
//        LiteOrmUtil.getLiteOrm().save(new LitormData());
//
//        manager = LoadingAndRetryManager.getLoadingAndRetryManager(findViewById(R.id.swipe),
//                new MyOnLoadingAndRetryListener(this, null));
//
//        recyclerView = (SuperRecyclerView) findViewById(R.id.recycleView);
//        recyclerView.setOnRefreshListener(this);
//        recyclerView.setOnLoaddingListener(this);
//        recyclerView.setAdapter(adapter = new CommonAdapter<LoopViewData, ItemListBinding>
//                (this, R.layout.item_list, listDatas) {
//            @Override
//            public void convert(ViewHolder<ItemListBinding> holder, LoopViewData o) {
//                holder.dataBind.setData(o);
//            }
//        });
//        headerAdapter = new CommonHeaderAdapter<LoopViewData, HeadItemListBinding>
//                (this, R.layout.head_item_list, listDatas) {
//            @Override
//            public void convert(ViewHolder<HeadItemListBinding> holder, LoopViewData o) {
//                holder.dataBind.setData(o);
//                holder.dataBind.executePendingBindings();
//            }
//
//            @Override
//            public long getHeaderId(int position) {
//                return listDatas.get(position).getShowText().substring(0, 1).hashCode();
//            }
//        };
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
//        recyclerView.addItemDecoration(new StickyHeadersBuilder()
//                .setRecyclerView(recyclerView.getRecyclerView())
//                .setAdapter(adapter)
//                .setStickyHeadersAdapter(headerAdapter, true)
//                .build());
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setRefreshing(true);
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(ViewGroup parent, View view, Object data, int position) {
//                ToastUtils.showShort(MainActivity.this, " aaa " + position);
//            }
//        });
        gsonTest();
        httpTest();
    }

    private void httpTest() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                RequestParam p = new RequestParam();
                p.get();
                p.addHeader("accessToken", "8079CE15-038E-4977-8443-E885730DE268");
                //4690943?accessToken=8079CE15-038E-4977-8443-E885730DE268
                p.url("http://10.155.50.51:5080/api/jlh/apply/getCustomerApplyInfo/");
                p.appendPath("4690943");
                p.addParam("accessToken", "8079CE15-038E-4977-8443-E885730DE268");
                try {
                    HttpResult<List<HttpTestData>> result = OkHttpUtils.getInstance().
                            syncExecute(p, HttpResult.class, List.class, HttpTestData.class);
                    if (result.isSucceed()) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void gsonTest() {
        String gson = "{\n" +
                "\"key1\": {\n" +
                "\"key1\": \"aaaaa\",\n" +
                "\"key2\": 2,\n" +
                "\"key3\": 0.6,\n" +
                "\"key4\": true\n" +
                "},\n" +
                "\"key2\": 2,\n" +
                "\"key3\": 0.6,\n" +
                "\"key4\": true\n" +
                "}";
        HttpResponse response = new HttpResponse();
        response.json = gson;
        GsonTest s;
        try {
            s = response.getTypeToObject(GsonTest.class, "key1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
