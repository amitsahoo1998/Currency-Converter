package com.example.currencyconverter.data.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("/live?access_key=b1d215fffca5241694e84c98f87c1f65")
    suspend fun getRates(
        @Query("source") source: String
    ) : Response<CurrencyResponse>
}