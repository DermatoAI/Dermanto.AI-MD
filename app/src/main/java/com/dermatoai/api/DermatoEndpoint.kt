package com.dermatoai.api

import retrofit2.Call

/**
 * Interface that representative for api schema and have ability to fetch data to the server.
 */
interface DermatoEndpoint {

    fun regisUser(): Call<Unit>

    fun authorizeUser(): Call<Unit>

    fun getClimateInfo(): Call<ClimateInfoBRS>

    fun analyzeImage(): Call<AnalyzeImageBRS>

    fun askChatBot(): Call<ChatBRQS>

}