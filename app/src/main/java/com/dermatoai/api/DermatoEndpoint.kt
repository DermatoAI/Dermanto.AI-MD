package com.dermatoai.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
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
interface DermatoAnalyzeEndpoint {

    @Multipart
    @POST("analyze-skin")
    suspend fun analyzeImage(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
    ): ImageAnalysisResponse

}

interface DermatoChatBotEndpoint {
    @POST("chatbot")
    suspend fun chatApi(@Body chatRequest: ChatRequest): ChatResponse
}

interface DermatoBackendEndpoint {
    @POST("appointments/create")
    suspend fun createAppointment(
        @Body appointmentRequest: AppointmentRequest
    ): AppointmentResponse

    @DELETE("appointments/delete/{id}")
    suspend fun deleteAppointment(@Path("id") id: String)

    @GET("doctors/all")
    suspend fun getAllDoctors(): DoctorsResponse

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