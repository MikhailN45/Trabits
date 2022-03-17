package com.example.trabits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item.view.*


class HabitAdapter(
    private var habits: ArrayList<Habit>,
    val adapterOnClickConstraint: (Habit, Int) -> Unit
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder((inflater.inflate(R.layout.recycler_item, parent, false)))
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(habits[position], position)
    }

    fun addItem(habit: Habit) {
        habits.add(habit)
    }

    fun changeItem(habit: Habit, position: Int) {
        habits[position] = habit
        notifyItemChanged(position)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(habit: Habit, position: Int) {
            containerView.run {

                constraintMainRecyclerElement.setOnClickListener {
                    adapterOnClickConstraint(habit, position)
                }

                habitNameRecyclerElement.text = habit.name
                habit_description_field.text = habit.description
                habit_periodicity_field.text = "Repeat ${habit.period}"
                habit_priority_field.text = "${habit.priority} priority"

                habit_type_field.text = if (habit.type) {
                    "Good habit"
                } else {
                    "Bad habit"
                }
                habit_counter_field.text = "${habit.times.toString()} times complete"
            }


        }
    }

}