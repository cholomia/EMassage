package com.capstone.tip.emassage.ui.lessons;

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
import com.capstone.tip.emassage.databinding.ActivityLessonsBinding;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.ui.lessons.detail.LessonDetailActivity;
import com.capstone.tip.emassage.ui.pdf.PdfActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class LessonsActivity extends MvpViewStateActivity<LessonsView, LessonsPresenter>
        implements LessonsView {

    private ActivityLessonsBinding binding;
    private Realm realm;
    private Category category;
    private LessonListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        realm = Realm.getDefaultInstance();

        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lessons);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new LessonListAdapter(getMvpView());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        category = realm.where(Category.class).equalTo(Constants.ID, id).findFirstAsync();
        category.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (category.isLoaded() && category.isValid()) {
                    binding.layoutEmpty.setVisibility(category.getLessons().size() > 0 ?
                            View.GONE : View.VISIBLE);
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setSubtitle(category.getTitle());
                    adapter.setLessons(realm.copyFromRealm(category.getLessons()));
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
        category.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public LessonsPresenter createPresenter() {
        return new LessonsPresenter();
    }

    @NonNull
    @Override
    public ViewState<LessonsView> createViewState() {
        setRetainInstance(true);
        return new LessonsViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of LessonsView
     ***/

    @Override
    public void onLessonsItemClicked(Lesson lesson) {
        Intent intent = new Intent(this, LessonDetailActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

    /***
     * End of LessonsView
     ***/

}
