package com.capstone.tip.emassage.ui.login;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.response.LoginResponse;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/3/2016.
 *
 * @see https://github.com/realm/realm-java/tree/master/examples/encryptionExample
 */

public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    public void login(String username, final String password) {
        if (username.isEmpty()) {
            getView().showAlert("Enter Username");
        } else if (password.isEmpty()) {
            getView().showAlert("Enter Password");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().login(username, password)
                    .enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               final Response<LoginResponse> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess()) {
                                    final Realm realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            User user = response.body().getUser();
                                            user.setPassword(password);
                                            // TODO: 12/4/2016 Add Encryption (see url above)
                                            realm.copyToRealmOrUpdate(user);
                                        }
                                    }, new Realm.Transaction.OnSuccess() {
                                        @Override
                                        public void onSuccess() {
                                            realm.close();
                                            getView().onLoginSuccess();
                                        }
                                    }, new Realm.Transaction.OnError() {
                                        @Override
                                        public void onError(Throwable error) {
                                            realm.close();
                                            Log.e(TAG, "onError: Unable to save USER", error);
                                            getView().showAlert("Error Saving API Response");
                                        }
                                    });
                                } else {
                                    getView().showAlert(response.body().getMessage());
                                }
                            } else {
                                getView().showAlert(response.message() != null ? response.message()
                                        : "Unknown Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling login api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }
    }
}
