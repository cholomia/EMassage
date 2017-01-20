package com.capstone.tip.emassage.ui.category;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityCategoryBinding;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Course;
import com.capstone.tip.emassage.ui.lessons.LessonsActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class CategoryActivity extends MvpViewStateActivity<CategoryView, CategoryPresenter>
        implements CategoryView {

    private ActivityCategoryBinding binding;
    private Realm realm;
    private Course course;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        realm = Realm.getDefaultInstance();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent ID Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        adapter = new CategoryListAdapter(getMvpView());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        course = realm.where(Course.class).equalTo(Constants.ID, id).findFirstAsync();
        course.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (course.isLoaded() && course.isValid()) {
                    binding.layoutEmpty.setVisibility(course.getCategories().size() > 0 ?
                            View.GONE : View.VISIBLE);
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setSubtitle("for " + course.getTitle());
                    adapter.setCategories(realm.copyFromRealm(course.getCategories()));
                }
            }
        });

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
    protected void onDestroy() {
        course.removeChangeListeners();
        realm.close();
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

    /***
     * End of CategoryView
     ***/
}
