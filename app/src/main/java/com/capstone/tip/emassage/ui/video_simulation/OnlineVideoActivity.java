package com.capstone.tip.emassage.ui.video_simulation;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Endpoints;
import com.capstone.tip.emassage.databinding.ActivityOnlineVideoBinding;
import com.danikula.videocache.HttpProxyCacheServer;

public class OnlineVideoActivity extends AppCompatActivity {

    private static final String TAG = OnlineVideoActivity.class.getSimpleName();
    private static final String POSITION = "video_current_position";
    private ActivityOnlineVideoBinding binding;
    private MediaController mediaControls;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_online_video);

        String videoUrl = getIntent().getStringExtra("video_url");
        if (videoUrl == null) {
            Toast.makeText(getApplicationContext(), "No Video URL Intent Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) binding.videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        binding.videoView.setLayoutParams(params);

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        try {
            HttpProxyCacheServer proxy = App.getProxy(this);
            Log.d(TAG, "onCreate: video url: " + Endpoints.BASE_URL + videoUrl);
            String proxyUrl = proxy.getProxyUrl(Endpoints.BASE_URL + videoUrl);
            Log.d(TAG, "onCreate: proxy url: " + proxyUrl);
            binding.videoView.setMediaController(mediaControls);
            binding.videoView.setVideoURI(Uri.parse(proxyUrl));
            //binding.videoView.start();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error setting video", e);
        }

        binding.videoView.requestFocus();
        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                binding.videoView.seekTo(position);
                if (position == 0) {
                    binding.videoView.start();
                } else {
                    binding.videoView.pause();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        binding.videoView.stopPlayback();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, binding.videoView.getCurrentPosition());
        binding.videoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt(POSITION);
        binding.videoView.seekTo(position);
    }
}
