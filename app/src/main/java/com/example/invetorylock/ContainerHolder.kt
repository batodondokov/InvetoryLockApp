package com.example.invetorylock

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.invetorylock.databinding.ContainerBinding

class ContainerHolder(item: View): RecyclerView.ViewHolder(item) {
    val binding = ContainerBinding.bind(item)

    val imageList = listOf(R.drawable.red_locked,
        R.drawable.red_unlocked,
        R.drawable.green_locked,
        R.drawable.green_unlocked,
        R.drawable.blue_locked,
        R.drawable.blue_unlocked,
        R.drawable.yellow_locked,
        R.drawable.yellow_unlocked,
        R.drawable.gray_locked,
        R.drawable.gray_unlocked)

    fun bind(container: Container, listener: ContainerAdapter.Listener){
        val status = if (container.status == 1) "закрыт" else "открыт"
        if (container.imgId != null){
            with(binding){
                ivTitle.setImageResource(imageList[container.imgId!!])
                tvTitle.text = "${container.color} ящик №${container.id} $status"
                itemView.setOnClickListener{
                    listener.onClick(container)
                }
            }
        }
    }
}