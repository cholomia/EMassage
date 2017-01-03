package com.capstone.tip.emassage.ui.comments;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.response.CommentListResponse;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

public class CommentsPresenter extends MvpNullObjectBasePresenter<CommentsView> {
    private static final String TAG = CommentsPresenter.class.getSimpleName();
    private Realm realm;
    private RealmResults<Comment> commentRealmResults;

    public void onStart(int forumId) {
        realm = Realm.getDefaultInstance();
        Log.d(TAG, "onStart: Forum ID: " + forumId);
        commentRealmResults = realm.where(Comment.class).equalTo("forum", forumId).findAllSortedAsync("created", Sort.DESCENDING);
        commentRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Comment>>() {
            @Override
            public void onChange(RealmResults<Comment> element) {
                Log.d(TAG, "onChange: comment list");
                if (commentRealmResults.isLoaded() && commentRealmResults.isValid())
                    getView().setComments(realm.copyFromRealm(commentRealmResults));
            }
        });
    }

    public void onStop() {
        commentRealmResults.removeChangeListeners();
        realm.close();
    }

    public void loadCommentList() {
        comments(App.getInstance().getApiInterface().comments());
    }

    public void loadCommentList(Map<String, String> parameters) {
        comments(App.getInstance().getApiInterface().comments(parameters));
    }

    private void comments(Call<CommentListResponse> commentListResponseCall) {
        commentListResponseCall.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, final Response<CommentListResponse> response) {
                getView().stopLoading();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (response.body().getPrevious() == null
                                    || response.body().getPrevious().isEmpty())
                                realm.delete(Comment.class);
                            realm.insertOrUpdate(response.body().getResults());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Error Saving Comment List", error);
                            getView().showMessage("Error Saving Comment List");
                        }
                    });
                    if (response.body().getCount() <= 0)
                        getView().showMessage("No Comment Retrieved");
                    getView().addNext(response.body().getNext());
                } else {
                    try {
                        getView().showMessage(response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse: Error parsing error body", e);
                        getView().showMessage(response.message() != null ? response.message()
                                : "Unknown Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: Comment List Failed", t);
                getView().stopLoading();
                getView().showMessage("Error Retrieving Comment List");
            }
        });
    }
}