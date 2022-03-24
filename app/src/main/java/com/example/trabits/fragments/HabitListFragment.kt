package com.example.trabits.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.trabits.HabitListPagerAdapter
import com.example.trabits.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.habit_list_fragment.*

class HabitListFragment : Fragment(R.layout.habit_list_fragment) {

    private val navController: NavController by lazy {
        Navigation.findNavController(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        habit_list_viewpager.adapter = HabitListPagerAdapter(this)
        val tabNames = listOf(getString(R.string.good), getString(R.string.bad))

        TabLayoutMediator(habit_list_tablayout, habit_list_viewpager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        to_edit_habit_button.setOnClickListener {
            val action =
                HabitListFragmentDirections.actionHabitListFragmentToHabitCustomizeFragment(
                    getString(R.string.label_add)
                )
            navController.navigate(action)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}