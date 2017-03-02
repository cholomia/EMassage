package com.capstone.tip.emassage.ui.gallery.display;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityGalleryDisplayBinding;
import com.capstone.tip.emassage.model.data.Gallery;
import com.capstone.tip.emassage.ui.gallery.list.GalleryListActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

public class GalleryDisplayActivity extends MvpActivity<GalleryDisplayView, GalleryDisplayPresenter>
        implements GalleryDisplayView {

    private ActivityGalleryDisplayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_display);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setView(getMvpView());
        int id = getIntent().getIntExtra(Constants.ID, -1);
        if (id == -1) {
            Toast.makeText(this, "No Intent Extra Found", Toast.LENGTH_SHORT).show();
            finish();
        }
        presenter.start(id);
    }

    @Override
    protected void onDestroy() {
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
    public GalleryDisplayPresenter createPresenter() {
        return new GalleryDisplayPresenter();
    }

    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public void onMenu() {
        Intent intent = new Intent(this, GalleryListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    @Override
    public void setGallery(Gallery gallery) {
        binding.setGallery(gallery);
        Glide.with(this)
                .load(gallery.getImage())
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.touchImageView);
    }
}
