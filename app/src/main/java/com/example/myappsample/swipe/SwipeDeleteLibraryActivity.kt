package com.example.myappsample.swipe

import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding
import com.yeonkyu.HoldableSwipeHelper.HoldableSwipeHandler
import com.yeonkyu.HoldableSwipeHelper.SwipeButtonAction
import com.yeonkyu.HoldableSwipeHelper.deprecated.HoldableSwipeHelper


/**
 * https://github.com/yeon-kyu/HoldableSwipeHandler
 */

class SwipeDeleteLibraryActivity : AppCompatActivity() {

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
        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteLibraryActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteLibraryActivity)
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        HoldableSwipeHandler.Builder(this)
            .setOnRecyclerView(binding.recyclerView)
            .setSwipeButtonAction(object : SwipeButtonAction {
                override fun onClickFirstButton(absoluteAdapterPosition: Int) {
                    toast(absoluteAdapterPosition.toString())
                }
            })
            .setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            .setFirstItemDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background)!!)
            .setFirstItemSideMarginDp(20)
            .setDismissOnClickFirstItem(true)
            .excludeFromHoldableViewHolder(10010)
            .build()
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