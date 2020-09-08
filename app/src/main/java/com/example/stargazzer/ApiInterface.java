package com.example.stargazzer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/search")
    Call<LibraryItem> performSearch(@Query("q") String q);

    @GET("/asset/{nasa_id}")
    Call<AssetItem> getAssetInfo(@Path("nasa_id") String nasa_id);

    @GET("?api_key=XPlHjktYUmPzEiDfbPCTNmwad5p1ZgCcLMw1KHcx")
    Call<PicInfo> getPicInfo(@Query("date") String date) ;
}

