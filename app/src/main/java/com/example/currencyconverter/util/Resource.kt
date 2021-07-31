package com.example.currencyconverter.util

sealed class Resource<T>(val data: T? , val massage: String?) {
    class Succecs<T>(data: T) :Resource<T>(data,null)
    class Error<T>(massage: String) :Resource<T>(null,massage)
}