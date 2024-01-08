package com.example.myappsample.swipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.databinding.SwipeItemListBinding

class SwipeAdapter(private val strings: ArrayList<String>) : RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder>() {
    override fun getItemCount() = strings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        val swipeBinding = SwipeItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SwipeViewHolder(swipeBinding)
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) =
        holder.bind(strings[position], position)

    fun removeData(position: Int) {
        strings.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class SwipeViewHolder(private val binding: SwipeItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String, position: Int) {
            binding.tvRemove.text = "삭제"

            binding.tvRemove.setOnClickListener {
                removeData(position)
            }

            binding.swipeTextView.text = text
        }
    }
}