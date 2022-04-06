package com.example.trabits.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habit(
    var title: String,
    var description: String,
    var priority: Int,
    var type: Int,
    var count: Int,
    var period: Int,
    var color: Int,
    var date: Long,
    var doneDates: MutableList<Long>
) : Parcelable