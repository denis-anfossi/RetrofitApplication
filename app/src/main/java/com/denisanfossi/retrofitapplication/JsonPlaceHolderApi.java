package com.denisanfossi.retrofitapplication;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Article>> getArticles();

    @GET("posts/{id}")
    Call<Article> getArticle(@Path("id") int id);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);
}
