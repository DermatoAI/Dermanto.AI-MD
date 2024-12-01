package com.dermatoai.api

import com.dermatoai.BuildConfig
import com.dermatoai.api.OpenMeteoClient.gson
import com.dermatoai.api.OpenMeteoClient.retrofit
import com.dermatoai.api.OpenMeteoClient.service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A class that responsible to provide service to fetch the api.
 * @property gson
 * @property retrofit
 * @property service
 *
 */
object OpenMeteoClient {
    /**
     *variable that used to configure serialize and deserialize json and pojo
     */
    private var gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    /**
     *variable that used to build to service with base URL from server.
     */
    private var retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BuildConfig.OPENMETEO_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    /**
     *variable that used to fetch the api with Weather basis.
     * require Interface associated with retrofit schema
     */
    var service: OpenMeteoEndpoint = retrofit.create(OpenMeteoEndpoint::class.java)
}