package com.example.myappsample.chipAnimation

import android.animation.*
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.myappsample.R

class AnimationUtil {
    companion object {
        private val TAG = "yj : ${AnimationUtil::class.java.simpleName}"

        @JvmStatic
        fun animatedFadeOutIn(context: Context, textView: TextView, text: String) {
            // TextView fadeOut
            val fadeOut: ValueAnimator = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0.1f)
            fadeOut.duration = 600
            fadeOut.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    textView.text = text
                    textView.setTextColor(ContextCompat.getColor(context, R.color.green))
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            val originWidth = textView.width
            val targetWidth =
                textView.paint.measureText(text).toInt() + dpToPx(context, 32)
            Log.d(TAG, "originWidth size :$originWidth")
            Log.d(TAG, "TargetWidth size :$targetWidth")

            // TextView width
            val animator = ValueAnimator.ofInt(originWidth, targetWidth)
            animator.duration = 600 // Animation duration in milliseconds
            animator.addUpdateListener { animation ->
                val newWidth = animation.animatedValue as Int
                val params = textView.layoutParams
                params.width = newWidth
                textView.layoutParams = params
            }

            // TextView fadeIn
            val fadeIn: ValueAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0.1f, 1f)
            fadeIn.duration = 600

            // TextView color dissolve 800
            val greenAnimator = ValueAnimator.ofObject(
                ArgbEvaluator(), ContextCompat.getColor(
                    context, R.color.green
                ), ContextCompat.getColor(context, R.color.green)
            )
            greenAnimator.duration = 600 // Animation duration in milliseconds
            val valueAnimator = ValueAnimator.ofObject(
                ArgbEvaluator(), ContextCompat.getColor(
                    context, R.color.green
                ), ContextCompat.getColor(context, R.color.black)
            )
            valueAnimator.duration = 200 // Animation duration in milliseconds
            valueAnimator.addUpdateListener { animation -> textView.setTextColor((animation.animatedValue as Int)) }
            val animatorSet = AnimatorSet()
            animatorSet.play(animator).with(fadeOut)
            animatorSet.play(fadeIn).after(fadeOut)
            animatorSet.play(greenAnimator).after(fadeIn)
            animatorSet.play(valueAnimator).after(greenAnimator)
            animatorSet.start()
        }

        @JvmStatic
        fun animatedResize(context: Context, view: View) {
            // View fadeOut
            val fadeOut: ValueAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.2f)
            fadeOut.duration = 600

            // View width
            val originWidth = view.width
            val targetWidth = 0
            Log.d(TAG, "originWidth size :$originWidth")
            Log.d(TAG, "TargetWidth size :$targetWidth")

            // TextView width
            val animator = ValueAnimator.ofInt(originWidth, targetWidth)
            animator.duration = 600 // Animation duration in milliseconds
            animator.addUpdateListener { animation ->
                val newWidth = animation.animatedValue as Int
                val params = view.layoutParams
                params.width = newWidth
                view.layoutParams = params
            }

            val animatorSet = AnimatorSet()
            animatorSet.play(animator).with(fadeOut)
            animatorSet.start()
        }

        private fun dpToPx(context: Context, dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }

        @JvmStatic
        fun progressAnimation(progressbar: ProgressBar, savedStarCount: Int, textView: TextView) {
            val duration = 1000L
            val multiply = 100
            progressbar.max = 12 * multiply

            // progressBar
            val progressAnimation: ValueAnimator = ObjectAnimator.ofInt(progressbar, "progress", 0, savedStarCount * multiply)
            progressAnimation.duration = duration
            progressAnimation.addUpdateListener {
                val animationValue = it.animatedValue as Int
                progressbar.progress = animationValue
            }

            // delay
            val delayAnimator = ValueAnimator.ofInt(0,0)
            delayAnimator.duration = 500

            // progressBar width
            val originWidth = progressbar.width
            val targetWidth = 0
            val widthAnimation = ValueAnimator.ofInt(originWidth, targetWidth)
            widthAnimation.duration = duration
            widthAnimation.addUpdateListener {
                val newWidth = it.animatedValue as Int
                val params = progressbar.layoutParams
                params.width = newWidth
                progressbar.layoutParams = params
            }

            // progressBar fadeOut
            val fadeOut: ValueAnimator = ObjectAnimator.ofFloat(progressbar, "alpha", 1f, 0.2f)
            fadeOut.duration = duration

            // textUp
            val value = textView.measuredHeight
            val textViewAnimatorSet = ObjectAnimator.ofFloat(textView, "translationY", value.toFloat(), -value.toFloat())
            val textChangeDuration = duration/savedStarCount
            textViewAnimatorSet.duration = textChangeDuration

            var count = 1

            // 숫자 갱신 및 애니메이션 설정
            textViewAnimatorSet.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator) {
                    textView.text = count.toString()
                }

                override fun onAnimationEnd(p0: Animator) {
                    if (count < savedStarCount-1) {
                        count++
                        textView.text = count.toString()
                        textViewAnimatorSet.start()
                        Log.d("yj", "count : $count")
                    } else if (count == savedStarCount-1) {
                        val newAnimatorSet = ObjectAnimator.ofFloat(textView, "translationY", value.toFloat(), 0f)
                        newAnimatorSet.duration = textChangeDuration
                        count++
                        Log.d("yj", "count : $count")
                        textView.text = count.toString()
                        newAnimatorSet.start()
                    }
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }
            })

            val animatorSet = AnimatorSet()
            animatorSet.play(progressAnimation).with(textViewAnimatorSet)
            animatorSet.play(delayAnimator).after(progressAnimation)
            animatorSet.play(widthAnimation).with(fadeOut).after(delayAnimator)
            animatorSet.startDelay = 500

            animatorSet.start()
        }
    }
}