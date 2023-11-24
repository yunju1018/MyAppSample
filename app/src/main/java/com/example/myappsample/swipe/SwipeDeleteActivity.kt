package com.example.myappsample.swipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding

/**
 * https://github.com/ntnhon/RecyclerViewRowOptionsDemo/tree/master
 */

class SwipeDeleteActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: Adapter
    private var toast : Toast? = null
    private var list : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

//    private fun setUpRecyclerView() {
//        binding.recyclerView.adapter = Adapter(listOf(
//            "Item 0: No action",
//            "Item 1: Delete",
//            "Item 2: Delete & Mark as unread",
//            "Item 3: Delete, Mark as unread & Archive"
//        ))
//        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//
//        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recyclerView) {
//            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
//                var buttons = listOf<UnderlayButton>()
//                val deleteButton = deleteButton(position)
//                val markAsUnreadButton = markAsUnreadButton(position)
//                val archiveButton = archiveButton(position)
//                when (position) {
//                    1 -> buttons = listOf(deleteButton)
//                    2 -> buttons = listOf(deleteButton, markAsUnreadButton)
//                    3 -> buttons = listOf(deleteButton, markAsUnreadButton, archiveButton)
//                    else -> Unit
//                }
//                return buttons
//            }
//        })
//
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
//    }

    private fun init() {
        list = makeList()
        swipeAdapter = Adapter(list)

        setUpRecyclerViewYj()
    }

    private fun setUpRecyclerViewYj() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteActivity)
        }

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton(position)
                return listOf(deleteButton)
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun toast(text: String) {
        toast?.cancel()
        toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
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
            val str = "item $i"
            arrayList.add(str)
        }
        return arrayList
    }
//
//    private fun markAsUnreadButton(position: Int) : SwipeHelper.UnderlayButton {
//        return SwipeHelper.UnderlayButton(
//            this,
//            "읽지 않음",
//            14.0f,
//            android.R.color.holo_green_light,
//            object : SwipeHelper.UnderlayButtonClickListener {
//                override fun onClick() {
//                    toast("Marked as unread item $position")
//                }
//            })
//    }
//
//    private fun archiveButton(position: Int) : SwipeHelper.UnderlayButton {
//        return SwipeHelper.UnderlayButton(
//            this,
//            "보관",
//            14.0f,
//            android.R.color.holo_blue_light,
//            object : SwipeHelper.UnderlayButtonClickListener {
//                override fun onClick() {
//                    toast("Archived item $position")
//                }
//            })
//    }

}