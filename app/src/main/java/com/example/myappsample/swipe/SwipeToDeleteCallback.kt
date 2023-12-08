package com.example.myappsample.swipe

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.R


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