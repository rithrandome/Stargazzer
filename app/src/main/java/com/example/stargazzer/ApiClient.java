package com.example.stargazzer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit apodRetrofit;
    private static Retrofit libraryRetrofit;
    private static Gson gson;

    public static Retrofit getLibraryRetrofit() {

        gson = new GsonBuilder()
                .create();

        libraryRetrofit = new Retrofit.Builder()
                .baseUrl("https://images-api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return libraryRetrofit;
    }

    public static Retrofit getApodRetrofit(){

        gson = new GsonBuilder()
                .create();

        apodRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/planetary/apod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return apodRetrofit;

    }

}
