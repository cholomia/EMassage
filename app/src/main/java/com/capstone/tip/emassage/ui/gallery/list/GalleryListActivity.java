package com.capstone.tip.emassage.ui.gallery.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityGalleryListBinding;
import com.capstone.tip.emassage.model.data.Category;
import com.capstone.tip.emassage.ui.gallery.detail.GalleryDetailActivity;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.List;

public class GalleryListActivity extends MvpActivity<GalleryListView, GalleryListPresenter> implements GalleryListView {

    private ActivityGalleryListBinding binding;
    private GalleryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_list);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new GalleryListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    @NonNull
    @Override
    public GalleryListPresenter createPresenter() {
        return new GalleryListPresenter();
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
    public void setCategories(List<Category> categories) {
        adapter.setCategories(categories);
        if (categories.size() <= 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No Topics Found")
                    .setMessage("Go to Lessons and Refresh")
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GalleryListActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onCategoryClicked(Category category) {
        Intent intent = new Intent(this, GalleryDetailActivity.class);
        intent.putExtra(Constants.ID, category.getId());
        startActivity(intent);
    }
}
