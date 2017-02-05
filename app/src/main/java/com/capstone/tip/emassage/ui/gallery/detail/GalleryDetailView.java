package com.capstone.tip.emassage.ui.gallery.detail;

import com.capstone.tip.emassage.model.data.Gallery;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public interface GalleryDetailView extends MvpView {
    void setGallery(List<Gallery> galleries);

    void stopLoading();

    void showMessage(String message);

    void onGalleryClicked(Gallery gallery);

    void setTitle(String title);
}
