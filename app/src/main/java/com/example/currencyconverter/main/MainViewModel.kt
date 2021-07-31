package com.example.currencyconverter.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.models.Quotes
import com.example.currencyconverter.util.DispatcherProvider
import com.example.currencyconverter.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.round


class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.massage!!)
                is Resource.Succecs -> {
                    val rates = ratesResponse.data!!.quotes
                    val rate = getRateForCurrency(toCurrency, rates)
                    if(rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Quotes) = when (currency) {
        "CAD" -> rates.USDCAD
        "HKD" -> rates.USDHKD
        "ISK" -> rates.USDISK
        "EUR" -> rates.USDEUR
        "PHP" -> rates.USDPHP
        "DKK" -> rates.USDDKK
        "HUF" -> rates.USDHUF
        "CZK" -> rates.USDCZK
        "AUD" -> rates.USDAUD
        "RON" -> rates.USDRON
        "SEK" -> rates.USDSEK
        "IDR" -> rates.USDIDR
        "INR" -> rates.USDINR
        "BRL" -> rates.USDBRL
        "RUB" -> rates.USDRUB
        "HRK" -> rates.USDHRK
        "JPY" -> rates.USDJPY
        "THB" -> rates.USDTHB
        "CHF" -> rates.USDCHF
        "SGD" -> rates.USDSGD
        "PLN" -> rates.USDPLN
        "BGN" -> rates.USDBGN
        "CNY" -> rates.USDCNY
        "NOK" -> rates.USDNOK
        "NZD" -> rates.USDNZD
        "ZAR" -> rates.USDZAR
        "USD" -> rates.USDNZD
        "MXN" -> rates.USDMXN
        "ILS" -> rates.USDILS
        "GBP" -> rates.USDGBP
        "KRW" -> rates.USDKRW
        "MYR" -> rates.USDMYR
        else -> null
    }
}