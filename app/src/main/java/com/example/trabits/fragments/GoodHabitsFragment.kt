package com.example.trabits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.trabits.R
import com.example.trabits.adapters.HabitAdapter
import com.example.trabits.databinding.GoodHabitsFragmentBinding
import com.example.trabits.models.Habit
import com.example.trabits.viewmodels.HabitListViewModel

class GoodHabitsFragment : Fragment(R.layout.good_habits_fragment) {
    private var _binding: GoodHabitsFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: HabitAdapter by lazy {
        val data = ArrayList<Habit>()
        HabitAdapter(data, requireContext())
    }

    private val viewModel: HabitListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GoodHabitsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.goodHabitsRecycler.adapter = adapter

        viewModel.currentHabits.observe(viewLifecycleOwner, { habits ->
            habits?.let { list ->
                adapter.setData(
                    list.filter
                    {
                        it.type.toBoolean()
                    }.toMutableList()
                )
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun Int.toBoolean(): Boolean = this == 1

    companion object {
        fun newInstance(): GoodHabitsFragment = GoodHabitsFragment()
    }
}
