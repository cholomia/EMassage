package com.capstone.tip.emassage.ui.lessons.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityLessonDetailBinding;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.ui.pdf.PdfActivity;
import com.capstone.tip.emassage.ui.quiz.QuizActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.Locale;

public class LessonDetailActivity extends MvpActivity<LessonDetailView, LessonDetailPresenter> implements LessonDetailView, TextToSpeech.OnInitListener {

    private ActivityLessonDetailBinding binding;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lesson_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setView(getMvpView());
        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found!", Toast.LENGTH_SHORT).show();
            finish();
        }
        textToSpeech = new TextToSpeech(this, this);

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
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
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
        Intent intent = new Intent(this, PdfActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

    @Override
    public void onTakeQuiz(Lesson lesson) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

    @Override
    public void onPopTextToSpeech(View view, String stringToTextToSpeech) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(stringToTextToSpeech, TextToSpeech.QUEUE_FLUSH, null, stringToTextToSpeech);
        } else {
            //noinspection deprecation
            textToSpeech.speak(stringToTextToSpeech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
                Snackbar.make(binding.getRoot(), "The TextToSpeech Language is not Supported",
                        Snackbar.LENGTH_SHORT).show();
            }

        } else {
            Snackbar.make(binding.getRoot(), "TextToSpeech Initialization Failed!",
                    Snackbar.LENGTH_SHORT).show();
            Log.e("TTS", "Initialization Failed!");
        }
    }
}
