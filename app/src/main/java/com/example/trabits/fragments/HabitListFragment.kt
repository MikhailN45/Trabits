package com.example.trabits.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.trabits.adapters.HabitListPagerAdapter
import com.example.trabits.R
import com.example.trabits.databinding.HabitListFragmentBinding
import com.example.trabits.models.FilterTypes
import com.example.trabits.viewmodels.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.bottom_sheet_main_fragment.*

class HabitListFragment : Fragment(R.layout.habit_list_fragment) {
    private var _binding: HabitListFragmentBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController by lazy {
        Navigation.findNavController(requireView())
    }

    private val viewModel: HabitListViewModel by activityViewModels()

    private val filterTypes by lazy {
        resources.getStringArray(R.array.filterTypes)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HabitListFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.habitListViewpager.adapter = HabitListPagerAdapter(this)
        val tabNames = listOf(getString(R.string.good), getString(R.string.bad))

        TabLayoutMediator(binding.habitListTablayout, binding.habitListViewpager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        binding.toEditHabitButton.setOnClickListener {
            val action =
                HabitListFragmentDirections.actionHabitListFragmentToHabitCustomizeFragment(
                    getString(R.string.label_add)
                )
            navController.navigate(action)
        }

        filter_search_edit_text.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {

                if (text != null)
                    viewModel.sortHabits(text.toString())
                else
                    viewModel.cleanHabitsFilter()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        filter_sort_up_button.setOnClickListener {

            filter_sort_up_button.setImageResource(R.drawable.ic_baseline_arrow_upward_24_activated)
            filter_sort_down_button.setImageResource(R.drawable.ic_baseline_arrow_downward_24)

            when (filter_type_spinner.text.toString()) {

                filterTypes[0] -> {

                    viewModel.sortHabits(FilterTypes.ByPriority, true)

                }

                filterTypes[1] -> {
                    viewModel.sortHabits(FilterTypes.ByPeriod, true)
                }

                filterTypes[2] -> {
                    viewModel.sortHabits(FilterTypes.ByCount, true)
                }

                filterTypes[3] -> {
                    viewModel.sortHabits(FilterTypes.ByDate, true)
                }

            }
        }

        filter_sort_down_button.setOnClickListener {

            filter_sort_down_button.setImageResource(R.drawable.ic_baseline_arrow_downward_24_activated)
            filter_sort_up_button.setImageResource(R.drawable.ic_baseline_arrow_upward_24)

            when (filter_type_spinner.text.toString()) {

                filterTypes[0] -> {

                    viewModel.sortHabits(FilterTypes.ByPriority, false)
                }

                filterTypes[1] -> {
                    viewModel.sortHabits(FilterTypes.ByPeriod, false)
                }

                filterTypes[2] -> {
                    viewModel.sortHabits(FilterTypes.ByCount, false)
                }

                filterTypes[3] -> {
                    viewModel.sortHabits(FilterTypes.ByDate, false)
                }
            }
        }

        filter_type_spinner.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

                if (position == 4) {
                    filter_sort_up_button.setImageResource(R.drawable.ic_baseline_arrow_upward_24)
                    filter_sort_down_button.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                    viewModel.cleanHabitsFilter()
                }
            }

        val behavior = BottomSheetBehavior.from(bottom_sheet_habit_list_fragment)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        behavior.peekHeight = 0


        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.toEditHabitButton.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset)
                    .setDuration(0).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheet.hideKeyboard()
                }
                binding.toEditHabitButton.isClickable = newState == BottomSheetBehavior.STATE_COLLAPSED
            }
        })

        bottom_sheet_title_bar.setOnClickListener {
            if (behavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        val adapterFilterTypes =
            context?.let { ArrayAdapter(it, R.layout.spinner_list_item, filterTypes) }
        (filter_type_input_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterFilterTypes)
        filter_type_spinner.keyListener = null

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.find_and_sort) {
            bottom_sheet_habit_list_fragment?.let {
                val behavior = BottomSheetBehavior.from(bottom_sheet_habit_list_fragment)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return true
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
