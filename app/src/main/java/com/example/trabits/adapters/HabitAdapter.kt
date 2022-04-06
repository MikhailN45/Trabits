package com.example.trabits.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trabits.R
import com.example.trabits.fragments.HabitListFragmentDirections
import com.example.trabits.models.Habit
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.recycler_item.view.*

class HabitAdapter(
    private var habits: MutableList<Habit>,
    context: Context
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    private val priorities = context.resources.getStringArray(R.array.priorities)
    private val frequent = context.resources.getStringArray(R.array.periods)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder((inflater.inflate(R.layout.recycler_item, parent, false)))
    }

    override fun getItemCount(): Int = habits.size

    fun setData(newHabits: MutableList<Habit>) {
        val diffUtil = DiffUtilHabitsRecycler(habits, newHabits)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        habits = newHabits
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(habit: Habit) {

            containerView.run {

                recycler_item_element.setOnClickListener {
                    val action =
                        HabitListFragmentDirections.actionHabitListFragmentToHabitCustomizeFragment(
                            context.resources.getString(R.string.label_edit)
                        )
                    action.habitToEdit = habit

                    Navigation.findNavController(it).navigate(action)
                }

                habit_recycler_name.text = habit.title

                habit_recycler_description.text = habit.description
//TODO(вынести логику в утилс и внедрить строки с параметрами)
                habit_recycler_priority.text = when (habit.priority) {
                    1 -> "${priorities[1]} ${this.resources.getString(R.string.priority)}"
                    2 -> "${priorities[2]} ${this.resources.getString(R.string.priority)}"
                    else -> priorities[0]
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