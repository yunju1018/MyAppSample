package com.example.myappsample.collasingtoolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivityBehaviorTestBinding
import com.google.android.material.appbar.AppBarLayout

class CollasingToolbarActivity : AppCompatActivity() {
    lateinit var binding: ActivityBehaviorTestBinding
    private var isImageVisible = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_behavior_test)


        val minImageHeight = resources.getDimensionPixelSize(R.dimen.image_min_height) // 최소 이미지 높이

        binding.appbarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val ratio = 1 - Math.abs(verticalOffset / appBarLayout.totalScrollRange.toFloat())
            val newHeight = Math.max((resources.getDimension(R.dimen.image_max_height) * ratio).toInt(), minImageHeight)

            val params = binding.imageView.layoutParams
            params.height = newHeight
            binding.imageView.layoutParams = params

//            // 스크롤 위치가 일정 값 이하로 내려갔을 때 이미지뷰를 숨김
//            if (newHeight <= minImageHeight) {
//                if (isImageVisible) {
//                    isImageVisible = false
//                }
//            } else {
//                if (!isImageVisible) {
//                    isImageVisible = true
//                }
//            }
        })

    }
}