package com.hbung.wheelview3d;

/**
 * Created by Administrator on 2016/5/4.
 */
public class LoopViewData {
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
}
