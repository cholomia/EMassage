package com.capstone.tip.emassage.ui.announcement.detail;

import com.capstone.tip.emassage.model.data.Announcement;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Cholo Mia on 2/5/2017.
 */

@SuppressWarnings("WeakerAccess")
public interface AnnouncementDetailView extends MvpView {
    void setAnnouncement(Announcement announcement);
}
