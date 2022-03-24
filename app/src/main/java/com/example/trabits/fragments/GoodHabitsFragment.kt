package com.example.trabits.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.trabits.Habit
import com.example.trabits.HabitAdapter
import com.example.trabits.R
import kotlinx.android.synthetic.main.good_habits_fragment.*

class GoodHabitsFragment : Fragment(R.layout.good_habits_fragment) {

    private val adapter: HabitAdapter by lazy {
        val data = ArrayList<Habit>()
        HabitAdapter(data, requireContext())
    }

    private val habitsLiveData by lazy {
        FakeDatabase.habitsLiveData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        good_habits_recycler.adapter = adapter
        habitsLiveData.observe(viewLifecycleOwner) { habitsList ->
            adapter.addListOfHabits((habitsList.filter {
                it.type.toBoolean()
            }.toMutableList()))
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun Int.toBoolean(): Boolean = this == 1

    companion object {
        fun newInstance(): GoodHabitsFragment = GoodHabitsFragment()
    }
}
