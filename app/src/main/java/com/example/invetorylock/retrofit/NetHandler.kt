package com.example.invetorylock.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetHandler {
    companion object {
        val retrofit by lazy {
            val interseptor = HttpLoggingInterceptor()
            interseptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

            with(Retrofit.Builder()) {
                baseUrl("https://inventorylock.hostfl.ru/")
                client(client)
                addConverterFactory(GsonConverterFactory.create())
                build()
            }
        }

        val mainApi by lazy {
            retrofit.create(MainAPI::class.java)
        }
    }
}