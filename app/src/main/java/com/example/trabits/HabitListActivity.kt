package com.example.trabits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trabits.databinding.HabitListActivityBinding

class HabitListActivity : AppCompatActivity() {
    private lateinit var binding: HabitListActivityBinding

    private val adapter: HabitAdapter by lazy {
        val data = ArrayList<Habit>()
        HabitAdapter(data, this) { habit, position ->

            val intent = Intent(this, HabitCustomizeActivity::class.java).run {
                putExtra(INTENT_POSITION_KEY, position)
                putExtra(INTENT_OBJECT_KEY, habit)
            }
            startActivityForResult(intent, INTENT_EDIT_KEY)
        }
    }

    private var habits = arrayListOf<Habit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.habitsListToolbar)

        binding.habitsListRecycler.adapter = adapter

        binding.toEditHabitButton.setOnClickListener {
            Intent(this, HabitCustomizeActivity::class.java).run {
                startActivityForResult(this, INTENT_ADD_KEY)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(CONFIG_CHANGE_HABITS_CODE, adapter.habits)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        habits = savedInstanceState.getSerializable(CONFIG_CHANGE_HABITS_CODE) as ArrayList<Habit>
        if (habits.size != 0) {
            adapter.addListOfHabits(habits)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == INTENT_ADD_KEY && resultCode == Activity.RESULT_OK) {
            adapter.addItem(data?.getParcelableExtra("new")!!)
        } else if (requestCode == INTENT_EDIT_KEY) {
            adapter.changeItem(
                data?.getParcelableExtra(HabitCustomizeActivity.NEW_HABIT)!!,
                data.getIntExtra(HabitCustomizeActivity.POSITION, 0)
            )
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val INTENT_ADD_KEY = 1
        const val INTENT_EDIT_KEY = 2
        const val INTENT_POSITION_KEY = "position"
        const val INTENT_OBJECT_KEY = "object"
        const val CONFIG_CHANGE_HABITS_CODE = "list of habits"
    }
}
