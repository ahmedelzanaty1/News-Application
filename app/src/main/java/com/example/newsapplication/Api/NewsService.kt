package com.example.newsapplication.Api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines/sources")
    fun GetSources(@Query("apiKey") apiKey: String = "238e4e26601947f6bb765fab3f064e2e")
    : Call<NewsResponse>

}