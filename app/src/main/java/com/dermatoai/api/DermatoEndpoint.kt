package com.dermatoai.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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

    @Multipart
    @POST("/analyze-skin")
    suspend fun analyzeSkin(
        @Part image: MultipartBody.Part
    ): SkinAnalysisResponse

    @POST("/add-diskusi")
    suspend fun tambahDiskusi(
        @Body requestBody: TambahDiskusiRequest
    ): TambahDiskusiResponse

    @DELETE("/hapus-diskusi/{id}")
    suspend fun hapusDiskusi(
        @Path("id") id: Int
    ): GeneralResponse

    @POST("/add-komentar")
    suspend fun tambahKomentar(
        @Body requestBody: TambahKomentarRequest
    ): TambahKomentarResponse

    @DELETE("/hapus-komentar/{id}")
    suspend fun hapusKomentar(
        @Path("id") id: Int
    ): GeneralResponse

    @GET("/list-diskusi")
    suspend fun listDiskusi(): ListDiskusiResponse
}