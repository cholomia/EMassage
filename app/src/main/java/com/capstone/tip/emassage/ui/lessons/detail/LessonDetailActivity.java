package com.capstone.tip.emassage.ui.lessons.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityLessonDetailBinding;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.model.data.LessonDetail;
import com.capstone.tip.emassage.ui.category.CategoryActivity;
import com.capstone.tip.emassage.ui.quiz.QuizActivity;
import com.capstone.tip.emassage.ui.video_simulation.OnlineVideoActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.Locale;

public class LessonDetailActivity extends MvpActivity<LessonDetailView, LessonDetailPresenter>
        implements LessonDetailView, TextToSpeech.OnInitListener {

    private ActivityLessonDetailBinding binding;
    private TextToSpeech textToSpeech;
    private int currentIndex;
    private Lesson lesson;
    private int lessonDetailId;

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
        lessonDetailId = getIntent().getIntExtra("lesson_detail", -1);
        textToSpeech = new TextToSpeech(this, this);

        presenter.onStart(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lesson_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_topic_menu:
                onMenu();
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
        this.lesson = lesson;
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(lesson.getTitle());
        if (lessonDetailId == -1) {
            currentIndex = 0;
            changeFragment(LessonSummaryFragment.newInstance(lesson.getObjective(), lesson.getSummary()),
                    LessonSummaryFragment.class.getSimpleName(),
                    false);
        } else {
            for (int i = 0; i < lesson.getLessonDetails().size(); i++) {
                LessonDetail lessonDetail = lesson.getLessonDetails().sort("sequence").get(i);
                if (lessonDetail.getId() == lessonDetailId) {
                    currentIndex = i + 1;
                    setLessonDetailFragment();
                }
            }
        }
        binding.btnViewVideo.setVisibility(lesson.getVideo() != null && !lesson.getVideo().isEmpty() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onPrevious() {
        if (currentIndex == 0) {
            onBackPressed();
        } else {
            currentIndex--;
            if (currentIndex == 0) {
                changeFragment(LessonSummaryFragment.newInstance(lesson.getObjective(), lesson.getSummary()),
                        LessonSummaryFragment.class.getSimpleName(),
                        false);
            } else {
                setLessonDetailFragment();
            }
        }
    }

    @Override
    public void onNext() {
        if (currentIndex == lesson.getLessonDetails().size()) {
            // index 0 is objcetive/summary
            if (presenter.hasTakenQuiz()) {
                new AlertDialog.Builder(this)
                        .setTitle("View Next Lesson?")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presenter.getNextLesson(lesson.getId());
                            }
                        })
                        .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LessonDetailActivity.this.finish();
                            }
                        })
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Quiz Not Yet Taken")
                        .setCancelable(false)
                        .setPositiveButton("Close", null)
                        .show();
            }
        } else {
            currentIndex++;
            setLessonDetailFragment();
        }
    }

    private void setLessonDetailFragment() {
        changeFragment(LessonBodyFragment.newInstance(
                lesson.getLessonDetails().sort("sequence").get(currentIndex - 1).getTitle(),
                lesson.getLessonDetails().sort("sequence").get(currentIndex - 1).getBody()),
                LessonBodyFragment.class.getSimpleName() + (currentIndex - 1), false);
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
    public void onMenu() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onTakeQuiz() {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

    @Override
    public void onViewVideo() {
        Intent intent = new Intent(this, OnlineVideoActivity.class);
        intent.putExtra("video_url", lesson.getVideo());
        startActivity(intent);
    }

    @Override
    public void setNextLesson(Lesson nextLesson) {
        Intent intent = new Intent(this, LessonDetailActivity.class);
        intent.putExtra(Constants.ID, nextLesson.getId());
        startActivity(intent);
    }

    @Override
    public void setNoNextLesson() {
        new AlertDialog.Builder(this)
                .setTitle("No Next Lesson")
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LessonDetailActivity.this.finish();
                    }
                })
                .show();
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

    public void changeFragment(Fragment fragment, String tag, boolean addToBackStack) {
        //binding.btnViewVideo.setVisibility(currentIndex == 0 ? View.GONE : View.VISIBLE);
        binding.btnTakeQuiz.setVisibility(currentIndex == lesson.getLessonDetails().size() && lesson.getQuestions().size() > 0 ? View.VISIBLE : View.GONE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }


}
