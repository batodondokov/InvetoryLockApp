package com.example.invetorylock.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainAPI {
    @GET("/InventoryLock/containers.php?load_all=1")
    suspend fun getAllContainers(): ContainerListResponse

    @GET("/InventoryLock/containers.php?container_id=1")
    suspend fun getContainer(): Container

    @POST("auth.php")
    fun auth(@Body authRequest: AuthenticationRequest): Call<AuthenticationResponse>

    @POST("/InventoryLock/accounting.php")
    suspend fun performAccounting(@Body accountingRequest: AccountingRequest)
}