package com.dermatoai.api

import com.dermatoai.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * A class that responsible to provide service to fetch the api.
 *
 */
object DermatoClient {
    private var gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()


    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    /**
     *variable that used to build to service with base URL from server.
     */
    private var analyzeClient: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.DERMATO_SERVER_URL_ANALYZE)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private var backendClient: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.DERMATO_SERVER_URL_BACKEND)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private var chatbotClient: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.DERMATO_SERVER_URL_CHATBOT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    /**
     *variable that used to fetch the api with DermatoAnalyzeEndpoint basis.
     * require Interface associated with retrofit schema
     */
    var analyzeService: DermatoAnalyzeEndpoint =
        analyzeClient.create(DermatoAnalyzeEndpoint::class.java)
    var backendService: DermatoBackendEndpoint =
        backendClient.create(DermatoBackendEndpoint::class.java)
    var chatbotService: DermatoChatBotEndpoint =
        chatbotClient.create(DermatoChatBotEndpoint::class.java)
}