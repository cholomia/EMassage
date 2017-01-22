package com.capstone.tip.emassage.app;

import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.Course;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.data.Grade;
import com.capstone.tip.emassage.model.data.Twist;
import com.capstone.tip.emassage.model.data.User;
import com.capstone.tip.emassage.model.data.Video;
import com.capstone.tip.emassage.model.response.CommentVote;
import com.capstone.tip.emassage.model.response.ForumVote;
import com.capstone.tip.emassage.model.response.CommentListResponse;
import com.capstone.tip.emassage.model.response.ForumListResponse;
import com.capstone.tip.emassage.model.response.LoginResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Cholo Mia on 12/4/2016.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> login(@Field(Constants.USERNAME) String username,
                              @Field(Constants.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Endpoints.REGISTER)
    Call<User> register(@Field(Constants.USERNAME) String username,
                        @Field(Constants.EMAIL) String email,
                        @Field(Constants.FIRST_NAME) String firstName,
                        @Field(Constants.LAST_NAME) String lastName,
                        @Field(Constants.PASSWORD) String password);

    @GET(Endpoints.COURSES)
    Call<List<Course>> courses();

    @GET(Endpoints.FORUMS)
    Call<ForumListResponse> forums(@Header("Authorization") String basicAuthentication);

    @GET(Endpoints.FORUMS)
    Call<ForumListResponse> forums(@Header("Authorization") String basicAuthentication,
                                   @QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(Endpoints.FORUMS)
    Call<Forum> createForum(@Header("Authorization") String basicAuthentication,
                            @Field("title") String title,
                            @Field("content") String content);

    @FormUrlEncoded
    @PUT(Endpoints.FORUM)
    Call<Forum> editForum(@Path("id") int id,
                          @Header("Authorization") String basicAuthentication,
                          @Field("title") String title,
                          @Field("content") String content);

    @DELETE(Endpoints.FORUM)
    Call<ResponseBody> deleteForum(@Path("id") int id,
                                   @Header("Authorization") String basicAuthentication);

    @GET(Endpoints.COMMENTS)
    Call<CommentListResponse> comments();

    @GET(Endpoints.COMMENTS)
    Call<CommentListResponse> comments(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(Endpoints.COMMENTS)
    Call<Comment> createComment(@Header("Authorization") String basicAuthentication,
                                @Field("forum") int forumId,
                                @Field("body") String body);

    @FormUrlEncoded
    @PUT(Endpoints.COMMENT)
    Call<Comment> editComment(@Path("id") int id,
                              @Header("Authorization") String basicAuthentication,
                              @Field("forum") int forumId,
                              @Field("body") String body);

    @DELETE(Endpoints.COMMENT)
    Call<ResponseBody> deleteComment(@Path("id") int id,
                                     @Header("Authorization") String basicAuthentication);

    @GET(Endpoints.GRADES)
    Call<List<Grade>> getGrades(@Header("Authorization") String basicAuthentication);

    @GET(Endpoints.GRADES)
    Call<List<Grade>> getGrades(@Header("Authorization") String basicAuthentication,
                                @QueryMap Map<String, String> params);

    /*@FormUrlEncoded
    @POST(Endpoints.GRADES)
    Call<Grade> saveGrade(@Header("Authorization") String basicAuthentication,
                          @Field("lesson") int lessonId,
                          @Field("raw_score") int rawScore,
                          @Field("item_count") int itemCount);*/

    @FormUrlEncoded
    @PUT(Endpoints.GRADE)
    Call<Grade> saveGrade(@Path("id") String id,
                          @Header("Authorization") String basicAuthentication,
                          @Field("id") String pk,
                          @Field("lesson") int lessonId,
                          @Field("raw_score") int rawScore,
                          @Field("item_count") int itemCount,
                          @Field("try_count") int tryCount);

    @FormUrlEncoded
    @PUT(Endpoints.FORUM_VOTE)
    Call<ForumVote> forumVote(@Path("id") String id,
                              @Header("Authorization") String basicAuthentication,
                              @Field("forum") int forumId,
                              @Field("vote") int vote);

    @FormUrlEncoded
    @PUT(Endpoints.COMMENT_VOTE)
    Call<CommentVote> commentVote(@Path("id") String id,
                                  @Header("Authorization") String basicAuthentication,
                                  @Field("comment") int commentId,
                                  @Field("vote") int vote);

    @GET(Endpoints.VIDEOS)
    Call<List<Video>> videos();

    @GET(Endpoints.TWIST)
    Call<List<Twist>> getTwistWords();

}
