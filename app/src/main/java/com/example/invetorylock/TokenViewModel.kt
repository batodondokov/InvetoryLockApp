package com.example.invetorylock

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TokenViewModel:ViewModel() {
    var token = MutableLiveData<String>()
}