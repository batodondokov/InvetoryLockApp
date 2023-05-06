package com.example.invetorylock

import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContainerAdapter(val listener: Listener): RecyclerView.Adapter<ContainerHolder>() {
    val containers = arrayListOf<Container>(
        Container(1,"Красный", true, 0),
        Container(2,"Зелёный", true, 2),
        Container(3,"Синий", true, 4),
        Container(4,"Жёлтый", true, 6),
        Container(5,"Серый", true, 8))


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