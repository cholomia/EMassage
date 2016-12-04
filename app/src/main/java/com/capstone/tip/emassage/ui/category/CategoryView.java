package com.capstone.tip.emassage.ui.category;

import com.capstone.tip.emassage.model.data.Category;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface CategoryView extends MvpView {

    void onCategoryItemClicked(Category category);
}
