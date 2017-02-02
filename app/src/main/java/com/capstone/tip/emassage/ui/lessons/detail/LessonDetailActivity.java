package com.capstone.tip.emassage.ui.lessons.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityLessonDetailBinding;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.ui.quiz.QuizActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

public class LessonDetailActivity extends MvpActivity<LessonDetailView, LessonDetailPresenter> implements LessonDetailView {

    private ActivityLessonDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lesson_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        presenter.onStart(id);
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
        presenter.onStop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public LessonDetailPresenter createPresenter() {
        return new LessonDetailPresenter();
    }

    @Override
    public void setLesson(Lesson lesson) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(lesson.getTitle());
        binding.setLesson(lesson);
    }

    @Override
    public void onViewPdf(Lesson lesson) {
        Intent intent = new Intent(this, LessonDetailActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

    @Override
    public void onTakeQuiz(Lesson lesson) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

}
