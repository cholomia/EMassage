package com.capstone.tip.emassage.ui.forums;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.databinding.ActivityForumsBinding;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.ui.forums.details.ForumDetailActivity;
import com.capstone.tip.emassage.ui.forums.form.ForumFormActivity;
import com.capstone.tip.emassage.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.List;
import java.util.Map;

public class ForumsActivity extends MvpViewStateActivity<ForumsView, ForumsPresenter>
        implements ForumsView, SwipeRefreshLayout.OnRefreshListener {

    private ActivityForumsBinding binding;
    private ForumsListAdapter forumsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forums);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setView(getMvpView());

        forumsListAdapter = new ForumsListAdapter(getMvpView());

        binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        binding.recyclerView.setAdapter(forumsListAdapter);
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
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                onRefresh();
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
        presenter.onStop();
        super.onDestroy();
    }

    @Override
    public void onAddClicked() {
        startActivity(new Intent(this, ForumFormActivity.class));
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
        forumsListAdapter.setLoading(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addNext(String nextUrl) {
        forumsListAdapter.setNextUrl(nextUrl);
    }

    @Override
    public void onForumClicked(Forum forum) {
        Intent intent = new Intent(this, ForumDetailActivity.class);
        intent.putExtra(Constants.ID, forum.getId());
        startActivity(intent);
    }

    @Override
    public void setForums(List<Forum> forumList) {
        forumsListAdapter.setForums(forumList);
    }

    @Override
    public void onRefresh() {
        presenter.loadForumList();
    }

    @Override
    public void onMore(final String nextUrl) {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                forumsListAdapter.setLoading(true);
                Map<String, String> parameters = StringUtils.getParamsFromUrl(nextUrl);
                if (parameters.size() > 0) presenter.loadForumList(parameters);
                else presenter.loadForumList();
            }
        });
    }
}
