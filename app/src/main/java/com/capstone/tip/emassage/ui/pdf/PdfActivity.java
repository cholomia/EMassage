package com.capstone.tip.emassage.ui.pdf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityPdfBinding;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.ui.category.CategoryActivity;
import com.capstone.tip.emassage.ui.lessons.detail.LessonDetailActivity;
import com.capstone.tip.emassage.ui.quiz.QuizActivity;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class PdfActivity extends MvpViewStateActivity<PdfView, PdfPresenter>
        implements PdfView, OnPageScrollListener, OnPageChangeListener {

    private Realm realm;
    private ActivityPdfBinding binding;
    private Lesson lesson;
    private ProgressDialog progressDialog;
    private int page;

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

        page = getIntent().getIntExtra("page", -1);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setView(getMvpView());
        lesson = realm.where(Lesson.class).equalTo(Constants.ID, id).findFirstAsync();
        lesson.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (lesson.isLoaded() && lesson.isValid()) {
                    binding.setLesson(lesson);
                    if (getSupportActionBar() != null)
                        getSupportActionBar().setTitle(lesson.getTitle());
                    String pdf = lesson.getPdf().replace("/media/", "");
                    File file = getBaseContext().getFileStreamPath(pdf);
                    if (file.exists()) {
                        loadPdfLocal();
                    } else {
                        presenter.loadPdf(lesson.getPdf());
                    }
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
        lesson.removeChangeListeners();
        realm.close();
        super.onDestroy();
    }

    /***
     * Start of MvpViewStateActivity
     ***/

    @NonNull
    @Override
    public PdfPresenter createPresenter() {
        return new PdfPresenter();
    }

    @NonNull
    @Override
    public ViewState<PdfView> createViewState() {
        setRetainInstance(true);
        return new PdfViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    /***
     * End of MvpViewStateActivity
     ***/

    /***
     * Start of PdfView
     ***/

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading PDF...");
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
    public void loadPdfLocal() {
        File file = getBaseContext().getFileStreamPath(lesson.getPdf().replace("/media/", ""));
        binding.pdfView.fromFile(file)
                .enableSwipe(true)
                .enableDoubletap(true)
                .onPageScroll(this)
                .onPageChange(this)
                .load();
        if (page != -1) {
            Toast.makeText(this, "Page# " + page, Toast.LENGTH_SHORT).show();
            binding.pdfView.jumpTo(page + 1);
        }
    }

    @Override
    public void onTakeQuiz(Lesson lesson) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(Constants.ID, lesson.getId());
        startActivity(intent);
    }

    @Override
    public void onViewVideo(Lesson lesson) {
       /* try {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, Constants.DEVELOPER_KEY, lesson.getYoutubeCode());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "No YouTube Player Found", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onBack() {
        if (binding.pdfView.getCurrentPage() == 0) {
            onBackPressed();
        } else {
            binding.pdfView.jumpTo(binding.pdfView.getCurrentPage() - 1);
        }
    }

    @Override
    public void onMenu() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNext() {
        if (binding.pdfView.getCurrentPage() == binding.pdfView.getPageCount() - 1) {
            Intent intent = new Intent(this, LessonDetailActivity.class);
            intent.putExtra(Constants.ID, lesson.getId() + 1);
            startActivity(intent);
        } else {
            binding.pdfView.jumpTo(binding.pdfView.getCurrentPage() + 1);
        }
    }

    @Override
    public void onPageScrolled(int page, float positionOffset) {
        binding.btnTakeQuiz.setVisibility(page == binding.pdfView.getPageCount() - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        binding.btnTakeQuiz.setVisibility(page == pageCount - 1 ? View.VISIBLE : View.GONE);
    }

    /***
     * End of PdfView
     ***/
}
