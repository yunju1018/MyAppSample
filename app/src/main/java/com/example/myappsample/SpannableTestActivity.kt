package com.example.myappsample

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myappsample.databinding.ActivitySpannableTestBinding

class SpannableTestActivity : AppCompatActivity() {
    lateinit var binding: ActivitySpannableTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_spannable_test)


        val textView = binding.textView

        // 이미지를 포함하는 문자열 리스트
        val textList = arrayListOf("첫번째" , "두번째" , "세번째" , "네번째", "다섯번","첫번째" , "두번째" , "세번째" , "네번째", "다섯번","첫번째" , "두번째" , "세번째" , "네번째", "다섯번","첫번째" , "두번째" , "세번째" , "네번째", "다섯번")
        // ... (계속해서 이미지를 추가)

        // SpannableStringBuilder를 사용하여 텍스트와 이미지 조합
        val builder = SpannableStringBuilder()

        for (text in textList) {
            // 이미지 리소스
            val imageDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)

            imageDrawable?.let {
                val imageSize = (textView.textSize * 1.5).toInt() // 텍스트 크기의 1.5배로 이미지 크기 설정

                // 이미지를 SpannableStringBuilder에 추가
                it.setBounds(0, 0, imageSize, imageSize)
                val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BOTTOM)

                // 이미지 앞에 텍스트를 추가하고 이미지를 이어붙임
                builder.append(text).append(" ")
                val start = builder.length - text.length - 1
                val end = builder.length

                builder.setSpan(imageSpan, end-1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                Log.d("yj", "builder.start : ${start}")
                Log.d("yj", "builder.end : ${end}")
                Log.d("yj", "builder.length : ${builder.length}")
            }
        }

        // 최종적으로 설정된 SpannableStringBuilder를 TextView에 설정
        Log.d("yj", "text : $builder")
        textView.text = builder
    }
}