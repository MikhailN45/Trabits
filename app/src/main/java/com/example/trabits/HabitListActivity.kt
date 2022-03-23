package com.example.trabits

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trabits.databinding.HabitListActivityBinding

class HabitListActivity : AppCompatActivity() {
    private lateinit var binding: HabitListActivityBinding
    private lateinit var adapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = ArrayList<Habit>()

        adapter = HabitAdapter(data) { habit, position ->
            val intent = Intent(this, HabitCustomizeActivity::class.java).run {
                putExtra("position", position)
                putExtra("object", habit)
            }
            startActivityForResult(intent, Constants.EDIT_KEY)
        }

        binding.habitsRecycler.adapter = adapter

        binding.editHabitButton.setOnClickListener {
            Intent(this, HabitCustomizeActivity::class.java).run {
                startActivityForResult(this, Constants.ADD_KEY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.ADD_KEY && resultCode == Activity.RESULT_OK) {
            adapter.addItem(data?.getSerializableExtra("new") as Habit)
            adapter.notifyItemInserted(adapter.itemCount - 1)
        } else if (requestCode == Constants.EDIT_KEY) {
            println((data?.getSerializableExtra("new") as Habit).toString())
            adapter.changeItem(
                data.getSerializableExtra("new") as Habit,
                data.getIntExtra("position", 0)
            )
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    class Constants {
        companion object {
            const val ADD_KEY = 1
            const val EDIT_KEY = 2
        }
    }
}