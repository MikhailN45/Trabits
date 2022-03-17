package com.example.trabits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.habit_list_activity.*


class HabitListActivity : AppCompatActivity() {

    private val ADD_KEY = 1
    private val EDIT_KEY = 2
    private lateinit var adapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_list_activity)

        val data = ArrayList<Habit>()

        adapter = HabitAdapter(data) { habit, position ->
            val intent = Intent(this, HabitCustomizeActivity::class.java).run {
                putExtra("position", position)
                putExtra("object", habit)
            }
            startActivityForResult(intent, EDIT_KEY)
        }

        habits_recycler.adapter = adapter

        edit_habit_button.setOnClickListener {
            val intent = Intent(this, HabitCustomizeActivity::class.java).run {
                startActivityForResult(this, ADD_KEY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_KEY && resultCode == Activity.RESULT_OK) {
            adapter.addItem(data?.getSerializableExtra("new") as Habit)
            adapter.notifyItemInserted(adapter.itemCount - 1)
        } else if (requestCode == EDIT_KEY) {
            println((data?.getSerializableExtra("new") as Habit).toString())
            adapter.changeItem(
                data.getSerializableExtra("new") as Habit,
                data.getIntExtra("position", 0)
            )
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}