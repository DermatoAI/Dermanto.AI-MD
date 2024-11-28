package com.dermatoai.api

import com.dermatoai.BuildConfig
import com.dermatoai.api.DermatoClient.retrofit
import com.dermatoai.api.DermatoClient.service
import retrofit2.Retrofit

/**
 * A class that responsible to provide service to fetch the api.
 * @property retrofit
 * @property service
 *
 */
object DermatoClient {
    /**
     *variable that used to build to service with base URL from server.
     * @param BuildConfig.DERMARO_SERVER_URL
     */
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.DERMATO_SERVER_URL)
        .build()

    /**
     *variable that used to fetch the api with DermatoEndpoint basis.
     * @param Interface associated with retrofit schema
     */
    var service: DermatoEndpoint = retrofit.create(DermatoEndpoint::class.java)
}