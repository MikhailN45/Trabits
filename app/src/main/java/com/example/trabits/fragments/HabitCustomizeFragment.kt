package com.example.trabits.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.trabits.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.habit_customize_fragment.*


class HabitCustomizeFragment : Fragment(R.layout.habit_customize_fragment) {

    private var chosenColor: Int = Util.intColors[16]!!
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.discard_changes)
                .setMessage(R.string.unsaved_changes)
                .setPositiveButton(R.string.yes) { _, _ ->
                    navController.popBackStack()
                }
                .setNegativeButton(R.string.no) { _, _ -> }
                .setCancelable(true)
                .show()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
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

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt(COLOR_CONFIG_CHANGE_CODE, chosenColor)
        outState.putInt(COLOR_NUM_CONFIG_CHANGE_CODE, chosenColorNumber)

        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {

        savedInstanceState?.let { it ->
            chosenColor = it.getInt(COLOR_CONFIG_CHANGE_CODE)
            chosenColorNumber =
                it.getInt(COLOR_NUM_CONFIG_CHANGE_CODE)
            current_color_image_view.setColorFilter(chosenColor)
        }

        super.onViewStateRestored(savedInstanceState)
    }

    private fun saveHabit() {
        if (validateInput()) {

            val newPriority = if (habit_priority_edit.text.toString().isNotEmpty()) {
                priorities.indexOf(habit_priority_edit.text.toString())
            } else 0

            val newCount = if (habit_counter_edit.text.toString() == "")
                0
            else
                habit_counter_edit.text.toString().toInt()

            val newFrequency = if (habit_periodicity_edit.text.toString().isNotEmpty()) {
                periods.indexOf(habit_periodicity_edit.text.toString())
            } else 0

            val newHabit = Habit(
                title = habit_name_edit.text.toString(),
                description = habit_description_edit.text.toString(),
                priority = newPriority,
                type = radio_good.isChecked.toInt(),
                count = newCount,
                frequency = newFrequency,
                color = chosenColor
            )

            if (habitToCustomize != null) {
                FakeDatabase.replaceHabit(habitToCustomize!!, newHabit)
            } else {
                FakeDatabase.addHabit(newHabit)
            }
            requireView().hideKeyboard()
            navController.popBackStack()

        }
    }

    private fun validateInput(): Boolean {
        var correct = true

        if (habit_name_edit.text?.isEmpty() == true) {
            habit_name_layout.error = resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else
            habit_name_layout.error = ""

        if (habit_description_edit.text?.isEmpty() == true) {
            habit_description_layout.error =
                resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else
            habit_description_layout.error = ""

        if (habit_periodicity_edit.text?.isEmpty() == true) {
            habit_periodicity_layout.error =
                resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else
            habit_periodicity_layout.error = ""

        return correct
    }


    private fun init() {

        habitToCustomize?.let {
            habit_name_edit.setText(it.title)
            habit_description_edit.setText((it.description))

            if (it.type.toBoolean()) {
                radio_good.isChecked = true
            } else {
                radio_bad.isChecked = true
            }

            habit_counter_edit.setText(it.count.toString())

            habit_priority_edit.setText(priorities[it.priority])
            habit_periodicity_edit.setText(periods[it.frequency])

            chosenColor = it.color

            current_color_image_view.setColorFilter(it.color)
        }

        val adapterPriority = ArrayAdapter(requireContext(), R.layout.spinner_list_item, priorities)
        (habit_priority_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterPriority)
        habit_priority_edit.keyListener = null

        val adapterPeriod = ArrayAdapter(requireContext(), R.layout.spinner_list_item, periods)
        (habit_periodicity_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriod)
        habit_periodicity_edit.keyListener = null

        habit_editor_color_picker_button.setOnClickListener {
            colorPickersOnClick()
        }

        current_color_image_view.setOnClickListener {
            colorPickersOnClick()
        }

        current_color_image_view.setColorFilter(chosenColor)
    }


    private fun colorPickersOnClick() {

        ColorPickerDialogFragment.newInstance(Util.getColorNumberByColor(chosenColor)) { newColor, newColorNumber ->

            chosenColor = newColor
            chosenColorNumber = newColorNumber
            current_color_image_view.setColorFilter(newColor)
        }
            .show(parentFragmentManager, ColorPickerDialogFragment.TAG)

    }

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun Int.toBoolean(): Boolean = this == 1


    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    companion object {
        const val COLOR_CONFIG_CHANGE_CODE = "current color"
        const val COLOR_NUM_CONFIG_CHANGE_CODE = "current color number"
    }
}
