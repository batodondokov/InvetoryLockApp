package com.example.invetorylock.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface MainAPI {
    @GET("/InventoryLock/containers.php?load_all=1")
    suspend fun getAllContainers(@Header("AUTHORIZATION") token: String): ContainerListResponse

    @GET("/InventoryLock/containers.php?")
    suspend fun getContainer(@Header("AUTHORIZATION") token: String,
                             @Query("container_id") id:Int): Container

    @POST("/InventoryLock/auth.php")
    suspend fun auth(@Body authRequest: AuthenticationRequest): AuthenticationResponse

    @POST("/InventoryLock/accounting.php")
    suspend fun performAccounting(@Header("AUTHORIZATION") token: String,
                                  @Body accountingRequest: AccountingRequest)
}