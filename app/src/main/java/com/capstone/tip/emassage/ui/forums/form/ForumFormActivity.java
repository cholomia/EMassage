package com.capstone.tip.emassage.ui.forums.form;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityForumFormBinding;
import com.capstone.tip.emassage.model.data.Forum;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class ForumFormActivity extends MvpViewStateActivity<ForumFormView, ForumFormPresenter>
        implements ForumFormView {

    private ActivityForumFormBinding binding;
    private ProgressDialog progressDialog;
    private Forum forum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forum_form);
        binding.setView(getMvpView());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int id = getIntent().getIntExtra(Constants.ID, -1);
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
    public ForumFormPresenter createPresenter() {
        return new ForumFormPresenter();
    }

    @NonNull
    @Override
    public ViewState<ForumFormView> createViewState() {
        setRetainInstance(true);
        return new ForumFormViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public void onSave() {
        new AlertDialog.Builder(this)
                .setTitle("Save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.saveForum(binding.etTitle.getText().toString(),
                                binding.etContent.getText().toString(), forum);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void saveSuccess() {
        showMessage("Save Forum Successful");
        finish();
    }

    @Override
    public void setForum(Forum forum) {
        this.forum = forum;
        binding.setForum(forum);
    }
}
