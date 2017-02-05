package com.capstone.tip.emassage.ui.announcement.detail;

import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Announcement;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class AnnouncementDetailPresenter extends MvpNullObjectBasePresenter<AnnouncementDetailView> {

    private Realm realm;
    private Announcement announcement;

    public void start(int id) {
        realm = Realm.getDefaultInstance();
        announcement = realm.where(Announcement.class).equalTo(Constants.ID, id).findFirstAsync();
        announcement.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (announcement.isLoaded() && announcement.isValid()) {
                    getView().setAnnouncement(realm.copyFromRealm(announcement));
                }
            }
        });
    }

    public void stop() {
        announcement.removeChangeListeners();
        realm.close();
    }
}
