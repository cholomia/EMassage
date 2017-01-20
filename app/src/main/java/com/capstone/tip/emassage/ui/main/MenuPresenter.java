package com.capstone.tip.emassage.ui.main;

import com.capstone.tip.emassage.model.data.User;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * @author pocholomia
 * @since 20/01/2017
 */

class MenuPresenter extends MvpNullObjectBasePresenter<MenuView> {
    private Realm realm;
    private User user;

    public void onStart() {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirstAsync();
        user.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (user.isLoaded() && user.isValid()) {
                    getView().onSetUser(realm.copyFromRealm(user));
                }
            }
        });
    }


    public void onStop() {
        user.removeChangeListeners();
        realm.close();
    }
}
