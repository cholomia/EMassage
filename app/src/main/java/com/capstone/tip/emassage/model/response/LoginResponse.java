package com.capstone.tip.emassage.model.response;

import com.capstone.tip.emassage.model.data.User;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public class LoginResponse extends BasicResponse {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
