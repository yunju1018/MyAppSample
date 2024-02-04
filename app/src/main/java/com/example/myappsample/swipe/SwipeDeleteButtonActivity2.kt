package com.example.myappsample.swipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.myappsample.databinding.ActivitySwipeDeleteBinding
import com.example.myappsample.toastsnackbar.ToastSnackBar

/**
 * https://velog.io/@nimok97/RecyclerView-%EC%97%90%EC%84%9C-item-Swipe-%ED%95%98%EA%B8%B0-feat.-ItemTouchHelper-ItemTouchUIUtil
 */

class SwipeDeleteButtonActivity2 : AppCompatActivity() {

    private lateinit var binding : ActivitySwipeDeleteBinding
    private lateinit var swipeAdapter: SwipeAdapter
    private var list : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwipeDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        list = makeList()
        swipeAdapter = SwipeAdapter(list)

        setUpRecyclerViewYj()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpRecyclerViewYj() {

        binding.recyclerView.apply {
            adapter = swipeAdapter
            addItemDecoration(DividerItemDecoration(this@SwipeDeleteButtonActivity2, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(this@SwipeDeleteButtonActivity2)
        }

        val swipeHelper = SwipeHelper2(binding.recyclerView, 60 * resources.displayMetrics.density, onSwipeClear)
        // 버튼탭이 열려있을때는 터치(클릭)에 반응하지 않는다.
        // 버튼 탭이 닫혀있을 때는 뷰홀더 온클릭 리스너가 동작한다. -> 터치리스너 return false

        binding.recyclerView.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // ItemTouchHelper 동작 시 (Swipe 시) action Up 동작 X
                swipeHelper.getCurrentPosition()?.let {
                    // 스와이프 된 View가 null이 아니고
                    rv.findChildViewUnder(e.x, e.y)?.let {
                        // 리싸이클러뷰의 선택 된 뷰가 null이 아닐 떄
                        val selectHolder = rv.getChildViewHolder(it)
                        if(selectHolder.bindingAdapterPosition != swipeHelper.getCurrentPosition()) {
                            // 선택된 아이템 위치와 헬퍼의 아이템 위치가 다르면 닫기
                            swipeHelper.removeHolding(rv)
                            swipeAdapter.removeItem(true)
                        } else {
                            if(e.action == MotionEvent.ACTION_UP) {
                                // 선택된 아이템 위치와 헬퍼의 아이템 위치가 같지만, UP 이벤트 들어 왔을 경우에는 닫기.
                                swipeHelper.removeHolding(rv)
                                swipeAdapter.removeItem(true)
                            }
                            // 선택된 아이템 위치와 헬퍼 위치가 같다면 유지
                        }
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                // 터치 이벤트 가로채는것을 원하지 않으면 true
            }
        })
    }

    private val onSwipeClear = object : SwipeHelper2.OnSwipeClearListener {
        override fun onSwipeClear() {
            ToastSnackBar.make(binding.snackBarArea, "스와이프 안되는 뷰").show()
        }
    }

    private fun makeList() : ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 0 until 100) {
            val str = "item $i 스와이프 삭제 테스트 "
            arrayList.add(str)
        }
        return arrayList
    }
}