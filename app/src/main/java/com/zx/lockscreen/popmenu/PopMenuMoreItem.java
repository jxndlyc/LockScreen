package com.zx.lockscreen.popmenu;

/**
 * Created by liuyuchuan on 2018/8/20.
 */

public class PopMenuMoreItem {
    public int id; //标识
    public int resId; //资源图标
    public String text;//文字

    public PopMenuMoreItem(int id, String text) {
        this.id = id;
        this.resId = 0;
        this.text = text;
    }

    public PopMenuMoreItem(int id, int resId, String text) {
        this.id = id;
        this.resId = resId;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
