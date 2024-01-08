package com.example.myappsample.swipe

import android.graphics.Canvas
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.R
import kotlin.math.min

/**
 * https://velog.io/@nimok97/RecyclerView-%EC%97%90%EC%84%9C-item-Swipe-%ED%95%98%EA%B8%B0-feat.-ItemTouchHelper-ItemTouchUIUtil
 * https://github.com/yeon-kyu/HoldableSwipeHandler/blob/main/HoldableSwipeHelper/src/main/java/com/yeonkyu/HoldableSwipeHelper/HoldableSwipeHandler.kt
 */

class SwipeHelper2() : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

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
        // swiped 동작 없음
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPosition = viewHolder.bindingAdapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // ItemTouchHelper가 ViewHolder를 스와이프 되었거나 드래그 되었을 때 호출
        viewHolder?.let {
            currentPosition = viewHolder.bindingAdapterPosition  // 현재 스와이프 한 아이템 위치
            getDefaultUIUtil().onSelected(getView(it))
        }
    }
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
            val isClamped = getTag(viewHolder)      // 고정할지 말지 결정, true : 고정함 false : 고정 안 함
            val newX = clampViewPositionHorizontal(dX, isClamped, isCurrentlyActive)  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

//            // 고정시킬 시 애니메이션 추가
//            if (newX == -clamp) {
//                getView(viewHolder).animate().translationX(-clamp).setDuration(50L).start()
//                return
//            }

            currentDx = newX
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                newX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        //return super.getSwipeEscapeVelocity(defaultValue)
        // yj : 필요 여부 확인
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        // 스와이프로 간주되기 위해 사용자가 뷰를 움직여야 하는 비율을 반환
        // 5f 이상 스와이프 시 끝까지 스와이프가 된다.
        val holderWidth = -viewHolder.itemView.findViewById<TextView>(R.id.swipe_text_view).width
        setTag(viewHolder, currentDx >= holderWidth)
        return 2f
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View = viewHolder.itemView.findViewById(R.id.swipe_view)

    private fun clampViewPositionHorizontal(
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        // RIGHT 방향으로 swipe 막기
        val max = 0f

        // 고정할 수 있으면
        val newX = if (isClamped) {
            // 현재 swipe 중이면 swipe되는 영역 제한
            if (isCurrentlyActive)
            // 오른쪽 swipe일 때
                if (dX < 0) dX/3 - clamp
                // 왼쪽 swipe일 때
                else dX - clamp
            // swipe 중이 아니면 고정시키기
            else -clamp
        }
        // 고정할 수 없으면 newX는 스와이프한 만큼
        else dX / 3

        // newX가 0보다 작은지 확인
        return min(newX, max)
    }

    // 스와이프가 되었는지를 tag 값으로 판단했으나, 뷰홀더 재활용 과정에서 혼란이 발생할 수 있음 -> 리사이클러뷰 데이터 클래스에 swipe가 되었는지를 판단하는 data 추가
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        // isClamped를 view의 tag로 관리
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setClamp(clamp: Float) { this.clamp = clamp }

    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }

}