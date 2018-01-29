package utils.ashlikun.com.utils;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashlikun.superwebview.SuperWebView;
import com.ashlikun.utils.Utils;
import com.ashlikun.wheelview3d.listener.OnItemSelectListener;
import com.ashlikun.wheelview3d.view.DialogOptions;
import com.ashlikun.xrecycleview.OnLoaddingListener;
import com.ashlikun.xrecycleview.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

import utils.ashlikun.com.utils.datebean.LoopData;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {

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
        setContentView(R.layout.activity_main);
        final SuperWebView webView = (SuperWebView) findViewById(R.id.webView);

        findViewById(R.id.actionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("https://test1.miaofun.com/xiamai/indssssex.html#/home?");
//                Intent intent = new Intent(MainActivity.this, RxJavaTestActivity.class);
//                startActivity(intent);
            }
        });
        findViewById(R.id.actionButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.loadUrl("https://test1.miaofun.com/xiamai/index.html#/home?");
//                Intent intent = new Intent(MainActivity.this, RxJavaTestActivity.class);
//                startActivity(intent);
                showLoopView();
            }
        });
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
