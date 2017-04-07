package utils.hbung.com.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hbung.utils.ui.ToastUtils;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.adapter.LoopViewData;
import com.hbung.wheelview3d.adapter.SimpleLoopAdapter;
import com.hbung.wheelview3d.listener.LoopListener;
import com.hbung.wheelview3d.listener.OnItemSelectListener;
import com.hbung.wheelview3d.listener.OnPositiveClickListener;
import com.hbung.wheelview3d.view.DialogOptions;
import com.hbung.wheelview3d.view.LoopOptions;

import java.util.ArrayList;
import java.util.List;

import utils.hbung.com.utils.datebean.CityData;
import utils.hbung.com.utils.datebean.QuyuData;
import utils.hbung.com.utils.datebean.ShenData;

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
        loopView.setDividerColor(0xffff0000);
        loopView.setShowItemCount(9);

        findViewById(R.id.flatButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listDatas.clear();
//                if (chang) {
//                    for (int i = 0; i < 40; i++) {
//                        listDatas.add(new LoopViewData(i, "新的第" + i));
//                    }
//                } else {
//                    for (int i = 0; i < 40; i++) {
//                        listDatas.add(new LoopViewData(i, "新的新的第" + i));
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                chang = !chang;
                showDialog();
            }
        });

    }

    private void showDialog() {
        DialogOptions dialogOptions = new DialogOptions.Builder(this)
                .onItemSelectListener(new OnItemSelectListener<ShenData, CityData, QuyuData>() {

                    @Override
                    public List<ShenData> getOneData() {
                        return getShenData();
                    }

                    @Override
                    public List<CityData> getTowData(int onePosition, ShenData oneItemData) {
                        return getCity(oneItemData);
                    }
//
//                    @Override
//                    public List<QuyuData> getThreeData(int twoPosition, ShenData oneItemData, CityData towItemData) {
//                        return getQuyu(towItemData);
//                    }
                })
                .onPositiveClickListener(new OnPositiveClickListener<ShenData, CityData, QuyuData>() {
                    @Override
                    public void onPositive(ShenData oneData, CityData twoData, QuyuData threadData) {
                        ToastUtils.show(MainActivity.this, oneData.getShowText() + "   " + twoData != null ? twoData.getShowText() : ""
                                + "    " + threadData != null ? threadData.getShowText() : "", 1);
                    }
                })
                .mode(LoopOptions.Mode.TWO)
                .loop(false)
                .builder();
        dialogOptions.show();
    }

    private List<ShenData> getShenData() {
        List<ShenData> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new ShenData(i, "省" + i));
        }
        return datas;
    }

    private List<CityData> getCity(ShenData shenData) {
        List<CityData> datas = new ArrayList<>();
        for (int i = 0; i < 20 + shenData.getId(); i++) {
            datas.add(new CityData(i, "省" + shenData.getId() + "的 市" + i));
        }
        return datas;
    }

    private List<QuyuData> getQuyu(CityData cityData) {
        List<QuyuData> datas = new ArrayList<>();
        for (int i = 0; i < 30 + cityData.getId(); i++) {
            datas.add(new QuyuData(i, "市" + cityData.getId() + "的 区" + i));
        }
        return datas;
    }
}
