package com.capstone.tip.emassage.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityMenuBinding;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.ui.announcement.list.AnnouncementListActivity;
import com.capstone.tip.emassage.ui.category.CategoryActivity;
import com.capstone.tip.emassage.ui.fake_grades.FakeGradesActivity;
import com.capstone.tip.emassage.ui.forums.ForumsActivity;
import com.capstone.tip.emassage.ui.gallery.list.GalleryListActivity;
import com.capstone.tip.emassage.ui.grades.GradesActivity;
import com.capstone.tip.emassage.ui.login.LoginActivity;
import com.capstone.tip.emassage.ui.text_twist.TextTwistActivity;
import com.capstone.tip.emassage.ui.video_simulation.VideoSimulationActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import io.realm.Realm;

public class MenuActivity extends MvpActivity<MenuView, MenuPresenter> implements MenuView {

    private static final String TAG = MenuActivity.class.getSimpleName();
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        binding.setView(getMvpView());
        presenter.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                onLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public MenuPresenter createPresenter() {
        return new MenuPresenter();
    }

    @Override
    public void onViewCourses() {
        startActivity(new Intent(this, CategoryActivity.class));
    }

    @Override
    public void onViewForums() {
        startActivity(new Intent(this, ForumsActivity.class));
    }

    @Override
    public void onViewGrades() {
        startActivity(new Intent(this, FakeGradesActivity.class));
    }

    @Override
    public void onViewVideos() {
        startActivity(new Intent(this, VideoSimulationActivity.class));
    }

    @Override
    public void onViewMiniGame() {
        startActivity(new Intent(this, TextTwistActivity.class));
    }

    @Override
    public void onLogout() {
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                // TODO: 12/4/2016 add flag to clear all task
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                MenuActivity.this.finish();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                Log.e(TAG, "onError: Error Logging out (deleting all data)", error);
            }
        });
    }

    @Override
    public void onSetUser(User user) {
        binding.setUser(user);
    }

    @Override
    public void onViewAnnouncement() {
        startActivity(new Intent(this, AnnouncementListActivity.class));
    }

    @Override
    public void onViewSummaryAssessment() {
        startActivity(new Intent(this, GradesActivity.class));
    }

    @Override
    public void onViewGallery() {
        startActivity(new Intent(this, GalleryListActivity.class));
    }

    @Override
    public void onViewConsole() {

    }
}
