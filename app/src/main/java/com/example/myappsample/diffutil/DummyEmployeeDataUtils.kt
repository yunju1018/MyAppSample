package com.example.myappsample.diffutil

import java.util.Collections

class DummyEmployeeDataUtils {

    companion object {
        fun getEmployeeListSortedByName(): List<Employee> {
            val employeeList = getEmployeeList()
            Collections.sort(employeeList, object : Comparator<Employee> {
                override fun compare(a1: Employee, a2: Employee): Int {
                    return a1.name.compareTo(a2.name)
                }
            })
            return employeeList
        }

        fun getEmployeeListSortedByRole(): List<Employee> {
            val employeeList = getEmployeeList()
            Collections.sort(employeeList, object : Comparator<Employee> {
                override fun compare(a1: Employee, a2: Employee): Int {
                    return a2.role.compareTo(a1.role)
                }
            })
            return employeeList
        }

        private fun getEmployeeList(): List<Employee> {
            val employees: MutableList<Employee> = ArrayList()
            employees.add(Employee(1, "Employee 1", "Developer"))
            employees.add(Employee(2, "Employee 2", "Tester"))
            employees.add(Employee(3, "Employee 3", "Support"))
            employees.add(Employee(4, "Employee 4", "Sales Manager"))
            employees.add(Employee(5, "Employee 5", "Manager"))
            employees.add(Employee(6, "Employee 6", "Team lead"))
            employees.add(Employee(7, "Employee 7", "Scrum Master"))
            employees.add(Employee(8, "Employee 8", "Sr. Tester"))
            employees.add(Employee(9, "Employee 9", "Sr. Developer"))
            return employees
        }
    }
}