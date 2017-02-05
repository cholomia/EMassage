package com.capstone.tip.emassage.ui.gallery.list;

import com.capstone.tip.emassage.model.data.Category;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public interface GalleryListView extends MvpView {
    void setCategories(List<Category> categories);

    void onCategoryClicked(Category category);
}
