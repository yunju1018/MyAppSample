package com.example.myappsample.swipe

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.view.get
import androidx.databinding.adapters.AbsListViewBindingAdapter.OnScroll
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding

/**
 * https://velog.io/@nimok97/RecyclerView-%EC%97%90%EC%84%9C-item-Swipe-%ED%95%98%EA%B8%B0-feat.-ItemTouchHelper-ItemTouchUIUtil
 */

class SwipeDeleteButtonActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: SwipeAdapter
    private var list : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        list = makeList()
        swipeAdapter = SwipeAdapter(list)

        setUpRecyclerViewYj()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpRecyclerViewYj() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteButtonActivity2, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteButtonActivity2)
        }

        val swipeHelper = SwipeHelper2(binding.recyclerView).apply {
            setHoldingWidth(60 * resources.displayMetrics.density) // dp to px)
        }

//        binding.recyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->
//            swipeHelper.removeHolding(binding.recyclerView)
//        }

        binding.recyclerView.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                Log.d("yj", "MotionEvent motion : $e")
                // Down -> Move -> Up
                if(e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_MOVE) {
                    swipeHelper.getCurrentPosition()?.let {
                        rv.findChildViewUnder(e.x, e.y)?.let {
                            val selectHolder = rv.getChildViewHolder(it)
                            if(selectHolder.bindingAdapterPosition != swipeHelper.getCurrentPosition()) {
                                swipeHelper.removeHolding(rv)
                                swipeAdapter.removeItem(true)
                            }
                        }
                    }

                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                // 터치 이벤트 가로채는것을 원하지 않으면 true
            }
        })
    }

    private fun makeList() : ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 0 until 100) {
            val str = "item $i 스와이프 삭제 테스트 "
            arrayList.add(str)
        }
        return arrayList
    }
}