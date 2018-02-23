package com.senben.category.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 所有的ViewHolder都要继承这个基类，用于规范开发代码
 * Created by chenxingbin on 2018/2/22.
 */
public abstract class MyViewHolder<T extends Row> extends RecyclerView.ViewHolder {

    abstract protected void onBindViewHolder(T item);

    public MyViewHolder(View itemView) {
        super(itemView);
    }
}