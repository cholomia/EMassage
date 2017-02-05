package com.capstone.tip.emassage.ui.announcement.list;

import com.capstone.tip.emassage.model.data.Announcement;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

public interface AnnouncementListView extends MvpView {
    void setAnnouncements(List<Announcement> announcements);

    void stopLoading();

    void showMessage(String message);

    void onAnnouncementClicked(Announcement announcement);
}
