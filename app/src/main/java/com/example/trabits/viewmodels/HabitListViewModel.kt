package com.example.trabits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trabits.data.FakeDataSource
import com.example.trabits.models.FilterTypes
import com.example.trabits.models.Habit

class HabitListViewModel : ViewModel() {

    private val currentHabitsLiveData: MutableLiveData<MutableList<Habit>> = MutableLiveData()
    val currentHabits: LiveData<MutableList<Habit>> get() = currentHabitsLiveData

    init {
        currentHabitsLiveData.value = FakeDataSource.habitsList
    }

    fun addHabit(newHabit: Habit) {
        cleanHabitsFilter()
        FakeDataSource.addHabit(newHabit)
    }

    fun replaceHabit(oldHabit: Habit, newHabit: Habit) {
        FakeDataSource.replaceHabit(oldHabit, newHabit)
    }

    fun sortHabits(filterType: FilterTypes, byDescending: Boolean) {

        when (filterType) {

            FilterTypes.ByPriority -> {
                if (byDescending) {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedByDescending { it.priority }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                } else {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedBy { it.priority }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                }
            }

            FilterTypes.ByPeriod -> {
                if (byDescending) {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedByDescending { it.frequency }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                } else {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedBy { it.frequency }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                }
            }

            FilterTypes.ByCount -> {
                if (byDescending) {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedByDescending { it.count }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                } else {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedBy { it.count }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                }
            }

            FilterTypes.ByDate -> {
                if (byDescending) {
                    val sortedHabits = currentHabitsLiveData.value?.sortedByDescending { it.date }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                } else {
                    val sortedHabits =
                        currentHabitsLiveData.value?.sortedBy { it.date }
                    currentHabitsLiveData.value = sortedHabits?.toMutableList()
                }
            }

            FilterTypes.NoFilter -> {
                cleanHabitsFilter()
            }
        }
    }

    fun sortHabits(text: String) { //Поиск по привычкам
        if (text.isNotEmpty()) {
            val sortedHabits = FakeDataSource.habitsList.filter {
                it.title.contains(text, ignoreCase = true)
            }
            currentHabitsLiveData.value = sortedHabits.toMutableList()
        } else {
            cleanHabitsFilter()
        }
    }

    fun cleanHabitsFilter() {
        currentHabitsLiveData.value = FakeDataSource.habitsList
    }
}
