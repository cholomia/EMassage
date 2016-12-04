package com.capstone.tip.emassage.model.data;

import com.capstone.tip.emassage.app.Constants;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class User extends RealmObject {

    @PrimaryKey
    private String username;
    @SerializedName(Constants.FIRST_NAME)
    private String firstName;
    @SerializedName(Constants.LAST_NAME)
    private String lastName;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
