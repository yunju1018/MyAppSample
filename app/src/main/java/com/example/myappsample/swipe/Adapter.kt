package com.example.myappsample.swipe

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val strings: ArrayList<String>) : RecyclerView.Adapter<TextViewHolder>() {
    override fun getItemCount() = strings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TextViewHolder.from(parent)

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) =
        holder.bind(strings[position])

    fun removeAt(position: Int) {
        strings.removeAt(position)
        notifyItemRemoved(position)
    }
}