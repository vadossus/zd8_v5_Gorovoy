package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3 // Number of fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BusManagementFragment()
            1 -> DriverManagementFragment()
            2 -> RouteManagementFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}