package com.capstone.tip.emassage.ui.category;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityCategoryBinding;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.ui.lessons.LessonsActivity;
import com.capstone.tip.emassage.ui.lessons.detail.LessonDetailActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;

public class CategoryActivity extends MvpViewStateActivity<CategoryView, CategoryPresenter>
        implements CategoryView {

    private ActivityCategoryBinding binding;
    //private CategoryListAdapter adapter;
    private CategoryAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);
        binding.setView(getMvpView());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        //binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        presenter.onStart();
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
        presenter.onStop();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public CategoryPresenter createPresenter() {
        return new CategoryPresenter();
    }

    @NonNull
    @Override
    public ViewState<CategoryView> createViewState() {
        setRetainInstance(true);
        return new CategoryViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of CategoryView
     ***/

    @Override
    public void onCategoryItemClicked(Category category) {
        Intent intent = new Intent(this, LessonsActivity.class);
        intent.putExtra(Constants.ID, category.getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
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
    public void onLessonsItemClicked(int id) {
        Intent intent = new Intent(this, LessonDetailActivity.class);
        intent.putExtra(Constants.ID, id);
        startActivity(intent);
    }

    @Override
    public void setLessonGroups(List<LessonGroup> lessonGroups) {
        binding.layoutEmpty.setVisibility(lessonGroups.size() > 0 ? View.GONE : View.VISIBLE);
        adapter = new CategoryAdapter(lessonGroups, getMvpView());
        binding.recyclerView.setAdapter(adapter);
    }

    /***
     * End of CategoryView
     ***/
}
