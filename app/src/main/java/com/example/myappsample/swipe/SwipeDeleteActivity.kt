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

class SwipeDeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: Adapter
    private var list: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        list = makeList()
        swipeAdapter = Adapter(list)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@SwipeDeleteActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(this@SwipeDeleteActivity)
        }
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private var itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder, target: ViewHolder,
        ): Boolean {
            val fromPos = viewHolder.adapterPosition
            val toPos = target.adapterPosition
            toast("$toPos 삭제")
            // move item in `fromPos` to `toPos` in adapter.
            return false // true if moved, false otherwise
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }


        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            list.removeAt(position)
            swipeAdapter.notifyDataSetChanged()
        }
    })

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