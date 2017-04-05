package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hbung.wheelview3d.LoopListener;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.wheelview3d.adapter.SimpleLoopAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LoopView loopView;
    List<LoopViewData> listDatas = new ArrayList<>();
    SimpleLoopAdapter adapter;
    boolean chang = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loopView = (LoopView) findViewById(R.id.loopView);

        for (int i = 0; i < 20; i++) {
            listDatas.add(new LoopViewData(i, "我是第" + i));
        }

        loopView.setAdapter(adapter = new SimpleLoopAdapter<LoopViewData>(listDatas) {
            @Override
            public String getShowText(int position) {
                return getItem(position).getTitle();
            }
        });
        loopView.setListener(new LoopListener<LoopViewData>() {
            @Override
            public void onItemSelect(int item, LoopViewData data) {
                Log.e("onItemSelect", data.getTitle());
            }
        });
        loopView.setSelectTextColor(0xffff0000);
        loopView.setLineWidth(3);
        loopView.setLineColor(0xffff0000);
        loopView.setShowItemCount(9);

        findViewById(R.id.flatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDatas.clear();
                if (chang) {
                    for (int i = 0; i < 40; i++) {
                        listDatas.add(new LoopViewData(i, "新的第" + i));
                    }
                } else {
                    for (int i = 0; i < 40; i++) {
                        listDatas.add(new LoopViewData(i, "新的新的第" + i));
                    }
                }
                adapter.notifyDataSetChanged();
                chang = !chang;
            }
        });

    }
}
