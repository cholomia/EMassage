package com.capstone.tip.emassage.ui.gallery.display;

import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Gallery;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class GalleryDisplayPresenter extends MvpNullObjectBasePresenter<GalleryDisplayView> {

    private Realm realm;
    private Gallery gallery;

    public void start(int id) {
        realm = Realm.getDefaultInstance();
        gallery = realm.where(Gallery.class).equalTo(Constants.ID, id).findFirstAsync();
        gallery.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (gallery.isLoaded() && gallery.isValid()) {
                   getView().setGallery(realm.copyFromRealm(gallery));
                    Category category = realm.where(Category.class)
                            .equalTo(Constants.ID, gallery.getCategory()).findFirst();
                    getView().setTitle(category.getTitle());
                }
            }
        });
    }

    void stop() {
        gallery.removeChangeListeners();
        realm.close();
    }
}
