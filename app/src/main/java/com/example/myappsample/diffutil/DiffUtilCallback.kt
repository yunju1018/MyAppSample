package com.example.myappsample.diffutil

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback(
    private val oldList: List<Employee>,
    private val newList: List<Employee>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // 두 아이템이 같은 객체인지 여부를 반환
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // 두 아이템이 같은 데이터를 가지고 있는지 여부를 반환, areItemTheSame true 반환할 때만 호출
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}