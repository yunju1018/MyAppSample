package com.example.myappsample.swipe

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding
import com.example.myappsample.databinding.ListItemBinding
import com.example.myappsample.databinding.TextViewHolderBinding
import me.thanel.swipeactionview.SwipeActionView
import me.thanel.swipeactionview.SwipeGestureListener


/**
 * https://github.com/ntnhon/RecyclerViewRowOptionsDemo/tree/master
 */

class SwipeActionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: SwipeActionAdapter
    private var list: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        list = makeList()
        swipeAdapter = SwipeActionAdapter(list)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@SwipeActionActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }


    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun makeList(): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 1 until 21) {
            val str = "item $i"
            arrayList.add(str)
        }
        return arrayList
    }


    class SwipeActionAdapter(private val strings: ArrayList<String>) : RecyclerView.Adapter<SwipeActionAdapter.TextViewHolder>() {
        override fun getItemCount() = strings.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
            val binding: ListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item,
                parent,
                false
            )
            return TextViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TextViewHolder, position: Int) =
            holder.bind(strings[position],position)

        fun removeAt(position: Int) {
            strings.removeAt(position)
            notifyItemRemoved(position)
        }

        inner class TextViewHolder(private val binding: ListItemBinding) : ViewHolder(binding.root) {

            fun bind(text: String, position: Int) {
                binding.text.text = text
                binding.swipeView.swipeGestureListener = object : SwipeGestureListener {

                    override fun onSwipedLeft(swipeActionView: SwipeActionView): Boolean {
                        binding.textButton.setOnClickListener {
                            Toast.makeText(itemView.context, "${position+1} 번 아이템 삭제", Toast.LENGTH_SHORT).show()
                            removeAt(position)
                        }

                        return false
                    }
                }
            }

        }
    }

}