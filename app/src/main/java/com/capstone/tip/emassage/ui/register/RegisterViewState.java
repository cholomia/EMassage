package com.capstone.tip.emassage.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.capstone.tip.emassage.app.Constants;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class RegisterViewState implements RestorableViewState<RegisterView> {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String repeatPassword;

    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putString(Constants.USERNAME, username);
        out.putString(Constants.FIRST_NAME, firstName);
        out.putString(Constants.LAST_NAME, lastName);
        out.putString(Constants.PASSWORD, password);
        out.putString(Constants.REPEAT_PASSWORD, repeatPassword);
    }

    @Override
    public RestorableViewState<RegisterView> restoreInstanceState(Bundle in) {
        username = in.getString(Constants.USERNAME, "");
        firstName = in.getString(Constants.FIRST_NAME, "");
        lastName = in.getString(Constants.LAST_NAME, "");
        password = in.getString(Constants.PASSWORD, "");
        repeatPassword = in.getString(Constants.REPEAT_PASSWORD, "");
        return this;
    }

    @Override
    public void apply(RegisterView view, boolean retained) {
        view.setEditTextValue(username, firstName, lastName, password, repeatPassword);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
