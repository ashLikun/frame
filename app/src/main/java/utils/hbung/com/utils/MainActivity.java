package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ashlikun.xrecycleview.OnLoaddingListener;
import com.ashlikun.xrecycleview.RefreshLayout;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onLoadding() {

    }

    @Override
    public void onRefresh() {

    }
}
