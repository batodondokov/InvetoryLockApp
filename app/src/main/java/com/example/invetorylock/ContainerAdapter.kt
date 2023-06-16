package com.example.invetorylock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.invetorylock.retrofit.Container

class ContainerAdapter(val containerList: List<Container>, val listener: ContainersInteraction): RecyclerView.Adapter<ContainerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleTextView: TextView
        private val imageView: ImageView
        private lateinit var container: Container
        init {
            titleTextView = itemView.findViewById(R.id.tvTitle)
            imageView = itemView.findViewById(R.id.image_view)
            itemView.setOnClickListener {
                listener.onContainerChosen(container)
            }
        }
        val unlockedContainersImgList = listOf(
            R.drawable.red_unlocked,
            R.drawable.green_unlocked,
            R.drawable.blue_unlocked,
            R.drawable.yellow_unlocked,
            R.drawable.gray_unlocked
        )
        val lockedContainersImgList = listOf(
            R.drawable.red_locked,
            R.drawable.green_locked,
            R.drawable.blue_locked,
            R.drawable.yellow_locked,
            R.drawable.gray_locked
        )
        fun setContainer(c: Container) {
            container = c
            val statusStr = if (container.status == 0) "закрыт" else "открыт"
            titleTextView.text = "${container.color} ящик №${container.id} $statusStr"
            if (container.status == 1){
                imageView.setImageResource(unlockedContainersImgList[container.id - 1])
            }
            if (container.status == 0){
                imageView.setImageResource(lockedContainersImgList[container.id - 1])
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContainer(containerList[position])
    }

    override fun getItemCount(): Int {
        return containerList.size
    }
}