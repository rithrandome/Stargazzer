package com.example.stargazzer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("?api_key=XPlHjktYUmPzEiDfbPCTNmwad5p1ZgCcLMw1KHcx")
    Call<PicInfo> getAPOD();

    @GET("?api_key=XPlHjktYUmPzEiDfbPCTNmwad5p1ZgCcLMw1KHcx")
    Call<PicInfo> getPicInfo(@Query("date") String date) ;
}

