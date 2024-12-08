package com.dermatoai.api

import com.dermatoai.BuildConfig
import com.dermatoai.api.DermatoClient.retrofit
import com.dermatoai.api.DermatoClient.service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A class that responsible to provide service to fetch the api.
 * @property retrofit
 * @property service
 *
 */
object DermatoClient {
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
        .baseUrl(BuildConfig.DERMATO_SERVER_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    /**
     *variable that used to fetch the api with DermatoEndpoint basis.
     * require Interface associated with retrofit schema
     */
    var service: DermatoEndpoint = retrofit.create(DermatoEndpoint::class.java)
}