package com.capstone.tip.emassage.ui.announcement.list;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Announcement;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class AnnouncementListPresenter extends MvpNullObjectBasePresenter<AnnouncementListView> {

    private Realm realm;
    private RealmResults<Announcement> announcementRealmResults;

    void onStart() {
        realm = Realm.getDefaultInstance();
        announcementRealmResults = realm.where(Announcement.class).findAllSortedAsync("created", Sort.DESCENDING);
        announcementRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Announcement>>() {
            @Override
            public void onChange(RealmResults<Announcement> element) {
                if (announcementRealmResults.isLoaded() && announcementRealmResults.isValid())
                    getView().setAnnouncements(realm.copyFromRealm(announcementRealmResults));
            }
        });
    }

    void onStop() {
        announcementRealmResults.removeChangeListeners();
        realm.close();
    }

    void refresh() {
        App.getInstance().getApiInterface().announcements().enqueue(new Callback<List<Announcement>>() {
            @Override
            public void onResponse(Call<List<Announcement>> call, final Response<List<Announcement>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.delete(Announcement.class);
                            realm.insertOrUpdate(response.body());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            error.printStackTrace();
                            realm.close();
                            getView().showMessage("Error Saving Announcement to DB");
                        }
                    });
                } else {
                    try {
                        getView().showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        getView().showMessage(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                t.printStackTrace();
                getView().stopLoading();
                getView().showMessage("Error Retrieving Announcement List");
            }
        });
    }

}
