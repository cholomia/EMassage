package com.capstone.tip.emassage.ui.gallery.list;

import com.capstone.tip.emassage.model.data.Category;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class GalleryListPresenter extends MvpNullObjectBasePresenter<GalleryListView> {

    private Realm realm;
    private RealmResults<Category> categoryRealmResults;

    void start() {
        realm = Realm.getDefaultInstance();
        categoryRealmResults = realm.where(Category.class).findAllAsync();
        categoryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> element) {
                if (categoryRealmResults.isLoaded() && categoryRealmResults.isValid()) {
                    getView().setCategories(realm.copyFromRealm(categoryRealmResults));
                }
            }
        });
    }

    void stop() {
        categoryRealmResults.removeChangeListeners();
        realm.close();
    }
}
