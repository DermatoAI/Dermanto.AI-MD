package com.dermatoai.api

import retrofit2.Call
import retrofit2.http.POST

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

    @POST("analyze")
    suspend fun analyzeImage(): Response<AnalyzeImage>

    @POST("chatbot")
    fun askChatBot(): Call<ChatBRQS>

}