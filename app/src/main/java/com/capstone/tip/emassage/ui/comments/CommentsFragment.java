package com.capstone.tip.emassage.ui.comments;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.capstone.tip.emassage.R;
import com.capstone.tip.emassage.databinding.FragmentCommentsBinding;
import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.ui.forums.details.ForumDetailView;
import com.capstone.tip.emassage.utils.StringUtils;
import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommentsFragment extends MvpViewStateFragment<CommentsView, CommentsPresenter>
        implements CommentsView, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_FORUM_ID = "forum_id";
    private static final String TAG = CommentsFragment.class.getSimpleName();

    private int forumId;
    private FragmentCommentsBinding binding;
    private CommentsListAdapter adapter;
    private ProgressDialog progressDialog;
    private ForumDetailView forumDetailView;

    public static CommentsFragment newInstance(int forumId) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FORUM_ID, forumId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) forumId = getArguments().getInt(ARG_FORUM_ID, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false);
        binding.swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.swipe_refresh_layout_color_scheme));
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new CommentsListAdapter(getMvpView());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0)
                        ? 0 : recyclerView.getChildAt(0).getTop();
                binding.swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        binding.txtCommentHeader.setText("Comments");
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ForumDetailView) forumDetailView = (ForumDetailView) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        forumDetailView = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(forumId);
    }

    @Override
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @NonNull
    @Override
    public CommentsPresenter createPresenter() {
        return new CommentsPresenter();
    }

    @NonNull
    @Override
    public ViewState<CommentsView> createViewState() {
        return new CommentsViewState();
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
        Map<String, String> parameters = new HashMap<>();
        parameters.put("forum_id", forumId + "");
        if (parameters.size() > 0) presenter.loadCommentList(parameters);
        else presenter.loadCommentList();
    }

    @Override
    public void onMore(final String nextUrl) {
        binding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefreshLayout.setRefreshing(true);
                adapter.setLoading(true);
                Map<String, String> parameters = StringUtils.getParamsFromUrl(nextUrl);
                if (parameters.size() > 0) presenter.loadCommentList(parameters);
                else presenter.loadCommentList();
            }
        });
    }

    @Override
    public void setComments(List<Comment> comments) {
        Log.d(TAG, "setComments: ");
        adapter.setComments(comments);
    }

    @Override
    public void stopLoading() {
        binding.swipeRefreshLayout.setRefreshing(false);
        adapter.setLoading(false);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addNext(String nextUrl) {
        adapter.setNextUrl(nextUrl);
    }

    @Override
    public void onMoreOptions(View view, final Comment comment) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.menu_comment_options);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        forumDetailView.onEditComment(comment);
                        break;
                    case R.id.action_delete:
                        presenter.deleteComment(comment);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void setUser(User user) {
        adapter.setUser(user);
    }

    @Override
    public void startProgressLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Connecting...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void stopProgressLoading() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void onUpVote(Comment comment) {
        presenter.vote(comment.getId(), comment.getMyVote() == 1 ? 0 : 1);
    }

    @Override
    public void onDownVote(Comment comment) {
        presenter.vote(comment.getId(), comment.getMyVote() == -1 ? 0 : -1);
    }
}
