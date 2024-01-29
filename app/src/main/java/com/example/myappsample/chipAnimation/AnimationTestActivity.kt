package com.example.myappsample.chipAnimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.R
import com.example.myappsample.chipAnimation.AnimationUtil.Companion.animatedResize
import com.example.myappsample.databinding.ActivityMotionTestBinding

class AnimationTestActivity: AppCompatActivity() {
    private val TAG = "yj : " + AnimationTestActivity::class.java.simpleName
    private lateinit var binding: ActivityMotionTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_motion_test)

        testViewResize()
        testRecyclerViewResize()
    }

    private fun testViewResize() {
        binding.motionView.setOnClickListener {
            animatedResize(
                this,
                binding.colorView
            )
        }
    }

    private fun testRecyclerViewResize() {
        val str = arrayListOf<String>("pay", "coupon", "myGiftBook", "NTF")

        val adapter = AnimationAdapter(str)
        binding.recyclerview.apply {
            this.adapter = adapter
        }
    }

    class AnimationAdapter(
        val mList: ArrayList<String>,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            const val REWARD_VIEW_TYPE = 0
            const val CONTENT_VIEW_TYPE = 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType) {
                REWARD_VIEW_TYPE -> {
                    val view: View = LayoutInflater.from(parent.context).inflate(R.layout.motion_option_item_1, parent, false)
                    return AnimationViewHolder(view)
                }
                else -> {
                    val view: View = LayoutInflater.from(parent.context).inflate(R.layout.motion_option_item_2, parent, false)
                    return TextViewHolder(view)
                }
            }

        }

        override fun getItemCount() = mList.size + 1

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when(holder) {
                is TextViewHolder -> {
                    holder.bind(mList[position-1])
                }
                is AnimationViewHolder -> {
                    holder.bind()
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if(position == 0) {
                //header
                REWARD_VIEW_TYPE
            } else {
                //아이템
                CONTENT_VIEW_TYPE
            }
        }

        inner class AnimationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val motionViewItem = view.findViewById<ConstraintLayout>(R.id.motionViewItem)
            private val rewardBar = view.findViewById<View>(R.id.rewardBar)

            fun bind() {
                motionViewItem.setOnClickListener {
                    animatedResize(itemView.context, rewardBar)
                }
            }
        }

        inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val textView = view.findViewById<TextView>(R.id.textView)

            fun bind(message: String) {
                textView.text = message
            }
        }
    }

// viewtreeobserver 찾아보기
}