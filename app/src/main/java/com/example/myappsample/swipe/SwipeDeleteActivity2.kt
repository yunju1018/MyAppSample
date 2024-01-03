package com.example.myappsample.swipe

import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding


/**
 * https://github.com/ntnhon/RecyclerViewRowOptionsDemo/tree/master
 */

class SwipeDeleteActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: SwipeAdapter
    private var list: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        list = makeList()
        swipeAdapter = SwipeAdapter(list)
        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteActivity2, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteActivity2)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val swipeHelperCallback = SwipeHelperCallback(swipeAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.setOnTouchListener { view, motionEvent ->
            swipeHelperCallback.removePreviousClamp(binding.recyclerView)
            false
        }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun makeList(): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 0 until 20) {
            val str = "item $i"
            arrayList.add(str)
        }
        return arrayList
    }
}