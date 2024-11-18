package com.example.myappsample.stickydecoration

import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @ Source : https://stackoverflow.com/questions/32949971/how-can-i-make-sticky-headers-in-recyclerview-without-external-lib/44327350#44327350
 *            https://gist.github.com/filipkowicz/1a769001fae407b8813ab4387c42fcbd
 */

// yj : 헤더 밀어 올리는 코드 임시 저장
class StickyPushHeaderItemDecoration(
    val parent: RecyclerView,
    val onHeaderClickListener: (headerPosition: Int) -> Unit,       // 헤더 클릭 시 포지션 전달
    private val isHeader: (itemPosition: Int) -> Boolean            // 헤더 여부 전달
) : RecyclerView.ItemDecoration() {

    companion object {
        private val TAG: String = StickyPushHeaderItemDecoration::class.java.simpleName
    }

    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null     // first : header Position, second : ViewHolder

    init {
        // 스티키 헤더 클릭 시 이벤트 전달
        parent.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(
                recyclerView: RecyclerView,
                motionEvent: MotionEvent
            ): Boolean {
                return if (motionEvent.action == MotionEvent.ACTION_UP) {
                    currentHeader?.let {
                        val headerView = it.second.itemView
                        Log.d(TAG, "motionEvent.y: ${motionEvent.y}, headerViewBottom : ${headerView.bottom}")
                        // 터치 좌표가 헤더 범위 내에 있을 경우
                        if (0 < motionEvent.y && motionEvent.y <= headerView.bottom) {
                            Log.d(TAG, "headerPosition : ${it.first}")
                            onHeaderClickListener.invoke(it.first)
                            return true
                        }
                    }
                    return false
                } else false
            }
        })
    }

    // 새로운 헤더가 있을 경우 헤더를 이동 시키고, 그렇지 않으면 기존 헤더를 그린다.
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        // 최상단 뷰 포지션 찾기
        val topChild = parent.getChildAt(0) ?: return
        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        // 헤더 생성
        val headerView = getHeaderViewForItem(topChildPosition, parent) ?: return

        // 만나는 지점 확인
        val contactPoint = headerView.bottom + parent.paddingTop
        val childInContact = getChildInContact(parent, contactPoint) ?: return

        // 만나는 지점의 뷰가 header 일 경우 기존 header 이동
        if (isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, headerView, childInContact)
            return
        }

        // 헤더 그리기
        drawHeader(c, headerView, parent.paddingTop)
    }

    // 헤더가 이미 있으면 해당 해더 리턴, 아니면 새로운 헤더를 생성 리턴.
    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) return null

        // 가장 가까운 헤더 포지션
        val headerPosition = getHeaderPositionForItem(itemPosition)
        if (headerPosition == RecyclerView.NO_POSITION) return null

        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null

        // currentHeader 저장된 값이 같으면 해당 itemView 반환
        if (currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType) {
            return currentHeader?.second?.itemView
        }

        // 헤더 뷰홀더 생성, 리턴
        val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
        headerHolder?.let {
            parent.adapter?.onBindViewHolder(it, headerPosition)

//            // 간편 결제 StickyHeader 생성 시 상단 divider Gone
//            // (divider 있는 상태로 기존 StickyHeader 밀고, 고정 시점에 divider 숨김 원할 시 적용)
//            if (it is PayCreditCardSettingAdapter.StickyHeaderViewHolder) it.binding.divider.visibility = View.GONE

            fixLayoutSize(parent, it.itemView)
            currentHeader = headerPosition to it    // 헤더 저장
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

    // 현재 헤더와 다음 헤더가 겹칠 경우, 헤더 위로 밀기
    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(c)
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
                Log.d(TAG, "getHeaderPositionForItem() currentPosition: $currentPosition")
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)

        return headerPosition
    }
}