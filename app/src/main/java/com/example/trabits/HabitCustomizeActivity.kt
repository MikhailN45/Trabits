package com.example.trabits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.trabits.databinding.HabitCustomizeActivityBinding

class HabitCustomizeActivity : AppCompatActivity() {
    private lateinit var binding: HabitCustomizeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitCustomizeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        if (extras == null) {

            val priorities = resources.getStringArray(R.array.habits_priority_types)
            val adapterPriority = ArrayAdapter(this, R.layout.list_item, priorities)
            binding.habitPriorityEdit.setAdapter(
                adapterPriority
            )

            val periods = resources.getStringArray(R.array.habits_period_types)
            val adapterPeriod = ArrayAdapter(this, R.layout.list_item, periods)
            binding.habitPeriodicityEdit.setAdapter(
                adapterPeriod
            )

            binding.applyHabitParamsButton.setOnClickListener {

                val habit = Habit(
                    binding.habitNameEdit.text.toString(),
                    binding.habitDescriptionEdit.text.toString(),
                    binding.habitPriorityEdit.text.toString(),
                    binding.radioGood.isActivated,
                    binding.habitPeriodicityEdit.text.toString(),
                    binding.habitCounterEdit.text?.toString()
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

            binding.habitPriorityEdit.setText(practice.priority)
            binding.habitPeriodicityEdit.setText(practice.period)

            val priorities = resources.getStringArray(R.array.habits_priority_types)

            val adapterPriority = ArrayAdapter(this, R.layout.list_item, priorities)
            binding.habitPriorityEdit.setAdapter(
                adapterPriority
            )

            val periods = resources.getStringArray(R.array.habits_period_types)

            val adapterPeriod = ArrayAdapter(this, R.layout.list_item, periods)
            binding.habitPeriodicityEdit.setAdapter(
                adapterPeriod
            )

            binding.habitNameEdit.setText(practice.name)
            binding.habitDescriptionEdit.setText((practice.description))

            if (practice.type) {
                binding.radioGood.isChecked = true
            } else {
                binding.radioBad.isChecked = true
            }
            binding.habitCounterEdit.setText(practice.times?.toString())

            binding.applyHabitParamsButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_save_alt_24))

            binding.applyHabitParamsButton.setOnClickListener {

                val habit = Habit(
                    binding.habitNameEdit.toString(),
                    binding.habitDescriptionEdit.text.toString(),
                    binding.habitPriorityEdit.text.toString(),
                    binding.radioGood.isChecked,
                    binding.habitPeriodicityEdit.text.toString(),
                    binding.habitCounterEdit.text?.toString()
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
