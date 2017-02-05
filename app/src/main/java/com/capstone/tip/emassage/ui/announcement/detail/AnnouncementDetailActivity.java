package com.capstone.tip.emassage.ui.announcement.detail;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityAnnouncementDetailBinding;
import com.capstone.tip.emassage.model.data.Announcement;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

public class AnnouncementDetailActivity
        extends MvpActivity<AnnouncementDetailView, AnnouncementDetailPresenter>
        implements AnnouncementDetailView {

    private ActivityAnnouncementDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_announcement_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        presenter.start(id);
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public AnnouncementDetailPresenter createPresenter() {
        return new AnnouncementDetailPresenter();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setAnnouncement(Announcement announcement) {
        binding.setAnnouncement(announcement);
    }
}
