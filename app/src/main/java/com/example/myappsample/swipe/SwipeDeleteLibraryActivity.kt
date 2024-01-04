package com.example.myappsample.swipe

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding
import com.yeonkyu.HoldableSwipeHelper.HoldableSwipeHandler
import com.yeonkyu.HoldableSwipeHelper.SwipeButtonAction


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

    fun draw(text : String) : Drawable {
        val textsize = 12 * resources.displayMetrics.scaledDensity
        val paint = Paint().apply {
            color = ContextCompat.getColor(this@SwipeDeleteLibraryActivity,R.color.white)
            textSize = 12 * resources.displayMetrics.density // dp to px
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.LEFT
        }

        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)

        // Create a bitmap with enough space to draw the text
        val bitmap = Bitmap.createBitmap((textsize * text.length).toInt(), (textsize).toInt(), Bitmap.Config.ARGB_8888)

        // Create a canvas using the bitmap
        val canvas = Canvas(bitmap)

        // Draw the text on the canvas
        canvas.drawText(text, 0f, textBounds.height().toFloat(), paint)

        return BitmapDrawable(bitmap)
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