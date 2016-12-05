package com.capstone.tip.emassage.app;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author pocholomia
 * @since 05/12/2016
 */

public interface MediaApiInterface {

    @Streaming
    @GET
    Call<ResponseBody> getLessonPdf(@Url String url);

}
