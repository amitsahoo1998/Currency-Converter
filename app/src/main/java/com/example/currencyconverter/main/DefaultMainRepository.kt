package com.example.currencyconverter.main

import com.example.currencyconverter.data.models.CurrencyApi
import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api :CurrencyApi
) : MainRepository{
    override suspend fun getRates(quotes: String): Resource<CurrencyResponse> {
        return try {
            val response= api.getRates(quotes)
            val result= response.body()
            if (response.isSuccessful && result != null){
                Resource.Succecs(result)
            }else{
                Resource.Error(response.message())            }
        }catch (e: Exception){
            Resource.Error(e.message?:"An error occurred")
        }
    }
}