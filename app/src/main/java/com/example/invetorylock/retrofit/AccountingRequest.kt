package com.example.invetorylock.retrofit

data class AccountingRequest(
    val container_id: Int,
    val token: String,
    val status: Int,
)
