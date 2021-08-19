package com.example.spinner;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.Call;

public interface JsonPlaceHolderApi {
    @GET("all")
    Call<List<API>> getAllPosts();
}
