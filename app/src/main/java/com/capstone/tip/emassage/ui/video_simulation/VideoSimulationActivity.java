package com.capstone.tip.emassage.ui.video_simulation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityVideoSimulationBinding;
import com.capstone.tip.emassage.model.data.Video;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;

public class VideoSimulationActivity
        extends MvpViewStateActivity<VideoSimulationView, VideoSimulationPresenter>
        implements VideoSimulationView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityVideoSimulationBinding binding;
    private VideoSimulationListAdapter adapter;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_simulation);
        getSupportActionBar().setTitle("Video Simulations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new VideoSimulationListAdapter(getMvpView());

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        presenter.onStart();
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
    public VideoSimulationPresenter createPresenter() {
        return new VideoSimulationPresenter();
    }

    @NonNull
    @Override
    public ViewState<VideoSimulationView> createViewState() {
        setRetainInstance(true);
        return new VideoSimulationViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh();
    }

    @Override
    public void setVideoList(List<Video> videos) {
        adapter.setVideoList(videos);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoClicked(Video video) {
        try {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, Constants.DEVELOPER_KEY, video.getCode());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "No YouTube Player Found", Toast.LENGTH_SHORT).show();
        }
    }
}
