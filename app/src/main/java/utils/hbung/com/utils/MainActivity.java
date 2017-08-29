package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ashlikun.xrecycleview.OnLoaddingListener;
import com.ashlikun.xrecycleview.RefreshLayout;
import com.hbung.supertoobar.SupperToolBar;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SupperToolBar toolBar = (SupperToolBar) findViewById(R.id.toolBar);
        toolBar.setBack(this);
        toolBar.setTitle("aaaa");
        findViewById(R.id.actionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
