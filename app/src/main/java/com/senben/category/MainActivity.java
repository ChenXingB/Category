package com.senben.category;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.senben.category.base.Row;
import com.senben.category.model.SubCategoryModel;
import com.senben.category.viewholder.SubCategoryContentViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private enum ViewType {
        VIEW_TYPE_TITLE,
        VIEW_TYPE_CONTENT,
        VIEW_TYPE_BANNER;
        private static ViewType[] allValues = values();

        public static ViewType fromOrdinal(int n) {
            if (n < 0 || n >= allValues.length) {
                return null;
            }
            return allValues[n];
        }
    }

    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RecyclerView recyclerView;

    private Context context;
    private List<Row> subCategoriesData = new ArrayList<>();

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
        progressBar = findViewById(R.id.progressBar);
        radioGroup = findViewById(R.id.radioGroup);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new CategoryAdapter());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

//                //获取左侧分类选项的坐标，在选中的时候使其居中
//                int[] location = new int[2];
//                radioButton.getLocationOnScreen(location);
//                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//                final int distance = location[1] - displayMetrics.heightPixels / 2;
//                Log.d("radioGroup", "distance = " + distance + " radio y = " + location[1]);
//                scrollView.smoothScrollBy(0, distance);

                int id = (int) radioButton.getTag();
                fetchSubCategoryData(id);
            }
        });
    }

    private void initData() {
        fetchCategoryData();
    }

    /**
     * 获取一级分类数据
     */
    private void fetchCategoryData() {
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

    /**
     * 获取二级分类数据
     *
     * @param id 一级分类id
     */
    private void fetchSubCategoryData(int id) {
        fromAssetsJsonFile(id);
    }

    /**
     * 读取本地json文件模拟获取数据
     */
    private void fromAssetsJsonFile(final int id) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String jsonStr = jsonFromAssetsFile(context, String.format(Locale.getDefault(), "category%d.json", id % 3));
                JSONObject jsonData;
                try {
                    jsonData = new JSONObject(jsonStr);
                    SubCategoryModel model = new Gson().fromJson(jsonData.toString(), SubCategoryModel.class);

                    subCategoriesData.clear();

                    // banner
                    SubCategoryModel.BannerBean banner = model.getBanner();
                    if (banner != null) {
                        subCategoriesData.add(new Row(ViewType.VIEW_TYPE_BANNER.ordinal(), banner));
                    }

                    //二级分类内容
                    List<SubCategoryModel.CategoryBean> categories = model.getCategories();
                    if (categories != null && !categories.isEmpty()) {
                        for (SubCategoryModel.CategoryBean bean : categories) {
                            List<SubCategoryModel.CategoryBean.SubCategoryBean> subCategories = bean.getSubCategories();
                            // 若二级分类中内容为空，则标题也不显示
                            if (subCategories != null && !subCategories.isEmpty()) {
                                subCategoriesData.add(new Row(ViewType.VIEW_TYPE_TITLE.ordinal(), bean.getCategoryName()));
                                subCategoriesData.add(new Row(ViewType.VIEW_TYPE_CONTENT.ordinal(), subCategories));
                            }
                        }
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, 500);

    }

    private class CategoryAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return subCategoriesData.get(position).getViewType();
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
                    return new SubCategoryContentViewHolder(context, view);
                case VIEW_TYPE_BANNER:
                    view = LayoutInflater.from(context).inflate(R.layout.list_item_category_banner, parent, false);
                    return new SubCategoryBannerViewHolder(view);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SubCategoryTitleViewHolder) {
                ((SubCategoryTitleViewHolder) holder).onBindViewHolder(subCategoriesData.get(position));
            } else if (holder instanceof SubCategoryContentViewHolder) {
                ((SubCategoryContentViewHolder) holder).onBindViewHolder(subCategoriesData.get(position));
            } else if (holder instanceof SubCategoryBannerViewHolder) {
                ((SubCategoryBannerViewHolder) holder).onBindViewHolder(subCategoriesData.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return subCategoriesData.size();
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

        void onBindViewHolder(Row item) {
            String title = (String) item.getData();
            tvSubCategoryTitle.setText(title);
        }
    }

    /**
     * 二级分类广告图
     */
    class SubCategoryBannerViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvSubCategoryBanner;

        SubCategoryBannerViewHolder(View itemView) {
            super(itemView);
            sdvSubCategoryBanner = itemView.findViewById(R.id.sdvSubCategoryBanner);
            sdvSubCategoryBanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Row row = subCategoriesData.get(getAdapterPosition());
                    SubCategoryModel.BannerBean bannerBean = (SubCategoryModel.BannerBean) row.getData();
                    Toast.makeText(context, bannerBean.getLink(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        void onBindViewHolder(Row item) {
            //动态设置banner图比例
            sdvSubCategoryBanner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sdvSubCategoryBanner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = sdvSubCategoryBanner.getLayoutParams();
                    layoutParams.height = (int) (sdvSubCategoryBanner.getWidth() * 1.0f / 265 * 110);
                    sdvSubCategoryBanner.setLayoutParams(layoutParams);
                }
            });


            SubCategoryModel.BannerBean bannerBean = (SubCategoryModel.BannerBean) item.getData();
            sdvSubCategoryBanner.setImageURI(bannerBean.getPic());
        }
    }

    /**
     * 从assets中读取json文件中的json字符串
     *
     * @param context   上下文
     * @param assetName json文件的名称
     * @return 返回json字符串
     */
    public static String jsonFromAssetsFile(Context context, String assetName) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputReader;
        try {
            inputReader = new InputStreamReader(
                    context.getResources().getAssets().open(assetName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
