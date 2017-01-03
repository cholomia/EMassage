package com.capstone.tip.emassage.ui.forums.form;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.data.User;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/22/2016.
 */

public class ForumFormPresenter extends MvpNullObjectBasePresenter<ForumFormView> {

    private static final String TAG = ForumFormPresenter.class.getSimpleName();
    private Realm realm;
    private User user;

    public void onStart(int id) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        if (id != -1) {
            Forum forum = realm.where(Forum.class).equalTo(Constants.ID, id).findFirst();
            getView().setForum(realm.copyFromRealm(forum));
        }
    }

    public void onStop() {
        realm.close();
    }

    public void saveForum(String title, String content, Forum forum) {
        if (title.isEmpty() || content.isEmpty()) {
            getView().showMessage("Please fill-up all fields");
        } else {
            getView().startLoading();
            saveForum(forum != null ?
                    App.getInstance().getApiInterface().editForum(forum.getId(),
                            Credentials.basic(user.getUsername(), user.getPassword()), title, content)
                    :
                    App.getInstance().getApiInterface().createForum(
                            Credentials.basic(user.getUsername(), user.getPassword()), title, content)
            );
        }
    }

    private void saveForum(Call<Forum> forumResponseCall) {
        forumResponseCall.enqueue(new Callback<Forum>() {
            @Override
            public void onResponse(Call<Forum> call, final Response<Forum> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(response.body());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                            getView().saveSuccess();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Error Saving Forum", error);
                            getView().showMessage("Error Saving Forum");
                        }
                    });
                } else {
                    try {
                        getView().showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: Error parsing error body", e);
                        getView().showMessage(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<Forum> call, Throwable t) {
                Log.e(TAG, "onFailure: Save Forum API Failed", t);
                getView().stopLoading();
                getView().showMessage("Error Saving Forum");
            }
        });
    }

}
