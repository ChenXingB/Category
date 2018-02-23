package com.senben.category.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.senben.category.R;
import com.senben.category.base.GridSpacingItemDecoration;
import com.senben.category.base.MyViewHolder;
import com.senben.category.base.Row;
import com.senben.category.model.SubCategoryModel;

import java.util.ArrayList;
import java.util.List;


/**
 * 二级分类内容ViewHolder
 * Created by chenxingbin on 2018/1/24.
 */

public class SubCategoryContentViewHolder extends MyViewHolder<Row> {
    private RecyclerView recyclerView;

    private Context context;
    private List<SubCategoryModel.CategoryBean.SubCategoryBean> subCategoryItems = new ArrayList<>();

    public SubCategoryContentViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;

        recyclerView = itemView.findViewById(R.id.recyclerView);

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = 15f * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration((int) px, true));
        recyclerView.setAdapter(new SubCategoryContentAdapter());
    }

    @Override
    public void onBindViewHolder(Row item) {
        List<?> subCategories = (List<?>) item.getData();

        subCategoryItems.clear();
        for (int i = 0; i < subCategories.size(); i++) {
            subCategoryItems.add((SubCategoryModel.CategoryBean.SubCategoryBean) subCategories.get(i));
        }

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    class SubCategoryContentAdapter extends RecyclerView.Adapter<SubCategoryContentItemViewHolder> {
        @Override
        public SubCategoryContentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_category_content_item, parent, false);
            return new SubCategoryContentItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SubCategoryContentItemViewHolder holder, int position) {
            holder.onBindViewHolder(subCategoryItems.get(position));
        }

        @Override
        public int getItemCount() {
            return subCategoryItems.size();
        }
    }

    class SubCategoryContentItemViewHolder extends MyViewHolder<SubCategoryModel.CategoryBean.SubCategoryBean> {
        SimpleDraweeView sdvSubCategoryItem;
        TextView tvSubCategoryName;

        SubCategoryContentItemViewHolder(View itemView) {
            super(itemView);
            tvSubCategoryName = itemView.findViewById(R.id.tvSubCategoryName);
            sdvSubCategoryItem = itemView.findViewById(R.id.sdvSubCategoryItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleGotoDetail();
                }
            });

            sdvSubCategoryItem.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sdvSubCategoryItem.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) sdvSubCategoryItem.getLayoutParams();
                    layoutParams.height = sdvSubCategoryItem.getWidth();
                    sdvSubCategoryItem.setLayoutParams(layoutParams);
                }
            });
        }

        @Override
        protected void onBindViewHolder(SubCategoryModel.CategoryBean.SubCategoryBean item) {
            sdvSubCategoryItem.setImageURI(item.getSubCategoryPic());
            tvSubCategoryName.setText(item.getSubCategoryName());
        }

        private void handleGotoDetail() {
            int position = getAdapterPosition();
            if (position < 0 || position >= subCategoryItems.size()) {
                return;
            }

            SubCategoryModel.CategoryBean.SubCategoryBean subCategoryBean = subCategoryItems.get(position);
            Toast.makeText(context, subCategoryBean.getSubCategoryLink(), Toast.LENGTH_SHORT).show();
        }
    }
}