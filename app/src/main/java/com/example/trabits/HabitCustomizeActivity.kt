package com.example.trabits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import kotlinx.android.synthetic.main.habit_customize_activity.*

class HabitCustomizeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_customize_activity)

        val extras = intent.extras

        if (extras == null) {

            val priorities = listOf("High", "Medium", "Low")
            val adapterPriority = ArrayAdapter(this, R.layout.list_item, priorities)
            (habit_priority_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterPriority)

            val periods =
                listOf("Daily", "Weekly", "Monthly")
            val adapterPeriod = ArrayAdapter(this, R.layout.list_item, periods)
            (habit_periodicity_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriod)

            apply_habit_params_button.setOnClickListener {


                val habit = Habit(
                    habit_name_edit.text.toString(),
                    habit_description_edit.text.toString(),
                    habit_priority_edit.text.toString(),
                    radio_good.isActivated,
                    habit_periodicity_edit.text.toString(),
                    habit_counter_edit.text?.toString()?.toInt()
                )

                val intent = Intent(this, HabitListActivity::class.java).run {
                    putExtra("new", habit)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        } else {
            val practice = intent.getSerializableExtra("object") as Habit
            val position = intent.getIntExtra("position", 0)

            habit_priority_edit.setText(practice.priority)
            habit_periodicity_edit.setText(practice.period)

            val priorities = listOf("High", "Medium", "Low")
            val adapterPriority = ArrayAdapter(this, R.layout.list_item, priorities)
            (habit_priority_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterPriority)

            val periods =
                listOf("Daily", "Weekly", "Monthly")
            val adapterPeriod = ArrayAdapter(this, R.layout.list_item, periods)
            (habit_periodicity_layout.editText as? AutoCompleteTextView)?.setAdapter(adapterPeriod)

            habit_name_edit.setText(practice.name)
            habit_description_edit.setText((practice.description))

            if (practice.type) {
                radio_good.isChecked = true
            } else {
                radio_bad.isChecked = true
            }
            habit_counter_edit.setText(practice.times?.toString())

            apply_habit_params_button.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_save_alt_24))

            apply_habit_params_button.setOnClickListener {

                val habit = Habit(
                    habit_name_edit.text.toString(),
                    habit_description_edit.text.toString(),
                    habit_priority_edit.text.toString(),
                    radio_good.isChecked,
                    habit_periodicity_edit.text.toString(),
                    habit_counter_edit.text?.toString()?.toInt()
                )

                val intent = Intent(this, HabitListActivity::class.java).run {
                    putExtra("new", habit)
                    putExtra("position", position)
                }

                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

    }
}

enum class HabitType(val resId: Int) {
    HIGH(R.string.high),
    MEDIUM(R.string.medium),
    LOW(R.string.low),
    DAY(R.string.day),
    WEEK(R.string.week),
    MONTH(R.string.month)
}