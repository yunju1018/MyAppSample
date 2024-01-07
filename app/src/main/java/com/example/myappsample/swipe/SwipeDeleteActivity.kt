package com.example.myappsample.swipe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding

// 단순 스와이프 삭제
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

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as Adapter
                adapter.removeAt(viewHolder.adapterPosition)
                toast(viewHolder.adapterPosition.toString())
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)


        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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
}

abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    val mContext = context
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        val itemView = viewHolder.itemView

        // 스와이프 배경 색 지정
        ColorDrawable().apply {
            color = Color.parseColor("#f44336")
            setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            draw(c)
        }

        // 스와이프 텍스트
        val text = "삭제"
        val paint = Paint()
            .apply {
                textSize = mContext.resources.displayMetrics.density
                typeface = Typeface.DEFAULT_BOLD
                textSize = 40f
                color = Color.parseColor("#ffffff")
                textAlign = Paint.Align.LEFT
            }

        val titleBounds = Rect()    // 사각형
        paint.getTextBounds(text, 0, text.length, titleBounds)

        val x = itemView.right - paint.measureText(text) - paint.measureText(text)
        val y = itemView.bottom - paint.measureText(text)
        c.drawText(text, x, y, paint)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}