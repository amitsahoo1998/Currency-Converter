package com.example.currencyconverter.main

import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.util.Resource

interface MainRepository {
    suspend fun getRates(quotes: String):Resource<CurrencyResponse>
}