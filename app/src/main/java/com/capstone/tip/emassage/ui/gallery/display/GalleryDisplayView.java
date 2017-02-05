package com.capstone.tip.emassage.ui.gallery.display;

import com.capstone.tip.emassage.model.data.Gallery;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public interface GalleryDisplayView extends MvpView {

    void onBack();

    void onMenu();

    void setTitle(String title);

    void setGallery(Gallery gallery);
}
