package com.dermatoai.api

import com.dermatoai.BuildConfig
import retrofit2.Retrofit


object DermatoClient {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.DERMATO_SERVER_URL)
        .build()

    var service: DermatoEndpoint = retrofit.create(DermatoEndpoint::class.java)
}