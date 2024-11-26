package com.dermatoai.api

import retrofit2.Call

interface DermatoEndpoint {

    fun regisUser(): Call<Unit>

    fun authorizeUser(): Call<Unit>

    fun getClimateInfo(): Call<Unit>

    fun analyzeImage(): Call<Unit>

    fun askChatBot(): Call<Unit>

}