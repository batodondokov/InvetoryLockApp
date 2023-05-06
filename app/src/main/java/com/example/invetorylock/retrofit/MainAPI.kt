package com.example.invetorylock.retrofit

import retrofit2.http.Body
import retrofit2.http.POST

interface MainAPI {
    @POST("/authentication")
    suspend fun authentication(@Body authenticationRequest: AuthenticationRequest):Token
}