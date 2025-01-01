package com.example.newsapplication.Api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private lateinit var retrofit: Retrofit
    private const val BASE_URL = "https://newsapi.org/v2/ "
    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
fun getNewsService(): NewsService {
    return retrofit.create(NewsService::class.java)
}
    private fun HttpLoggingInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor{
            Log.e("api" ,it)
        }
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }
    private fun OkHttpClient() : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    }
}
