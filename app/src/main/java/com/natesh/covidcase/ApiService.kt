package com.natesh.covidcase

import com.google.gson.GsonBuilder
import com.natesh.covidcase.pojo.CovidModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


interface ApiService {

    @GET("data.json")
    fun getSpinnerList(): Call<CovidModel>

    companion object RetrofitClient {
        private const val BASE_URL = "https://api.covid19india.org/"

        private val client = OkHttpClient().newBuilder()
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).build()
        private val gson = GsonBuilder()
            .setLenient()
            .create()

        fun serviceRequest(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(ApiService::class.java)
        }
    }
}