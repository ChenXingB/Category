package com.senben.category.base;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 栅格分割线
 * Created by chenxingbin on 2018/1/24.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int space, boolean includeEdge) {
        this.space = space;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }

        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = space - column * space / spanCount;
            outRect.right = (column + 1) * space / spanCount;

            if (position < spanCount) { // top edge
                outRect.top = space;
            }
            outRect.bottom = space; // item bottom
        } else {
            outRect.left = column * space / spanCount;
            outRect.right = space - (column + 1) * space / spanCount;
            if (position >= spanCount) {
                outRect.top = space; // item top
            }
        }
    }
}