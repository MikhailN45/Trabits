package com.example.trabits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import com.example.trabits.databinding.HabitCustomizeActivityBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.habit_customize_activity.*

class HabitCustomizeActivity : AppCompatActivity(), ColorPickerDialogFragment.IChangeColor {

    private lateinit var binding: HabitCustomizeActivityBinding
    private var chosenColor: Int = Util.intColors[16]!!
    private var chosenColorNumber = ColorPickerDialogFragment.DEFAULT_COLOR

    private val priorities: Array<String> by lazy {
        applicationContext.resources.getStringArray(R.array.priorities)
    }
    private val periods: Array<String> by lazy {
        applicationContext.resources.getStringArray(R.array.periods)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitCustomizeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.habitCustomizeToolbar)

        if (intent.extras == null) {
            initAdding()
        } else {
            initEditing()
        }

        val button: Button = findViewById(R.id.habit_save_changes_button)
        button.setOnClickListener {
            if (intent.extras == null) {
                addHabit()
            } else {
                editHabit()
            }
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.discard_changes)
            .setMessage(R.string.unsaved_changes)
            .setPositiveButton(R.string.yes) { _, _ ->
                intent.extras?.let {
                    val intent = Intent(this, HabitListActivity::class.java).run {
                        putExtra(
                            NEW_HABIT,
                            intent.getParcelableExtra<Habit>(HabitListActivity.INTENT_OBJECT_KEY)!!
                        )
                        putExtra(
                            POSITION,
                            intent.getIntExtra(HabitListActivity.INTENT_POSITION_KEY, 0)
                        )
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                super.onBackPressed()
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .setCancelable(true)
            .show()
    }

    override fun onSaveInstanceState(outState: Bundle) {

        outState.putInt(CURRENT_COLOR, chosenColor)
        outState.putInt(CURRENT_COLOR_NUMBER, chosenColorNumber)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        chosenColor = savedInstanceState.getInt(CURRENT_COLOR)
        chosenColorNumber = savedInstanceState.getInt(CURRENT_COLOR_NUMBER)
        binding.currentColorImageView.setColorFilter(chosenColor)

        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun addHabit() {
        if (validateInput()) {

            val newPriority = if (binding.habitPriorityEdit.text.toString().isNotEmpty()) {
                priorities.indexOf(binding.habitPriorityEdit.text.toString())
            } else {
                0
            }
            val newCount = if (binding.habitCounterEdit.text.toString() == "") {
                0
            } else {
                binding.habitCounterEdit.text.toString().toInt()
            }
            val newFrequency = if (binding.habitPeriodicityEdit.text.toString().isNotEmpty()) {
                periods.indexOf(binding.habitPeriodicityEdit.text.toString())
            } else {
                2
            }

            val newHabit = Habit(
                title = binding.habitNameEdit.text.toString(),
                description = binding.habitDescriptionEdit.text.toString(),
                priority = newPriority,
                type = binding.radioGood.isChecked.toInt(),
                count = newCount,
                frequency = newFrequency,
                color = chosenColor
            )

            val intent = Intent(this, HabitListActivity::class.java).run {
                putExtra(NEW_HABIT, newHabit)
            }

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun editHabit() {
        if (validateInput()) {
            val position = intent.getIntExtra(HabitListActivity.INTENT_POSITION_KEY, 0)

            val newPriority = if (binding.habitPriorityEdit.text.toString().isNotEmpty()) {
                priorities.indexOf(binding.habitPriorityEdit.text.toString())
            } else {
                0
            }
            val newCount = if (binding.habitCounterEdit.text.toString() == "") {
                0
            } else {
                binding.habitCounterEdit.text.toString().toInt()
            }
            val newFrequency = if (binding.habitPeriodicityEdit.text.toString().isNotEmpty()) {
                periods.indexOf(binding.habitPeriodicityEdit.text.toString())
            } else {
                2
            }

            val newHabit = Habit(
                title = binding.habitNameEdit.text.toString(),
                description = binding.habitDescriptionEdit.text.toString(),
                priority = newPriority,
                type = binding.radioGood.isChecked.toInt(),
                count = newCount,
                frequency = newFrequency,
                color = chosenColor
            )

            val intent = Intent(this, HabitListActivity::class.java).run {
                putExtra(NEW_HABIT, newHabit)
                putExtra(POSITION, position)
            }

            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun init() {

        val adapterPriority = ArrayAdapter(this, R.layout.spinner_list_item, priorities)
        (binding.habitPriorityLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterPriority)
        binding.habitPriorityEdit.keyListener = null

        val adapterPeriod = ArrayAdapter(this, R.layout.spinner_list_item, periods)
        (binding.habitPeriodicityLayout.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriod)
        binding.habitPeriodicityEdit.keyListener = null

        binding.habitEditorColorPickerButton.setOnClickListener {
            colorPickersOnClick()
        }

        binding.currentColorImageView.setOnClickListener {
            colorPickersOnClick()
        }

        binding.currentColorImageView.setColorFilter(chosenColor)
    }

    private fun colorPickersOnClick() {
        println(chosenColor)
        println(Util.getColorNumberByColor(chosenColor))

        ColorPickerDialogFragment.newInstance(Util.getColorNumberByColor(chosenColor))
            .show(supportFragmentManager, ColorPickerDialogFragment.TAG)
    }

    private fun initAdding() {
        init()
        binding.habitCustomizeToolbar.setTitle(R.string.label_add)
    }

    private fun initEditing() {
        init()

        binding.habitCustomizeToolbar.setTitle(R.string.label_edit)

        val habitToEdit =
            intent.getParcelableExtra<Habit>(HabitListActivity.INTENT_OBJECT_KEY) as Habit

        binding.habitNameEdit.setText(habitToEdit.title)
        binding.habitDescriptionEdit.setText((habitToEdit.description))

        if (habitToEdit.type.toBoolean()) {
            binding.radioGood.isChecked = true
        } else {
            binding.radioBad.isChecked = true
        }

        binding.habitCounterEdit.setText(habitToEdit.count.toString())
        binding.habitPriorityEdit.setText(priorities[habitToEdit.priority])
        binding.habitPeriodicityEdit.setText(periods[habitToEdit.priority])

        chosenColor = habitToEdit.color

        binding.currentColorImageView.setColorFilter(habitToEdit.color)
    }

    private fun validateInput(): Boolean {
        var correct = true
        if (binding.habitNameEdit.text?.isEmpty() == true) {
            binding.habitNameLayout.error = resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else {
            binding.habitNameLayout.error = ""
        }
        if (binding.habitDescriptionEdit.text?.isEmpty() == true) {
            binding.habitDescriptionLayout.error =
                resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else {
            binding.habitDescriptionLayout.error = ""
        }
        if (binding.habitPeriodicityEdit.text?.isEmpty() == true) {
            binding.habitPeriodicityLayout.error =
                resources.getString(R.string.field_must_not_be_empty)
            correct = false
        } else {
            binding.habitPeriodicityLayout.error = ""
        }
        return correct
    }

    override fun onChangeColor(newColor: Int, newColorNumber: Int) {
        chosenColor = newColor
        chosenColorNumber = newColorNumber
        binding.currentColorImageView.setColorFilter(newColor)
    }

    private fun Boolean.toInt() = if (this) 1 else 0
    private fun Int.toBoolean(): Boolean = this == 1

    companion object {
        const val NEW_HABIT = "new"
        const val POSITION = "position"
        const val CURRENT_COLOR = "current color"
        const val CURRENT_COLOR_NUMBER = "current color number"
    }
}