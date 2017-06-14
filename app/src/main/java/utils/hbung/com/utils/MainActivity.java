package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hbung.adapter.recyclerview.CommonAdapter;
import com.hbung.adapter.recyclerview.CommonHeaderAdapter;
import com.hbung.http.request.RequestParam;
import com.hbung.http.response.HttpResponse;
import com.hbung.loadingandretrymanager.ContextData;
import com.hbung.loadingandretrymanager.LoadingAndRetryManager;
import com.hbung.segmentcontrol.SegmentControlInterior;
import com.hbung.superwebview.SuperWebView;
import com.hbung.utils.Utils;
import com.hbung.utils.ui.ToastUtils;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.xrecycleview.OnLoaddingListener;
import com.hbung.xrecycleview.RefreshLayout;
import com.hbung.xrecycleview.SuperRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import utils.hbung.com.utils.datebean.GsonTest;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {
    LoopView loopView;
    List<LoopViewData> listDatas = new ArrayList<>();
    List<LoopViewData> headDatas = new ArrayList<>();
    List<View> listView = new ArrayList<>();
    CommonAdapter adapter;
    CommonHeaderAdapter headerAdapter;
    boolean chang = true;
    LoadingAndRetryManager manager;
    private SuperRecyclerView recyclerView;
    float position = 1;
    float aaaaa = 1;
    SegmentControlInterior controlInterior = null;
    ViewPager viewpager;

    private void aaa() {
        controlInterior.postDelayed(new Runnable() {
            @Override
            public void run() {

                position = position - 0.002f;
                if (position <= 0.01) {
                    position = 1;
                }
//                if (position >= 0.99) {
//                    aaaaa = -0.002f;
//                }
                // controlInterior.setSelectMove(true, position);
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
                ToastUtils.showShort(MainActivity.this, "aaaaaaaa" + aaaaa++);
            }
        });
        controlInterior = (SegmentControlInterior) findViewById(R.id.controlInterior);
        for (int i = 0; i < controlInterior.getCount(); i++) {
            SuperWebView superWebView = new SuperWebView(this);
            superWebView.setButtonClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(MainActivity.this, "aaaaa");
                }
            });
            superWebView.loadUrl("http://www.baidu.com");
            listView.add(superWebView);
        }
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new WebViewPagerAdapter(listView));

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                controlInterior.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.e("onPageScrolled", "position = " + position + "    positionOffset = " + positionOffset + "   positionOffsetPixels = " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("StateChanged", "state = " + state);
                controlInterior.onPageScrollStateChanged(state);
            }
        });
        controlInterior.setListener(new SegmentControlInterior.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                viewpager.setCurrentItem(index);
            }
        });

        //controlInterior.setCurrentIndex(1);
//        controlInterior.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // aaa();
//            }
//        }, 2000);
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
                p.toJson();
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
