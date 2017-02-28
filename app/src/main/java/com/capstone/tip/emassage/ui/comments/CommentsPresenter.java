package com.capstone.tip.emassage.ui.comments;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.response.CommentListResponse;
import com.capstone.tip.emassage.model.response.CommentVote;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
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
    private User user;

    public void onStart(int forumId) {
        realm = Realm.getDefaultInstance();
        Log.d(TAG, "onStart: Forum ID: " + forumId);
        user = realm.where(User.class).findFirst();
        getView().setUser(user);
        commentRealmResults = realm.where(Comment.class).equalTo("forum", forumId).findAllSortedAsync("points", Sort.DESCENDING);
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
        comments(App.getInstance().getApiInterface()
                .comments(Credentials.basic(user.getUsername(), user.getPassword())));
    }

    public void loadCommentList(Map<String, String> parameters) {
        comments(App.getInstance().getApiInterface()
                .comments(Credentials.basic(user.getUsername(), user.getPassword()), parameters));
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


    public void deleteComment(final Comment comment) {
        getView().startProgressLoading();
        App.getInstance().getApiInterface().deleteComment(comment.getId(),
                Credentials.basic(user.getUsername(), user.getPassword()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        getView().stopProgressLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Comment mComment = realm.where(Comment.class)
                                            .equalTo(Constants.ID, comment.getId()).findFirst();
                                    mComment.deleteFromRealm();
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
                                    Log.e(TAG, "onError: Error Deleting Comment", error);
                                    getView().showMessage("Error Deleting Comment");
                                }
                            });
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: Comment Delete Failed", t);
                        getView().stopProgressLoading();
                        getView().showMessage("Error Deleting Comment");
                    }
                });
    }

    public void vote(int commentId, int vote) {
        getView().startProgressLoading();
        App.getInstance().getApiInterface().commentVote("c-" + commentId + "-" + user.getUsername(),
                Credentials.basic(user.getUsername(), user.getPassword()), commentId, vote)
                .enqueue(new Callback<CommentVote>() {
                    @Override
                    public void onResponse(Call<CommentVote> call, final Response<CommentVote> response) {
                        getView().stopProgressLoading();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(response.body().getComment());
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
                                    Log.e(TAG, "onError: Error Vote Comment", error);
                                    getView().showMessage("Error Vote Comment");
                                }
                            });
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
                    public void onFailure(Call<CommentVote> call, Throwable t) {
                        Log.e(TAG, "onFailure: Comment Vote Failed", t);
                        getView().stopProgressLoading();
                        getView().showMessage("Error Vote Comment");
                    }
                });
    }
}
