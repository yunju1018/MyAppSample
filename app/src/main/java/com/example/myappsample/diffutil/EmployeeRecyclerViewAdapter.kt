package com.example.myappsample.diffutil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myappsample.R
import com.example.myappsample.databinding.DiffUtilListItemBinding

class EmployeeRecyclerViewAdapter(
    employee: List<Employee>
): RecyclerView.Adapter<EmployeeRecyclerViewAdapter.ViewHolder>() {

    private val mEmployee = ArrayList<Employee>()

    init {
        mEmployee.addAll(employee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: DiffUtilListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.diff_util_list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = mEmployee.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mEmployee[position])
    }

    fun setData(employee: List<Employee>) {
        mEmployee.clear()
        mEmployee.addAll(employee)
        notifyDataSetChanged()
    }

    fun updateEmployeeListItems (employee: List<Employee>) {
        val diffCallback = DiffUtilCallback(mEmployee, employee)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mEmployee.clear()
        mEmployee.addAll(employee)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: DiffUtilListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Employee) {
            binding.employeeName.text = data.name
            binding.employeeRole.text = data.role
        }
    }
}