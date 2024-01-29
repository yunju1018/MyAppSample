package com.example.myappsample.chipAnimation

import android.animation.*
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
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
    }
}