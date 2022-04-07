package com.example.trabits.models

class FakeDataSource {

    companion object {

        val habitsList : MutableList<Habit> = mutableListOf()

        fun addHabit(newHabit: Habit) {
            habitsList.add(newHabit)
        }

        fun replaceHabit(oldHabit: Habit, newHabit: Habit) {
            val index = habitsList.indexOf(oldHabit)
            habitsList.removeAt(index)
            habitsList.add(index, newHabit)
        }
    }
}

