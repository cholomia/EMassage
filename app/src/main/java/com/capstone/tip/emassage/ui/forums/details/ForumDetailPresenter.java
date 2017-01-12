package com.capstone.tip.emassage.ui.forums.details;

import android.util.Log;

import com.capstone.tip.emassage.app.App;
import com.capstone.tip.emassage.app.Constants;
import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.data.User;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cholo Mia on 12/27/2016.
 */

class ForumDetailPresenter extends MvpNullObjectBasePresenter<ForumDetailView> {
    private static final String TAG = ForumDetailPresenter.class.getSimpleName();
    private Realm realm;
    private Forum forum;
    private User user;

    void onStart(int id) {
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();
        forum = realm.where(Forum.class).equalTo(Constants.ID, id).findFirstAsync();
        forum.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel element) {
                if (forum.isLoaded() && forum.isValid())
                    getView().setForum(realm.copyFromRealm(forum));
            }
        });
    }

    void onStop() {
        forum.removeChangeListeners();
        realm.close();
    }

    void sendComment(Forum forum, String comment) {
        if (comment.isEmpty()) {
            getView().showMessage("Blank comment not permitted!");
        } else {
            saveComment(App.getInstance().getApiInterface().createComment(
                    Credentials.basic(user.getUsername(), user.getPassword()), forum.getId(), comment));
        }
    }

    void editComment(Comment comment) {
        if (comment.getBody().isEmpty()) {
            getView().showMessage("Blank comment not permitted!");
        } else {
            saveComment(App.getInstance().getApiInterface().editComment(comment.getId(),
                    Credentials.basic(user.getUsername(), user.getPassword()),
                    comment.getForum(), comment.getBody()));
            getView().startProgressLoading();
        }
    }

    private void saveComment(Call<Comment> commentCall) {
        getView().startProgressLoading();
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, final Response<Comment> response) {
                getView().stopProgressDialog();
                if (response.isSuccessful()) {
                    final Realm realm = Realm.getDefaultInstance();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(response.body());
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            realm.close();
                            getView().onCommentSaved();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            realm.close();
                            Log.e(TAG, "onError: Error Saving Comment", error);
                            getView().showMessage("Error Saving Comment");
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
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e(TAG, "onFailure: Create Save Comment API Failed", t);
                getView().stopProgressDialog();
                getView().showMessage("Error Sending Comment");
            }
        });
    }

    void deleteForum(final int forumId) {
        getView().startProgressLoading();
        App.getInstance().getApiInterface().deleteForum(forumId,
                Credentials.basic(user.getUsername(), user.getPassword()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        getView().stopProgressDialog();
                        if (response.isSuccessful()) {
                            final Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Forum forum = realm.where(Forum.class).equalTo(Constants.ID, forumId).findFirst();
                                    forum.deleteFromRealm();
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    realm.close();
                                    getView().deleteForumSuccessful();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    realm.close();
                                    Log.e(TAG, "onError: Error Saving Forum", error);
                                    getView().showMessage("Error Deleting Forum");
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
                        Log.e(TAG, "onFailure: Delete Forum API Failed", t);
                        getView().stopProgressDialog();
                        getView().showMessage("Error Deleting Forum");
                    }
                });

    }

    boolean isForumMine() {
        return user.getUsername().contentEquals(forum.getUsername());
    }



    void deleteCommentLocally(final Comment comment) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Comment mComment = realm.where(Comment.class).equalTo(Constants.ID, comment.getId()).findFirst();
                mComment.deleteFromRealm();
            }
        });

    }
}
