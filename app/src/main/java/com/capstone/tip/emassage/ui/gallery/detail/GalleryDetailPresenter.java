package com.capstone.tip.emassage.ui.gallery.detail;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Gallery;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public class GalleryDetailPresenter extends MvpNullObjectBasePresenter<GalleryDetailView> {

    private Realm realm;
    private Category category;
    private RealmResults<Gallery> galleryRealmResults;

    public void start(int id) {
        realm = Realm.getDefaultInstance();
        category = realm.where(Category.class).equalTo(Constants.ID, id).findFirstAsync();
        category.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (category.isLoaded() && category.isValid())
                    getView().setTitle(category.getTitle());
            }
        });
        galleryRealmResults = realm.where(Gallery.class).equalTo("category", id).findAllAsync();
        galleryRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Gallery>>() {
            @Override
            public void onChange(RealmResults<Gallery> element) {
                if (galleryRealmResults.isLoaded() && galleryRealmResults.isValid())
                    getView().setGallery(realm.copyFromRealm(galleryRealmResults));
            }
        });
    }

    public void stop() {
        galleryRealmResults.removeChangeListeners();
        realm.close();
    }

    public void refresh(final int id) {
        App.getInstance().getApiInterface().galleries(id).enqueue(new Callback<List<Gallery>>() {
            @Override
            public void onResponse(Call<List<Gallery>> call, final Response<List<Gallery>> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            RealmResults<Gallery> galleries = realm.where(Gallery.class)
                                    .equalTo("category", id).findAll();
                            galleries.deleteAllFromRealm();
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
                            getView().showMessage("Error Saving Gallery in DB");
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
            public void onFailure(Call<List<Gallery>> call, Throwable t) {
                t.printStackTrace();
                getView().stopLoading();
                getView().showMessage("Error Calling API");
            }
        });
    }

}
