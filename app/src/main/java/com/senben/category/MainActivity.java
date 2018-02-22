package com.senben.category;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private enum ViewType {
        VIEW_TYPE_TITLE,
        VIEW_TYPE_CONTENT;
        private static ViewType[] allValues = values();

        public static ViewType fromOrdinal(int n) {
            if (n < 0 || n >= allValues.length) {
                return null;
            }
            return allValues[n];
        }
    }

    private NestedScrollView scrollView;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;
    private TextView textView;

    private Context context;
    private List<Row> subCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);//初始化框架
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initUI();
        initData();
    }

    private void initUI() {
        scrollView = findViewById(R.id.scrollView);
        radioGroup = findViewById(R.id.radioGroup);
        recyclerView = findViewById(R.id.recyclerView);
        textView = findViewById(R.id.textView);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);

//                //获取左侧分类选项的坐标，在选中的时候使其居中
//                int[] location = new int[2];
//                radioButton.getLocationOnScreen(location);
//                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//                final int distance = location[1] - displayMetrics.heightPixels / 2;
//                Log.d("radioGroup", "distance = " + distance + " radio y = " + location[1]);
//                scrollView.smoothScrollBy(0, distance);

                int id = (int) radioButton.getTag();
                textView.setText("分类" + id);
            }
        });
    }

    private void initData() {
        radioGroup.removeAllViews();
        for (int id = 0; id < 18; id++) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.radio_button_top_category, radioGroup, false);
            String title = "分类" + id;
            radioButton.setText(title);
            radioButton.setTag(id);
            radioGroup.addView(radioButton);
        }

        // 默认选中第一个
        int checkedId = radioGroup.getChildAt(0).getId();
        radioGroup.check(checkedId);
    }

    private class CategoryAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return subCategory.get(position).getViewType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewType type = ViewType.fromOrdinal(viewType);
            if (type == null) {
                return null;
            }

            View view;
            switch (type) {
                case VIEW_TYPE_TITLE:
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_category_title, parent, false);
                    return new SubCategoryTitleViewHolder(view);
                case VIEW_TYPE_CONTENT:
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_category_content, parent, false);

            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SubCategoryTitleViewHolder) {
                ((SubCategoryTitleViewHolder) holder).onBindViewHolder(subCategory.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return subCategory.size();
        }
    }

    /**
     * 二级分类标题
     */
    class SubCategoryTitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubCategoryTitle;

        SubCategoryTitleViewHolder(View itemView) {
            super(itemView);
            tvSubCategoryTitle = itemView.findViewById(R.id.tvSubCategoryTitle);
        }

        public void onBindViewHolder(Row item) {
            String title = (String) item.getData();
            tvSubCategoryTitle.setText(title);
        }

    }
}
