package com.example.myappsample.chipAnimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.R
import com.example.myappsample.chipAnimation.AnimationUtil.Companion.animatedResize
import com.example.myappsample.databinding.ActivityMotionTestBinding
import com.yy.mobile.rollingtextview.CharOrder
import com.yy.mobile.rollingtextview.strategy.Direction
import com.yy.mobile.rollingtextview.strategy.Strategy


class AnimationTestActivity: AppCompatActivity() {
    private val TAG = "yj : " + AnimationTestActivity::class.java.simpleName
    private lateinit var binding: ActivityMotionTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_motion_test)

        testViewResize()
        testRecyclerViewResize()
        testSlotMachine()
        testSlotMachineRecyclerView()

        val rollingTextView1 = binding.rollingTextView1
        rollingTextView1.animationDuration = 1000
        rollingTextView1.charStrategy = Strategy.CarryBitAnimation(Direction.SCROLL_UP)
        rollingTextView1.addCharOrder(CharOrder.Number)
        rollingTextView1.animationInterpolator = AccelerateDecelerateInterpolator()
        rollingTextView1.setText("999")

//        val rollingTextView2 = binding.rollingTextView2
//        rollingTextView2.animationDuration = 1000
//        rollingTextView2.charStrategy = Strategy.CarryBitAnimation(Direction.SCROLL_UP)
//        rollingTextView2.addCharOrder(CharOrder.Number)
//        rollingTextView2.animationInterpolator = AccelerateDecelerateInterpolator()
//        rollingTextView2.setText("8")
//
//        val rollingTextView3 = binding.rollingTextView3
//        rollingTextView3.animationDuration = 1000
//        rollingTextView3.charStrategy = Strategy.CarryBitAnimation(Direction.SCROLL_UP)
//        rollingTextView3.addCharOrder(CharOrder.Number)
//        rollingTextView3.animationInterpolator = AccelerateDecelerateInterpolator()
//        rollingTextView3.setText("9")
    }

    private fun testViewResize() {
        binding.motionView.setOnClickListener {
            animatedResize(
                this,
                binding.colorView
            )
        }

        binding.rewardLayout.setOnClickListener {
            binding.rewardLayout.setProgressBarAnimation()
        }
    }

    private fun testRecyclerViewResize() {
        val str = arrayListOf<String>("pay", "coupon", "myGiftBook", "NTF")

        val adapter = AnimationAdapter(str)
        binding.recyclerview.apply {
            this.adapter = adapter
        }
    }

    private fun testSlotMachine() {

        val value = dpToPx(this, 50)
        val animatorSet = ObjectAnimator.ofFloat(binding.textView, "translationY", value.toFloat(), -value.toFloat())
        animatorSet.duration = 100

        var count = 1

        // 숫자 갱신 및 애니메이션 설정
        animatorSet.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                if (count < 9) {
                    count++
                    binding.textView.text = count.toString()
                    animatorSet.start()
                } else if (count == 9) {
                    val newAnimatorSet = ObjectAnimator.ofFloat(binding.textView, "translationY", value.toFloat(), 0f)
                    newAnimatorSet.duration = 100
                    count++
                    binding.textView.text = count.toString()
                    newAnimatorSet.start()
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }
        })

        animatorSet.start()
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
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
//            private val motionViewItem = view.findViewById<ConstraintLayout>(R.id.motionViewItem)
//            private val rewardBar = view.findViewById<View>(R.id.rewardBar)
            private val rewardLayout = view.findViewById<RewardsMyRewardsLayout>(R.id.rewardLayout)

            fun bind() {
                itemView.setOnClickListener {
                    rewardLayout.setProgressBarAnimation()
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

    private fun testSlotMachineRecyclerView() {
        val data = (1..10).map { it.toString() }
        val slotAdapter = SlotMachineAdapter(data)
        binding.slotRecyclerView.apply {
            adapter = slotAdapter
        }

        val smoothScroller: RecyclerView.SmoothScroller by lazy {
            object : LinearSmoothScroller(this) {
                override fun getVerticalSnapPreference() = SNAP_TO_START
            }
        }


        smoothScroller.targetPosition = data.size - 1

        binding.slotRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.slotRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                Handler(Looper.getMainLooper()).postDelayed({
                    val layoutManager = LinearLayoutManager(this@AnimationTestActivity)
                    binding.slotRecyclerView.layoutManager = layoutManager

                    // RecyclerView 마지막 위치로 스크롤
                    binding.slotRecyclerView.post {
                        binding.slotRecyclerView.smoothScrollToPosition(data.size-1)
                    }
                }, 1000)
            }
        })
    }

    class SlotMachineAdapter(private val data: List<String>) :
        RecyclerView.Adapter<SlotMachineAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView.findViewById(R.id.textView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_slot_machine, parent, false)
            Log.d("yj", "createViewHolder")
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textView.text = data[position]
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

// viewtreeobserver 찾아보기
}