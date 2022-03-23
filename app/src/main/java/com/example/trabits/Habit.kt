package com.example.trabits

import java.io.Serializable

data class Habit(
    var name: String,
    var description: String,
    var priority: String,
    var type: Boolean,
    var period: String,
    var times: String?
) : Serializable