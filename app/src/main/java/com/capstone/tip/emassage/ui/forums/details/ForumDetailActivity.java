package com.capstone.tip.emassage.ui.forums.details;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityForumDetailBinding;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.ui.comments.CommentsFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class ForumDetailActivity extends MvpViewStateActivity<ForumDetailView, ForumDetailPresenter>
        implements ForumDetailView {

    private static final String TAG = ForumDetailActivity.class.getSimpleName();
    private ActivityForumDetailBinding binding;
    private ProgressDialog progressDialog;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forum_detail);
        binding.setView(getMvpView());
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(getApplicationContext(), "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        fragmentManager = getSupportFragmentManager();
        changeFragment(CommentsFragment.newInstance(id), CommentsFragment.class.getSimpleName() + id, false);
        presenter.onStart(id);

    }

    public void changeFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_comments, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        presenter.onStop();
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
    public ForumDetailPresenter createPresenter() {
        return new ForumDetailPresenter();
    }


    @NonNull
    @Override
    public ViewState<ForumDetailView> createViewState() {
        setRetainInstance(true);
        return new ForumDetailViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }

    @Override
    public void setForum(Forum forum) {
        if (forum.getCreated() == null) {
            Log.d(TAG, "setForum: Date Created is Null");
        }
        binding.setForum(forum);
    }

    @Override
    public void onSend(Forum forum) {
        presenter.sendComment(forum, binding.etComment.getText().toString());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Sending comment...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

}
