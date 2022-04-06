package com.example.trabits.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.trabits.fragments.BadHabitsFragment
import com.example.trabits.fragments.GoodHabitsFragment

class HabitListPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> GoodHabitsFragment.newInstance()
        else -> BadHabitsFragment.newInstance()
    }
}