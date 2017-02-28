package com.capstone.tip.emassage.app;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cholo Mia on 12/3/2016.
 */

public class App extends Application {

    private static App sInstance;
    private OkHttpClient.Builder httpClient;
    private Retrofit retrofit;
    private ApiInterface apiInterface;
    private Retrofit retrofitMedia;
    private MediaApiInterface mediaApiInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    public synchronized static App getInstance() {
        return sInstance;
    }


    private OkHttpClient.Builder getOkHttpClient() {
        if (httpClient == null) {
            // setup logs for debugging of http request
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder();
            // add your other interceptors …

            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
        }
        return httpClient;
    }

    private Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            String url = Endpoints.API_URL;
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClient().build())
                    .build();
        }
        return retrofit;
    }

    private Retrofit getMediaClient() {
        if (retrofitMedia == null) {
            String url = Endpoints.BASE_URL;
            retrofitMedia = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient().build())
                    .build();
        }
        return retrofitMedia;
    }

    public ApiInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = getClient().create(ApiInterface.class);
        }
        return apiInterface;
    }

    public MediaApiInterface getMediaApiInterface() {
        if (mediaApiInterface == null) {
            mediaApiInterface = getMediaClient().create(MediaApiInterface.class);
        }
        return mediaApiInterface;
    }

}
