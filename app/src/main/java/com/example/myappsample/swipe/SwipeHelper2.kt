package com.example.myappsample.swipe

import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.R
import kotlin.math.max
import kotlin.math.min

/**
 * https://velog.io/@nimok97/RecyclerView-%EC%97%90%EC%84%9C-item-Swipe-%ED%95%98%EA%B8%B0-feat.-ItemTouchHelper-ItemTouchUIUtil
 * https://github.com/yeon-kyu/HoldableSwipeHandler/blob/main/HoldableSwipeHelper/src/main/java/com/yeonkyu/HoldableSwipeHelper/HoldableSwipeHandler.kt
 */

class SwipeHelper2(val recyclerView: RecyclerView) : ItemTouchHelper.Callback() {
    private var currentPosition: Int? = null            // 현재 아이템 위치
    private var currentDx = 0f                          // 움직인 넓이
    private var holdingWidth = 0f                       // 고정 시킬 넓이(버튼 크기)

    init {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        // 이동 방향 결정 (Drag 없음, Swipe LEFT)
//            Log.d("yj", "getMovementFlags")
        val status = viewHolder.itemView.tag.toString()

        return if("A".equals(status, true)) {
            makeMovementFlags(0, 0)
        } else {
            makeMovementFlags(0, ItemTouchHelper.LEFT)
        }
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
//        Log.d("yj", "onSwiped")
        // 뷰가 경계를 벗어날 때 까지 애니메이션을 적용한 다음 호출, swipe 동작 없음
    }

//    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//        // ItemTouchHelper가 ViewHolder 스와이프 / 드래그 되었을 때 호출
//        viewHolder?.let {
////            currentPosition = viewHolder.bindingAdapterPosition  // 현재 스와이프 한 아이템 위치
//            Log.d("yj", "onSelectedChanged")
//            getDefaultUIUtil().onSelected(getView(it))
//        }
//    }
//
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
//        Log.d("yj", "getSwipeEscapeVelocity")
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        // 스와이프로 간주되기 위해 사용자가 뷰를 움직여야 하는 비율 반환
        // 5f 이상 스와이프 시 끝까지 스와이프가 된다.
//        Log.d("yj", "getSwipeThreshold")

        setTag(viewHolder, currentDx <= -holdingWidth)

        return 2f
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)  {
            val view = getView(viewHolder)
            val isHold = getTag(viewHolder)      // 고정할지 말지 결정, true : 고정함 false : 고정 안 함
            val newX = holdViewHolderPositionWidth(dX, isHold, isCurrentlyActive)  // newX 만큼 이동(고정 시 이동 위치/고정 해제 시 이동 위치 결정)

            currentDx = newX
            currentPosition = viewHolder.bindingAdapterPosition
            // ItemTouchHelper 항목 변환을 처리하는 유틸리티 클래스, ViewHolder의 하위 항목
            getDefaultUIUtil().onDraw(c, recyclerView, view, newX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        // 사용자 상호작용이 끝나고 애니메이션도 완료되었을 때 ItemTouchHelper에 의해 호출
        // onSelectChanged, onChildDraw 에서 수행 된 모든 변경 사항을 지운다
//        Log.d("yj", "clearView")
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View = viewHolder.itemView.findViewById(R.id.swipe_view)

    private fun holdViewHolderPositionWidth(
        dX: Float,  // 행동에 따른 수평 변위량
        isHold: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {

        val max = 0f                   // 최대 값 : 스와이프 안된 상태
        val min = -holdingWidth     // 최소 값 : 버튼 크기 + 바운딩 줄 크기

        val x = if (isHold) {
            // 고정 중
            if(isCurrentlyActive) {
                // swipe 동작중 일 때
                if(dX > 0) {
                    // 오른쪽 swipe 일 때
                    0f
                } else {
                    // 왼쪽 swipe 일 때
                    dX-holdingWidth
                }
            } else {
                dX-holdingWidth
            }
        } else {
            // 고정 중이 아닐 경우
            dX
        }
        // dx 값은 최대 view 의 넓이를 넘지 않아야 함 (바운딩 되는 느낌 주려면 dx값 조정)
        val newX = max(min, x)

        return min(newX, max)       // 왼쪽 스와이프 시 작은 값 넘기기
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isHold: Boolean) {
        // holding 여부 세팅
        viewHolder.itemView.tag = isHold
    }

    fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        // holding 여부 반환
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setHoldingWidth(clamp: Float) {
        Log.d("yj", "holderWidth : $clamp")
        this.holdingWidth = clamp }

    fun getCurrentPosition() : Int? {
        return currentPosition
    }

    fun removeHolding(recyclerView: RecyclerView) {
        // 현재 아이템 고정 제거
        currentPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).animate().translationX(0f).duration = 200L
            setTag(viewHolder, false)
            currentPosition = null
        }
    }
}