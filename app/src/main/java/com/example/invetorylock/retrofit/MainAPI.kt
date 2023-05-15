package com.example.invetorylock.retrofit

import com.example.invetorylock.Container
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainAPI {
    @GET("containers.php?load_all=1")
    fun getContainers(): Call<ContainerListResponse>

    @POST("auth.php")
    fun auth(@Body authRequest: AuthenticationRequest): Call<AuthenticationResponse>

    @POST("accounting.php")
    fun performAccounting(@Body accountingRequest: AccountingRequest)
}