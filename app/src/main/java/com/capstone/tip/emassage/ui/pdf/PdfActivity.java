package com.capstone.tip.emassage.ui.pdf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityPdfBinding;
import com.capstone.tip.emassage.model.data.Lesson;
import com.capstone.tip.emassage.ui.quiz.QuizActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;

public class PdfActivity extends MvpViewStateActivity<PdfView, PdfPresenter> implements PdfView {

    private Realm realm;
    private ActivityPdfBinding binding;
    private Lesson lesson;
    private ProgressDialog progressDialog;

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lesson = realm.where(Lesson.class).equalTo(Constants.ID, id).findFirstAsync();
        lesson.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (lesson.isLoaded() && lesson.isValid()) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pdf, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_quiz:
                if (lesson.isLoaded() && lesson.isValid()) {
                    Intent intent = new Intent(this, QuizActivity.class);
                    intent.putExtra(Constants.ID, lesson.getId());
                    startActivity(intent);
                } else {
                    showAlert("No Lesson Found");
                }
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
                .load();

    }

    /***
     * End of PdfView
     ***/
}
