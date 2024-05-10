package com.example.myappsample.diffutil

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myappsample.R
import com.example.myappsample.databinding.ActivityDiffUtilTestBinding

class DiffUtilTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiffUtilTestBinding
    private lateinit var mAdapter: EmployeeRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diff_util_test)

        init()
    }

    private fun init() {
        mAdapter = EmployeeRecyclerViewAdapter(DummyEmployeeDataUtils.getEmployeeListSortedByName())
        binding.recyclerView.apply {
            adapter = mAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.sort_by_name -> {
                mAdapter.updateEmployeeListItems(DummyEmployeeDataUtils.getEmployeeListSortedByName())
//                mAdapter.setData(DummyEmployeeDataUtils.getEmployeeListSortedByName())
                return true
            }
            R.id.sort_by_role -> {
                mAdapter.updateEmployeeListItems(DummyEmployeeDataUtils.getEmployeeListSortedByRole())
//                mAdapter.setData(DummyEmployeeDataUtils.getEmployeeListSortedByRole())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}