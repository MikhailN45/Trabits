package com.example.trabits.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.trabits.Habit
import com.example.trabits.HabitAdapter
import com.example.trabits.R
import kotlinx.android.synthetic.main.bad_habits_fragment.*

class BadHabitsFragment : Fragment(R.layout.bad_habits_fragment) {

    private val adapter: HabitAdapter by lazy {
        val data = ArrayList<Habit>()
        HabitAdapter(data, requireContext())
    }

    private val habitsLiveData by lazy {
        FakeDatabase.habitsLiveData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bad_habits_recycler.adapter = adapter

        habitsLiveData.observe(viewLifecycleOwner) { habitsList ->
            adapter.addListOfHabits((habitsList.filter { !(it.type.toBoolean()) }).toMutableList())
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun Int.toBoolean(): Boolean = this == 1

    companion object {

        fun newInstance(): BadHabitsFragment = BadHabitsFragment()
    }
}