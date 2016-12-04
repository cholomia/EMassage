package com.capstone.tip.emassage.ui.courses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityCoursesBinding;
import com.capstone.tip.emassage.model.data.Course;
import com.capstone.tip.emassage.ui.category.CategoryActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CoursesActivity extends MvpViewStateActivity<CoursesView, CoursesPresenter>
        implements CoursesView {

    private ActivityCoursesBinding binding;
    private Realm realm;
    private RealmResults<Course> courseRealmResults;
    private CoursesListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_courses);
        binding.setView(getMvpView());

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CoursesListAdapter(getMvpView());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);

        courseRealmResults = realm.where(Course.class).findAllSortedAsync(Constants.COL_SEQ);
        courseRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Course>>() {
            @Override
            public void onChange(RealmResults<Course> element) {
                if (courseRealmResults.isLoaded() && courseRealmResults.isValid()) {
                    adapter.setCourses(realm.copyFromRealm(courseRealmResults));
                    binding.layoutEmpty.setVisibility(courseRealmResults.size() > 0
                            ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_courses, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                onRefresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        courseRealmResults.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public CoursesPresenter createPresenter() {
        return new CoursesPresenter();
    }

    @NonNull
    @Override
    public ViewState<CoursesView> createViewState() {
        setRetainInstance(true);
        return new CoursesViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of CoursesView
     ***/

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Retrieving Courses...");
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCourseItemClicked(Course course) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(Constants.ID, course.getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    /***
     * End of CoursesView
     ***/

}
