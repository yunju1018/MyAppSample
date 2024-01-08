package com.example.myappsample.swipe

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.R

/**
 * https://velog.io/@nimok97/RecyclerView-%EC%97%90%EC%84%9C-item-Swipe-%ED%95%98%EA%B8%B0-feat.-ItemTouchHelper-ItemTouchUIUtil
 * https://github.com/yeon-kyu/HoldableSwipeHandler/blob/main/HoldableSwipeHelper/src/main/java/com/yeonkyu/HoldableSwipeHelper/HoldableSwipeHandler.kt
 */

class SwipeHelper2(private val recyclerViewAdapter : SwipeAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        // 이동 방향 결정 (Drag 없음, Swipe LEFT)
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        // 항목을 드래그하면 호출 호출
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 뷰가 경계를 벗어날 때 까지 애니메이션을 적용한 다음 호출,
        // 여기서 업데이트 하고(삭제) notify 호출
//        recyclerViewAdapter.removeData(viewHolder.layoutPosition)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        // 스와이프로 간주되기 위해 사용자가 뷰를 움직여야 하는 비율을 반환
        // 5f 이상 스와이프 시 끝까지 스와이프가 된다.
        return 2f
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // ItemTouchHelper가 ViewHolder를 스와이프 되었거나 드래그 되었을 때 호출
        viewHolder?.let {
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View = viewHolder.itemView.findViewById(R.id.swipe_view)

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = false      // 고정할지 말지 결정, true : 고정함 false : 고정 안 함
//            val newX = clampViewPositionHorizontal(dX, isClamped, isCurrentlyActive)  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)
//
//            // 고정시킬 시 애니메이션 추가
//            if (newX == -clamp) {
//                getView(viewHolder).animate().translationX(-clamp).setDuration(50L).start()
//                return
//            }
//
//            currentDx = newX
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

}