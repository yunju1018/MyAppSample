package com.example.myappsample.swipe

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
        notifyDataSetChanged()
    }

    inner class SwipeViewHolder(private val binding: SwipeItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String, position: Int) {
            binding.tvRemove.text = "삭제"

            binding.swipeView.setOnClickListener {
                Toast.makeText(itemView.context, "$position 팝업띄우기 ", Toast.LENGTH_SHORT).show()
            }

            binding.tvRemove.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setMessage("$position 번째 삭제 하시겠습니까?")
                    .setPositiveButton("예") { p0, p1 ->
                        removeData(position)
                        Toast.makeText(itemView.context, "Deleted item $position", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("아니오", null)
                    .create()
                    .show()
            }

            binding.swipeTextView.text = text
        }
    }
}