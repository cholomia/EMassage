package com.capstone.tip.emassage.app;

import com.capstone.tip.emassage.model.data.Comment;
import com.capstone.tip.emassage.model.data.Course;
import com.capstone.tip.emassage.model.data.Forum;
import com.capstone.tip.emassage.model.data.User;
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
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

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
                        @Field(Constants.FIRST_NAME) String firstName,
                        @Field(Constants.LAST_NAME) String lastName,
                        @Field(Constants.PASSWORD) String password);

    @GET(Endpoints.COURSES)
    Call<List<Course>> courses();

    @GET(Endpoints.FORUMS)
    Call<ForumListResponse> forums();

    @GET(Endpoints.FORUMS)
    Call<ForumListResponse> forums(@QueryMap Map<String, String> params);

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

}
