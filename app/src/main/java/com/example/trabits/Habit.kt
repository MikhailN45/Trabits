package com.example.trabits

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Habit(
    var title: String,
    var description: String,
    var priority: Int,
    var type: Int,
    var count: Int,
    var frequency: Int,
    var color: Int
) : Parcelable