package com.capstone.tip.emassage.ui.main;

import com.capstone.tip.emassage.model.data.User;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * @author pocholomia
 * @since 20/01/2017
 */

public interface MenuView extends MvpView {


    void onViewCourses();

    void onViewForums();

    void onViewGrades();

    void onViewVideos();

    void onLogout();

    void onSetUser(User user);
}
