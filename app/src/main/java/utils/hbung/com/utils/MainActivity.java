package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.hbung.adapter.recyclerview.CommonAdapter;
import com.hbung.adapter.recyclerview.CommonHeaderAdapter;
import com.hbung.http.OkHttpUtils;
import com.hbung.http.request.RequestParam;
import com.hbung.http.response.HttpResponse;
import com.hbung.loadingandretrymanager.ContextData;
import com.hbung.loadingandretrymanager.LoadingAndRetryManager;
import com.hbung.segmentcontrol.SegmentControlInterior;
import com.hbung.utils.Utils;
import com.hbung.utils.ui.DrawableUtils;
import com.hbung.utils.ui.ToastUtils;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.xrecycleview.OnLoaddingListener;
import com.hbung.xrecycleview.RefreshLayout;
import com.hbung.xrecycleview.SuperRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.hbung.com.utils.datebean.GsonTest;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {
    private String data = "<img src=\"data:image/png;base64,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\" style=\"display: block;\">";
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

    public class JsToAndroid {
        @JavascriptInterface
        public void assign(String aaa) {
            Log.e("aaa", aaa.toString());
        }
    }

    private void save() {
        Pattern pattern = Pattern.compile("\\?<=base64,");
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String a = matcher.group();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(getApplication(), false);
        setContentView(R.layout.activity_main);
        findViewById(R.id.actionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        findViewById(R.id.actionButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setBackgroundDrawable(new DrawableUtils.RippleBuilder(this).setNormalColor(R.color.red).setPressedColor(R.color.gray).create());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(MainActivity.this, "aaaaa");
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
//http://jielehua.vcash.cn/api/jlh/apply/getApplyProgress/118915?accessToken=4E396D4E-67E1-4B04-A70C-B1C6AFBFB238
        RequestParam p = new RequestParam("");
        p.get();
        p.addHeader("accessToken", "A8C5CF33-64A1-49F4-ADBC-4DBF05D5F94B");
        //4690943?accessToken=8079CE15-038E-4977-8443-E885730DE268
        p.url("http://jielehua.vcash.cn/api/jlh/apply/getApplyProgress/");
        p.appendPath("118915");
        p.addParam("accessToken", "A8C5CF33-64A1-49F4-ADBC-4DBF05D5F94B");
        OkHttpUtils.getInstance().execute(p, new HttpCallBack<String>() {
            @Override
            public void onSuccess(String responseBody) {
                Log.e("aa", responseBody.toString());
            }
        });
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
