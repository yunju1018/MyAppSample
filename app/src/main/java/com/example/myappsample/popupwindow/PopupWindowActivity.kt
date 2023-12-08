package com.example.myappsample.popupwindow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivityPopupWindowBinding
import com.example.myappsample.databinding.SpinnerItemViewBinding

class PopupWindowActivity : AppCompatActivity() {

    lateinit var binding : ActivityPopupWindowBinding
    private var spinnerList = listOf<String>()
    private var sortPopup : ListPopupWindow? = null
    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_popup_window)

        setLayout()

    }

    private fun setLayout() {
        spinnerList = makeDropDownList()
        setSortPopupItem()
        binding.sortTextView.text = spinnerList[0]
        binding.sortTextView.setOnClickListener {
            popupWindow?.showAsDropDown(binding.sortTextView,0,0)
        }
    }

    private fun setSortPopupItem() {
        // ListPopupWindow 띄우기
        val listAdapter = ArrayAdapter(this@PopupWindowActivity, R.layout.spinner_item_view, spinnerList)
        sortPopup = ListPopupWindow(this).apply {
            isModal = true
            anchorView = binding.sortTextView
            width = ListPopupWindow.WRAP_CONTENT
            height = ListPopupWindow.WRAP_CONTENT
            promptPosition = ListPopupWindow.POSITION_PROMPT_BELOW
            verticalOffset = -30
            horizontalOffset = 24
            setAdapter(listAdapter)
        }

        sortPopup?.setOnItemClickListener { parent, view, position, id ->
            binding.sortTextView.text = spinnerList[position]
            Log.d("yj", "setOnItemClickListener")
            sortPopup?.dismiss()
        }

        // PopupWindow 띄우기
        val popupView = LayoutInflater.from(this).inflate(R.layout.spinner_layout, null)
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.spinnerRecyclerView)
        recyclerView.adapter = SortListAdapter(spinnerList)
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayout.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        popupWindow = PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun makeDropDownList(): List<String> {
        val list = listOf("유효기간 임박순", "최근 등록일순", "낮은 가격순", "높은 가격순")
        return list
    }

    class SortListAdapter(val sortList: List<String>) : RecyclerView.Adapter<SortListAdapter.SortListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortListViewHolder {
            val binding : SpinnerItemViewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.spinner_item_view, parent, false)
            return SortListViewHolder(binding)
        }

        override fun getItemCount() = sortList.size

        override fun onBindViewHolder(holder: SortListViewHolder, position: Int) {
            holder.bind(sortList[position])
        }

        inner class SortListViewHolder(val binding:SpinnerItemViewBinding) : ViewHolder(binding.root) {
            fun bind(item : String){
                binding.textView.text = item
            }
        }
    }
}