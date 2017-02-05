package com.capstone.tip.emassage.ui.fake_grades;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.ActivityFakeGradesBinding;
import com.capstone.tip.emassage.model.pojo.FakeGrade;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

public class FakeGradesActivity extends MvpActivity<FakeGradesView, FakeGradesPresenter>
        implements FakeGradesView {

    private ActivityFakeGradesBinding binding;
    private FakeGradesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fake_grades);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new FakeGradesAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
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

    @NonNull
    @Override
    public FakeGradesPresenter createPresenter() {
        return new FakeGradesPresenter();
    }

    @Override
    public void setFakeGrades(List<FakeGrade> fakeGrades) {
        adapter.setFakeGrades(fakeGrades);
    }
}
