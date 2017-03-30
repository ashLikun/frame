package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hbung.wheelview3d.LoopListener;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.LoopViewData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LoopView loopView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loopView = (LoopView) findViewById(R.id.loopView);
        List<LoopViewData> listDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            listDatas.add(new LoopViewData(i, "我是第" + i));
        }
        loopView.setArrayList(listDatas);
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item, LoopViewData data) {
                Log.e("onItemSelect", data.getTitle());
            }
        });
    }
}
