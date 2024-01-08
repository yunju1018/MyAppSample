package com.example.myappsample.swipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
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

    private fun setUpRecyclerViewYj() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteButtonActivity2, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteButtonActivity2)
        }

        val swipeHelper = SwipeHelper2(swipeAdapter)
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderButton {
        return SwipeHelper.UnderButton(
            this,
            "삭제",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    list.removeAt(position)
                    swipeAdapter.notifyDataSetChanged()
                    toast("Deleted item $position")
                }
            })
    }

    private fun makeList() : ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 0 until 20) {
            val str = "item $i 아이템 입력 테스트 "
            arrayList.add(str)
        }
        return arrayList
    }
}