package com.capstone.tip.emassage.ui.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface RegisterView extends MvpView {

    void onRegisterButtonClicked();

    void setEditTextValue(String username, String firstName, String lastName, String password, String repeatPassword);

    void showAlert(String message);

    void startLoading();

    void stopLoading();

    void onRegisterSuccessful();
}
