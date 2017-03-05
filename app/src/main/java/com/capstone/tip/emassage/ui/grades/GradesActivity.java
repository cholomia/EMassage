package com.capstone.tip.emassage.ui.grades;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityGradesBinding;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.pojo.DisplayGrade;
import com.capstone.tip.emassage.model.pojo.LessonGroup;
import com.capstone.tip.emassage.ui.category.CategoryAdapter;
import com.capstone.tip.emassage.ui.grades.detail.GradesDetailActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;

public class GradesActivity extends MvpViewStateActivity<GradesView, GradesPresenter>
        implements GradesView {

    private GradesListAdapter adapter;
    private ProgressDialog progressDialog;
    private ActivityGradesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_grades);

        // assumes that theme has toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        adapter = new GradesListAdapter(getMvpView());


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        presenter.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grades, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                new AlertDialog.Builder(this)
                        .setTitle("Alert!!!")
                        .setMessage("Quiz Grades that is not yet saved online will be deleted!")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presenter.refreshGrades();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

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
    public GradesPresenter createPresenter() {
        return new GradesPresenter();
    }

    @NonNull
    @Override
    public ViewState<GradesView> createViewState() {
        return new GradesViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public void setDisplayGradeList(List<DisplayGrade> displayGrades) {
        adapter.setDisplayGrades(displayGrades);
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing()) progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGradeSave(List<Grade> grades) {
        for (Grade grade : grades) if (!grade.isSaved()) presenter.saveGrade(grade);
    }

    @Override
    public void setLessonGroupList(List<LessonGroup> lessonGroups) {
        CategoryAdapter categoryAdapter = new CategoryAdapter(lessonGroups, getMvpView());
        binding.recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onLessonsItemClicked(int id) {
        Intent intent = new Intent(this, GradesDetailActivity.class);
        intent.putExtra("lesson", id);
        startActivity(intent);
    }
}
