package com.example.invetorylock

import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.invetorylock.retrofit.MainAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContainerAdapter(val listener: Listener): RecyclerView.Adapter<ContainerHolder>() {
    val containers = arrayListOf<Container>(
        Container(1,"Красный", 1, 0),
        Container(2,"Зелёный", 1, 2),
        Container(3,"Синий", 1, 4),
        Container(4,"Жёлтый", 1, 6),
        Container(5,"Серый", 1, 8))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.container, parent, false)
        return ContainerHolder(view)
    }

    override fun onBindViewHolder(holder: ContainerHolder, position: Int) {
        holder.bind(containers[position], listener)
    }

    override fun getItemCount(): Int {
        return containers.size
    }

    interface Listener{
        fun onClick(container: Container)
    }
}