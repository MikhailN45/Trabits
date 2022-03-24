package com.example.trabits

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item.view.*


class HabitAdapter(
    var habits: ArrayList<Habit>,
    context: Context,
    val adapterOnClickConstraint: (Habit, Int) -> Unit
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    private val priorities = context.resources.getStringArray(R.array.priorities)
    private val frequent = context.resources.getStringArray(R.array.periods)

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
        notifyItemInserted(itemCount - 1)
    }

    fun addListOfHabits(newHabits: ArrayList<Habit>) {
        habits = newHabits
        notifyDataSetChanged()
    }

    fun changeItem(habit: Habit, position: Int) {
        habits[position] = habit
        notifyItemChanged(position)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(habit: Habit, position: Int) {
            containerView.run {

                recycler_item_element.setOnClickListener {
                    adapterOnClickConstraint(habit, position)
                }

                habit_recycler_name.text = habit.title

                habit_recycler_description.text = habit.description

                habit_recycler_priority.text = when (habit.priority) {
                    1 -> "${priorities[1]} ${this.resources.getString(R.string.priority)}"
                    2 -> "${priorities[2]} ${this.resources.getString(R.string.priority)}"
                    else -> priorities[0]
                }

                habit_recycler_type.text = if (habit.type == 1) {
                    "${this.resources.getString(R.string.good)} ${this.resources.getString(R.string.habit)}"
                } else {
                    "${this.resources.getString(R.string.bad)} ${this.resources.getString(R.string.habit)}"
                }

                habit_recycler_periodicity.text = when (habit.frequency) {
                    0 -> "${habit.count} ${resources.getString(R.string.times)} ${frequent[0]}"
                    1 -> "${habit.count} ${resources.getString(R.string.times)} ${frequent[1]}"
                    2 -> "${habit.count} ${resources.getString(R.string.times)} ${frequent[2]}"
                    3 -> "${habit.count} ${resources.getString(R.string.times)} ${frequent[3]}"
                    else -> "${habit.count} ${resources.getString(R.string.times)} ${frequent[4]}"
                }
                recycler_item_element.setBackgroundColor(habit.color)
            }
        }
    }
}
//TODO add binding