package com.capstone.tip.emassage.ui.forums;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.capstone.tip.emassage.R;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

public class ForumsActivity extends MvpViewStateActivity<ForumsView, ForumsPresenter>
        implements ForumsView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setContentView(R.layout.activity_forums);
    }

    @NonNull
    @Override
    public ForumsPresenter createPresenter() {
        return new ForumsPresenter();
    }

    @NonNull
    @Override
    public ViewState<ForumsView> createViewState() {
        setRetainInstance(true);
        return new ForumsViewState();
    }

    @Override
    public void onNewViewStateInstance() {

    }
}
