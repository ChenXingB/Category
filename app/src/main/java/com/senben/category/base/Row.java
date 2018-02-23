package com.senben.category.base;

/**
 * RecyclerView每行的数据
 * Created by chenxingbin on 2018/2/2.
 */

public class Row {
    private int viewType;
    private Object data;

    public Row(int viewType, Object data) {
        this.viewType = viewType;
        this.data = data;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
