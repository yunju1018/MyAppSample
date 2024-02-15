package com.example.myappsample.swipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding
import com.example.myappsample.databinding.SwipeLayoutItemBinding


/**
 * https://github.com/yeon-kyu/HoldableSwipeHandler
 */

class SwipeDeleteLibraryActivity : AppCompatActivity() {

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
            addItemDecoration(
                DividerItemDecoration(
                    this@SwipeDeleteLibraryActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(this@SwipeDeleteLibraryActivity)
        }
    }

    inner class SwipeAdapter(private val strings: ArrayList<String>) : RecyclerSwipeAdapter<SwipeAdapter.SwipeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
            val binding = SwipeLayoutItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
            return SwipeViewHolder(binding)
        }

        override fun getItemCount(): Int = strings.size

        override fun getSwipeLayoutResourceId(position: Int): Int {
            return R.id.swipeLayout
        }

        override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
            holder.bind(strings[position], position)

            mItemManger.bindView(holder.itemView, position)
        }

        inner class SwipeViewHolder(private val binding: SwipeLayoutItemBinding) : RecyclerView.ViewHolder(binding.root)  {
            fun bind(text: String, position: Int) {
                binding.deleteTextView.text = "삭제"
                binding.textView.text = text
//
                if(position %2 == 0) {
                    binding.swipeLayout.isSwipeEnabled = false
                }

                binding.swipeLayout.isClickToClose = true
                binding.swipeLayout.addSwipeListener(onSwipeListener)
            }
        }

        private val onSwipeListener = object : SwipeLayout.SwipeListener{
            override fun onStartOpen(layout: SwipeLayout?) {
                // 선택한 뷰를 제외하고 닫음
                mItemManger.closeAllExcept(layout)
            }

            override fun onOpen(layout: SwipeLayout?) {

            }

            override fun onStartClose(layout: SwipeLayout?) {

            }

            override fun onClose(layout: SwipeLayout?) {

            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {

            }

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                mItemManger.closeAllItems()
            }
        }
    }


    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun makeList(): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 0 until 20) {
            val str = "item $i 지워보기 테스트"
            arrayList.add(str)
        }
        return arrayList
    }
}