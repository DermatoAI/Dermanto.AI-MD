package com.dermatoai.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Interface that representative for api schema and have ability to fetch data to the server.
 */
interface DermatoEndpoint {

    @Multipart
    @POST("analyze-skin")
    suspend fun analyzeImage(
        @Part file: MultipartBody.Part,
        @Part("userId") userId: RequestBody,
    ): ImageAnalysisResponse

    @POST("appointments/create")
    suspend fun createAppointment(
        @Body appointmentRequest: AppointmentRequest
    ): AppointmentResponse

    @DELETE("appointments/delete/{id}")
    suspend fun deleteAppointment(@Path("id") id: String)
}