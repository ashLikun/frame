package com.ashlikun.wheelview3d.adapter;

/**
 * Created by Administrator on 2016/5/4.
 */
public class LoopViewData implements ILoopShowData {
    private int id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LoopViewData() {
    }

    public LoopViewData(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getShowText() {
        return title;
    }

}