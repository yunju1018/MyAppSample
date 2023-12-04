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

class SwipeDeleteButtonActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: Adapter
    private var list : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        list = makeList()
        swipeAdapter = Adapter(list)

        setUpRecyclerViewYj()
    }

    private fun setUpRecyclerViewYj() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteButtonActivity, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteButtonActivity)
        }

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recyclerView) {
            override fun instantiateUnderlayButton(position: Int): List<UnderButton> {
                val deleteButton = deleteButton(position)
                return listOf(deleteButton)
            }
        })

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
            val str = "item $i"
            arrayList.add(str)
        }
        return arrayList
    }
}