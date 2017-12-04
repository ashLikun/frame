package utils.ashlikun.com.utils;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashlikun.superwebview.SuperWebView;
import com.ashlikun.utils.Utils;
import com.ashlikun.xrecycleview.OnLoaddingListener;
import com.ashlikun.xrecycleview.RefreshLayout;

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
                webView.loadUrl("https://www.baiddau.com/");
//                Intent intent = new Intent(MainActivity.this, RxJavaTestActivity.class);
//                startActivity(intent);
            }
        });
        findViewById(R.id.actionButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("https://www.baidu.com/");
//                Intent intent = new Intent(MainActivity.this, RxJavaTestActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoadding() {

    }

    @Override
    public void onRefresh() {

    }
}
