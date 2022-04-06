package com.example.trabits.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.trabits.*
import com.example.trabits.databinding.HabitCustomizeFragmentBinding
import com.example.trabits.models.Habit
import com.example.trabits.models.Util
import com.example.trabits.viewmodels.HabitCustomizeViewModel
import com.example.trabits.viewmodels.HabitListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class HabitCustomizeFragment : Fragment(R.layout.habit_customize_fragment) {
    private var _binding: HabitCustomizeFragmentBinding? = null
    private val binding get() = _binding!!
    private var chosenColor: Int = Util.intColors.getValue(16)
    private var chosenColorNumber = ColorPickerDialogFragment.DEFAULT_COLOR

    private val priorities: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.priorities)
    }

    private val periods: Array<String> by lazy {
        requireContext().resources.getStringArray(R.array.periods)
    }

    private val habitToCustomize: Habit? by lazy {
        HabitCustomizeFragmentArgs.fromBundle(requireArguments()).habitToEdit
    }

    private val navController: NavController by lazy {
        Navigation.findNavController(requireView())
    }

    private val habitsListViewModel: HabitListViewModel by activityViewModels()

    private val habitsCustomizeViewModel: HabitCustomizeViewModel by activityViewModels()

override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HabitCustomizeFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.discard_changes)
                .setMessage(R.string.unsaved_changes)
                .setPositiveButton(R.string.yes) { _, _ ->
                    navController.popBackStack()
                }
                .setNegativeButton(R.string.no, null)
                .setCancelable(true)
                .show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        val adapterPriority = ArrayAdapter(requireContext(), R.layout.spinner_list_item, priorities)
        (binding.habitPriorityLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterPriority)
        binding.habitPriorityEdit.keyListener = null

        val adapterPeriod = ArrayAdapter(requireContext(), R.layout.spinner_list_item, periods)
        (binding.habitPeriodicityLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriod)
        binding.habitPeriodicityEdit.keyListener = null

        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_habit -> {
            saveHabit()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun saveHabit() {
        if (validateInput()) {
            val newPriority = if (binding.habitPriorityEdit.text.toString().isNotEmpty())
                priorities.indexOf(binding.habitPriorityEdit.text.toString())
             else 0

            val newCount = if (binding.habitCounterEdit.text.toString() == "")
                0
            else
                binding.habitCounterEdit.text.toString().toInt()

            val newFrequency = if (binding.habitPeriodicityEdit.text.toString().isNotEmpty())
                periods.indexOf(binding.habitPeriodicityEdit.text.toString())
             else
                 0

            val newDate = if (habitToCustomize != null)
                habitToCustomize!!.date
             else
                Calendar.getInstance().time.time

            val newHabit = Habit(
                title = binding.habitNameEdit.text.toString(),
                description = binding.habitDescriptionEdit.text.toString(),
                priority = newPriority,
                type = binding.radioGood.isChecked.toInt(),
                count = newCount,
                period = newFrequency,
                color = chosenColor,
                date = newDate,
                doneDates = mutableListOf()
            )

            if (habitToCustomize != null) {
                habitsListViewModel.replaceHabit(habitToCustomize!!, newHabit)
            } else {
                habitsListViewModel.addHabit(newHabit)
            }
            requireView().hideKeyboard()
            habitsCustomizeViewModel.clear()
            navController.popBackStack()
        }
    }

    private fun validateInput(): Boolean {
        var correct = true

        if (binding.habitNameEdit.text?.isEmpty() == true) {
            binding.habitNameLayout.error = resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else
            binding.habitNameLayout.error = ""

        if (binding.habitDescriptionEdit.text?.isEmpty() == true) {
            binding.habitDescriptionLayout.error =
                resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else
            binding.habitDescriptionLayout.error = ""

        if (binding.habitPeriodicityEdit.text?.isEmpty() == true) {
            binding.habitPeriodicityLayout.error =
                resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else
            binding.habitPeriodicityLayout.error = ""

        return correct
    }

    private fun init() {
        habitsCustomizeViewModel.colorPair.observe(viewLifecycleOwner, { colorPair ->
            colorPair?.let {
                chosenColor = it.first
                chosenColorNumber = it.second
                binding.currentColorImageView.setColorFilter(it.first)
            }
        })

        habitToCustomize?.let {
            binding.habitNameEdit.setText(it.title)
            binding.habitDescriptionEdit.setText((it.description))

            if (it.type.toBoolean()) {
                binding.radioGood.isChecked = true
            } else {
                binding.radioBad.isChecked = true
            }

            binding.habitCounterEdit.setText(it.count.toString())
            binding.habitPriorityEdit.setText(priorities[it.priority])
            binding.habitPeriodicityEdit.setText(periods[it.period])

            chosenColor = it.color
            chosenColorNumber = Util.getColorNumberByColor(it.color)
        }

        binding.habitEditorColorPickerButton.setOnClickListener {
            colorPickersOnClick()
        }

        binding.currentColorImageView.setOnClickListener {
            colorPickersOnClick()
        }

        binding.currentColorImageView.setColorFilter(chosenColor)
    }

    private fun colorPickersOnClick() {

        ColorPickerDialogFragment.newInstance()
            .show(parentFragmentManager, ColorPickerDialogFragment.TAG)
        habitsCustomizeViewModel.setColorPair(chosenColor, chosenColorNumber)
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun Int.toBoolean(): Boolean = this == 1

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
