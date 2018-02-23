package com.senben.category.model;

import com.senben.category.base.Row;

import java.util.List;

/**
 * 分类二级数据模型
 * Created by chenxingbin on 2018/1/24.
 */

public class SubCategoryModel {

    private BannerBean banner;
    private List<CategoryBean> categories;

    public BannerBean getBanner() {
        return banner;
    }

    public void setBanner(BannerBean banner) {
        this.banner = banner;
    }

    public List<CategoryBean> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryBean> categories) {
        this.categories = categories;
    }

    public static class BannerBean {
        private String link;
        private String pic;

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

    public static class CategoryBean {
        private String categoryName;
        private List<SubCategoryBean> subCategories;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<SubCategoryBean> getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(List<SubCategoryBean> subCategories) {
            this.subCategories = subCategories;
        }

        public static class SubCategoryBean extends Row {
            private String subCategoryLink;
            private String subCategoryName;
            private String subCategoryPic;

            public SubCategoryBean(int viewType, Object data) {
                super(viewType, data);
            }

            public String getSubCategoryLink() {
                return subCategoryLink;
            }

            public void setSubCategoryLink(String subCategoryLink) {
                this.subCategoryLink = subCategoryLink;
            }

            public String getSubCategoryName() {
                return subCategoryName;
            }

            public void setSubCategoryName(String subCategoryName) {
                this.subCategoryName = subCategoryName;
            }

            public String getSubCategoryPic() {
                return subCategoryPic;
            }

            public void setSubCategoryPic(String subCategoryPic) {
                this.subCategoryPic = subCategoryPic;
            }
        }
    }
}
