package com.example.myappsample.swipe

import android.graphics.Canvas
import android.util.Log
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

class SwipeHelper2(val recyclerView: RecyclerView) : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null      // 현재 스와이프 한 아이템 위치
    private var beforePosition: Int? = null       // 상호 작용 끝난 포지션
    private var currentDx = 0f
    private var holdingWidth = 0f                    // 고정 시킬 넓이

    init {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

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
        // 뷰가 경계를 벗어날 때 까지 애니메이션을 적용한 다음 호출, swipe 동작 없음
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // ItemTouchHelper가 ViewHolder 스와이프 / 드래그 되었을 때 호출
        viewHolder?.let {
            currentPosition = viewHolder.bindingAdapterPosition  // 현재 스와이프 한 아이템 위치
            getDefaultUIUtil().onSelected(getView(it))
        }
    }
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,                             // 사용자의 행동으로 인한 수평 변위량
        dY: Float,                             // 사용자의 행동으로 인한 수직 변위량
        actionState: Int,
        isCurrentlyActive: Boolean            // 뷰가 사용자에 의해 제어되고 있는지 여부 반환
    ) {
        // 뷰의 변화가 생겼을 때 호출되는 함수, 상호 작용에 반응하는 방식 정의
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isHold = getTag(viewHolder)      // 고정할지 말지 결정, true : 고정함 false : 고정 안 함
            val newX = holdViewHolderPositionWidth(dX, isHold, isCurrentlyActive)  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

            currentDx = newX

            // ItemTouchHelper 항목 변환을 처리하는 유틸리티 클래스, ViewHolder의 하위 항목
            getDefaultUIUtil().onDraw(c, recyclerView, view, newX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        // 사용자 상호작용이 끝나고 애니메이션도 완료되었을 때 ItemTouchHelper에 의해 호출
        // onSelectChanged, onChildDraw 에서 수행 된 모든 변경 사항을 지운다
        currentDx = 0f
        beforePosition = viewHolder.bindingAdapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
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

    private fun holdViewHolderPositionWidth(
        dX: Float,  // 행동에 따른 수평 변위량
        isHold: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {

        // 최대값 : 스와이프 안된 상태
        val max = 0f

        // 고정할 수 있으면
        val newX = if (isHold) {
            // 현재 swipe 중이면 swipe되는 영역 제한
            if (isCurrentlyActive) {
                dX - holdingWidth
            } else {
                // swipe 중이 아니면 고정시키기
                -holdingWidth
            }
        } else {
            // 고정할 수 없으면 newX는 스와이프한 만큼
            dX / 4
        }
        // newX가 0보다 작은지 확인
        return min(newX, max)
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isHold: Boolean) {
        // holding 여부 세팅
        viewHolder.itemView.tag = isHold
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        // holding 여부 반환
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setHoldingWidth(clamp: Float) { this.holdingWidth = clamp }

    fun removeHolding(recyclerView: RecyclerView) {
        beforePosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).animate().translationX(0f).duration = 200L
            setTag(viewHolder, false)
            beforePosition = null
        }
    }
}