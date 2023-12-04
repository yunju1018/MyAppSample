package com.example.myappsample.popupwindow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.databinding.DataBindingUtil
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivityPopupWindowBinding

class PopupWindowActivity : AppCompatActivity() {

    lateinit var binding : ActivityPopupWindowBinding
    private var spinnerList = listOf<String>()
    private var sortPopup : ListPopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_popup_window)

        setLayout()

    }

    private fun setLayout() {
        spinnerList = makeDropDownList()

        setSortPopupItem()

        binding.sortTextView.text = spinnerList[0]
        spinnerList.forEach{
            Log.d("yj", it)
        }
        binding.sortTextView.setOnClickListener {
            sortPopup?.show()
        }
    }

    private fun setSortPopupItem() {
        sortPopup = ListPopupWindow(this).apply {
            isModal = true
            anchorView = binding.sortTextView
            width = binding.sortTextView.width
            promptPosition = ListPopupWindow.POSITION_PROMPT_BELOW
            setAdapter(ArrayAdapter(this@PopupWindowActivity, R.layout.spinner_item_view, spinnerList))
        }

        sortPopup?.setOnItemClickListener { parent, view, position, id ->
            binding.sortTextView.text = spinnerList[position]
            Log.d("yj", "setOnItemClickListener")
        }
    }

    private fun makeDropDownList(): List<String> {
        val list = listOf("유효기간 임박순", "최근 등록일순", "낮은 가격순", "높은 가격순")
        return list
    }
}