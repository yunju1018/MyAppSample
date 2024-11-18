package com.example.myappsample.stickydecoration

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class StickyStackHeaderItemDecoration(
    val onHeaderClickListener: (headerPosition: Int, offset: Int?) -> Unit,       // 헤더 클릭 시 포지션 전달
    private val isHeader: (itemPosition: Int) -> Boolean                          // 헤더 여부 전달
) : RecyclerView.ItemDecoration() {

    companion object {
        private val TAG: String = StickyStackHeaderItemDecoration::class.java.simpleName
    }

    // Pair<first : header Position, second : ViewHolder>
    private var firstHeader: Pair<Int, RecyclerView.ViewHolder>? = null
    private var secondHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    private var firstHeaderDrawn = false        // 첫 번째 헤더가 그려졌는지 추적하는 변수
    private var secondHeaderDrawn = false       // 두 번째 헤더가 그려졌는지 추적하는 변수

    private var simplePayRecyclerView: RecyclerView? = null

    // 스티키 헤더 클릭 시 이벤트 전달 (헤더 터치 이벤트 받지 못해 해당 영역 터치 감지)
    private val itemTouchListener = object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(
            recyclerView: RecyclerView,
            motionEvent: MotionEvent
        ): Boolean {
            return if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (firstHeaderDrawn) {
                    firstHeader?.let {
                        val headerView = it.second.itemView
                        // 헤더 뷰가 최 상단에 있고, 터치 좌표가 헤더 범위 내에 있을 경우
                        if (headerView.top == recyclerView.paddingTop && 0 < motionEvent.y && motionEvent.y <= headerView.bottom) {
                            Log.d(TAG, "motionEvent.y: ${motionEvent.y}, headerViewBottom : ${headerView.bottom}")
                            onHeaderClickListener.invoke(it.first, null)
                            return true
                        }
                    }
                }
                if (secondHeaderDrawn) {
                    secondHeader?.let {
                        val headerView = it.second.itemView
                        // 터치 좌표가 헤더 범위 내에 있을 경우
                        if (0 < motionEvent.y && motionEvent.y <= headerView.bottom + (firstHeader?.second?.itemView?.bottom?:0)) {
                            Log.d(TAG, "motionEvent.y: ${motionEvent.y}, headerViewBottom : ${headerView.bottom + (firstHeader?.second?.itemView?.bottom?:0)}")
                            onHeaderClickListener.invoke(it.first, firstHeader?.second?.itemView?.bottom?:0)
                            secondHeaderDrawn = false
                            return true
                        }
                    }
                }
                return false
            } else false
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        // recyclerView 초기화
        if (simplePayRecyclerView == null) {
            simplePayRecyclerView = parent.apply {
                addOnItemTouchListener(itemTouchListener)
            }
        }

        // 헤더 그려진 상태 초기화
        firstHeaderDrawn = false
        secondHeaderDrawn = false

        // 최상단 뷰 포지션 확인
        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        // 헤더 생성
        val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return

        // 만나는 지점 확인
        val contactPoint = headerView.bottom + parent.paddingTop
        val childInContact = getChildInContact(parent, contactPoint) ?: return

        // 첫번째 헤더 그리기
        firstHeader?.second?.itemView?.let {
            drawHeader(c, it, parent.paddingTop)
            firstHeaderDrawn = true         // 첫 번째 헤더가 그려지면 true 설정
        }

        // 만나는 지점의 뷰가 header 일 경우 첫번째 header 아래 그리기
        if (isHeader(parent.getChildAdapterPosition(childInContact))) {
            drawHeader(c, childInContact, headerView.height)
            secondHeaderDrawn = true       // 두 번째 헤더가 그려지면 true 설정
            return
        }

        // 헤더 위치 중복 확인
        if (firstHeader?.first == secondHeader?.first) return

        // 헤더 position 설정
        Log.d(TAG, "topChildPosition : $topChildPosition")
        // 현재 최 상단에 있는 item 포지션과 제일 가까운 헤더의 포지션
        val firstHeaderPosition = firstHeader?.first ?: getHeaderPositionForItem(topChildPosition)

        // 현재 최 상단 다음에 있는 item 포지션과 제일 가까운 헤더의 포지션
        val secondHeaderPosition = getHeaderPositionForItem(topChildPosition + 1)

        // 첫 번째 헤더 아래에 두 번째 헤더 그리기
        if (secondHeaderPosition != RecyclerView.NO_POSITION && secondHeaderPosition != firstHeaderPosition) {
            secondHeader?.second?.itemView?.let { currentHeaderView ->
                val secondContactPoint = firstHeader?.second?.itemView?.bottom ?: parent.paddingTop
                drawHeader(c, currentHeaderView, secondContactPoint)
                secondHeaderDrawn = true
            }
        }
    }


    // 헤더가 이미 있으면 해당 해더 리턴, 아니면 새로운 헤더를 생성 리턴.
    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) return null

        // 가장 가까운 헤더 포지션
        val headerPosition = getHeaderPositionForItem(itemPosition)
        if (headerPosition == RecyclerView.NO_POSITION) return null

        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null

        // secondHeader 저장된 값이 같으면 해당 itemView 반환
        if (secondHeader?.first == headerPosition && secondHeader?.second?.itemViewType == headerType) {
            return secondHeader?.second?.itemView
        }

        // 헤더 뷰홀더 생성, 리턴
        val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
        headerHolder?.let {
            parent.adapter?.onBindViewHolder(it, headerPosition)

            fixLayoutSize(parent, it.itemView)

            // 헤더 저장
            if (firstHeader == null) firstHeader = headerPosition to it
            else if (secondHeader?.first != headerPosition) secondHeader = headerPosition to it
        }

        return headerHolder?.itemView
    }

    // 헤더 그리기
    private fun drawHeader(c: Canvas, header: View, paddingTop: Int) {
        c.save()
        c.translate(0f, paddingTop.toFloat())
        header.draw(c)
        c.restore()
    }

    // 만나는 지점 자식 뷰 찾아 헤더가 겹치지 않도록 처리.
    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            if (mBounds.bottom > contactPoint) {
                if (mBounds.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    // 측정
    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        Log.i(TAG, "fixLayoutSize() called")
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    // 가장 가까운 헤더의 position 전달 yj : (현재 position 부터 -1 do-while 돌려서 가장 가까운 위치의 헤더 값 가져옴)
    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition
        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return headerPosition
    }
}