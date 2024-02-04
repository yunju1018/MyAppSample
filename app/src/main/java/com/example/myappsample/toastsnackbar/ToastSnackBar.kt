package com.example.myappsample.toastsnackbar

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myappsample.R
import com.example.myappsample.databinding.ToastSnackbarLayoutBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


class ToastSnackBar (val view: View, private val message: String) {

    companion object {
        @JvmStatic
        fun make(view: View, message: String) = ToastSnackBar(view, message)

        private const val DURATION = 3000
    }

    private val context = view.context
    private lateinit var snackBar: Snackbar
    private lateinit var snackBarLayout :SnackbarLayout
    private lateinit var snackBarBinding: ToastSnackbarLayoutBinding

    init {
        initView()
    }

    private fun initView() {
        snackBar = Snackbar.make(view, message, DURATION)
        snackBarLayout = snackBar.view as SnackbarLayout
        snackBarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.toast_snackbar_layout, null, false)

        with(snackBarLayout) {
            removeAllViews()
//            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackBarBinding.root, 0)
        }
        with(snackBarBinding) {
            snackbarText.text = message
        }
    }

    /**
     * 토스트에 버튼 이벤트 처리 시 사용
     */
    fun setAction(drawableRes: Int?, listener: View.OnClickListener?) : ToastSnackBar {
        with(snackBarBinding) {
            drawableRes?.let {
                snackbarAction.visibility = View.VISIBLE
                snackbarAction.setImageResource(it)
            }

            listener?.let {
                snackbarAction.setOnClickListener {v ->
                    it.onClick(v)
                    snackBar.dismiss()
                }
            }
        }
        return this
    }

    fun show() {
        snackBar.animationMode = ANIMATION_MODE_FADE
        snackBar.show()
    }
}