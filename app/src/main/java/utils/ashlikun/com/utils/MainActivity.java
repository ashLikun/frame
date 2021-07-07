package utils.ashlikun.com.utils;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashlikun.supertoobar.ImageAction;
import com.ashlikun.supertoobar.SuperToolBar;
import com.ashlikun.supertoobar.TextAction;
import com.ashlikun.utils.Utils;
import com.ashlikun.utils.other.DimensUtils;
import com.ashlikun.utils.ui.ToastUtils;
import com.ashlikun.xrecycleview.OnLoaddingListener;
import com.ashlikun.xrecycleview.RefreshLayout;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, OnLoaddingListener {

    RecyclerView recycleView;
    SuperToolBar supperToolBar;
    List<Boolean> listData = new ArrayList<>();
    RecyclerView.Adapter adapter;
    private String image = "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1535434196&di=584361192dd507b1778369caf125650b&src=http://pic31.nipic.com/20130723/7447430_105434565000_2.jpg";

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
        recycleView.postDelayed(new Runnable() {
            @Override
            public void run() {

                findViewById(R.id.actionButton).setSelected(true);
//                findViewById(R.id.actionButton).requestFocus();
//                findViewById(R.id.actionButton).requestFocusFromTouch();
            }
        }, 1000);

        supperToolBar.setBack(this);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setChangeDuration(0);
        recycleView.setItemAnimator(itemAnimator);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter = new RecyclerView.Adapter<MyHolder>() {
            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MyHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.imageview_item, parent, false));
            }

            @Override
            public void onBindViewHolder(MyHolder holder, int position) {
            }

            @Override
            public int getItemCount() {
                return listData.size();
            }
        });
        supperToolBar.addAction(new ImageAction(supperToolBar, R.mipmap.ic_cj_clock)
                .setWidth(DimensUtils.dip2px(this, 30))
                .setTintColor(0xff000000)
                .set());
        supperToolBar.addAction(new ImageAction(supperToolBar, R.mipmap.ic_cj_clock)
                .setTintColor(0xff000000)
                .set());
        supperToolBar.setNotification(1, 7);
        supperToolBar.addAction(new TextAction(supperToolBar, "分享分享分享")
                .setNotificationStrokeColor(0xff458788)
                .setNotificationMini()
                .set());

        supperToolBar.setOnActionClickListener((index, action) ->
                ToastUtils.showShort(getApplicationContext(), "aaaaaaa" + index));

        RequestOptions options = new RequestOptions();
        options.transform(new RoundedCorners(DimensUtils.dip2px(this, 6)));
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

    @Override
    public void onLoadding() {

    }

    @Override
    public void onRefresh() {

    }

    public void onStickClick(View view) {
        startActivity(new Intent(this, StickRecyclerViewActivity.class));
    }

    public void onClick(View view) {
        supperToolBar.setBackImgColor(0xffff0000);
        ImageAction action = supperToolBar.getAction(0);
        action.setDrawableId(R.drawable.material_back);
        action.setTintColor(0xffff0000);
        action.setNotification(100);
        TextAction textAction = supperToolBar.getAction(2);
        textAction.setNotification(101);
    }
}
