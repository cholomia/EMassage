package com.capstone.tip.emassage.ui.register;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.User;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {
    private static final String TAG = RegisterPresenter.class.getSimpleName();

    public void register(String username, String firstName, String lastName, String password,
                         String repeatPassword) {
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() ||
                repeatPassword.isEmpty()) {
            getView().showAlert("Fill-up all fields");
        } else if (!password.contentEquals(repeatPassword)) {
            getView().showAlert("Password does not match");
        } else {
            getView().startLoading();
            App.getInstance().getApiInterface().register(username, firstName, lastName, password)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            getView().stopLoading();
                            if (response.isSuccessful()) {
                                getView().onRegisterSuccessful();
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    getView().showAlert(errorBody);
                                } catch (IOException e) {
                                    Log.e(TAG, "onResponse: Error parsing error body as string", e);
                                    getView().showAlert(response.message() != null ?
                                            response.message() : "Unknown Exception");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e(TAG, "onFailure: Error calling register api", t);
                            getView().stopLoading();
                            getView().showAlert("Error Connecting to Server");
                        }
                    });
        }
    }
}
