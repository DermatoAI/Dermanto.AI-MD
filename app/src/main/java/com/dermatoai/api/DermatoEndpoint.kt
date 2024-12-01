package com.dermatoai.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Interface that representative for api schema and have ability to fetch data to the server.
 */
interface DermatoEndpoint {
    @POST("register")
    suspend fun regisUser(): Call<Unit>

    @POST("authorize")
    fun authorizeUser(): Call<Unit>

    @POST("climate")
    fun getClimateInfo(): Call<ClimateInfoBRS>

    @Multipart
    @POST("analyze")
    suspend fun analyzeImage(
        @Part file: MultipartBody.Part
    ): Response<AnalyzeImage>

    @POST("chatbot")
    fun askChatBot(): Call<ChatBRQS>

}