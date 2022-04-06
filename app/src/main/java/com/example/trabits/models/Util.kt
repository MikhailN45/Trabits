package com.example.trabits.models


import android.graphics.Color

class Util {

    companion object {

        val intColors = mutableMapOf(
            0 to parse("#FFCBBB"),
            1 to parse("#FFE8BB"),
            2 to parse("#FFFFBB"),
            3 to parse("#E9FFBB"),
            4 to parse("#D1FFBB"),
            5 to parse("#BBFFBC"),
            6 to parse("#BBFFDA"),
            7 to parse("#BBFFF3"),
            8 to parse("#BBF4FF"),
            9 to parse("#BBDAFF"),
            10 to parse("#BBBFFF"),
            11 to parse("#D1BBFF"),
            12 to parse("#EABBFF"),
            13 to parse("#FFB9FF"),
            14 to parse("#FFBBE6"),
            15 to parse("#FFBBCA"),
            16 to parse("#9fa8da")
        )

        private fun parse(stringColor: String): Int = Color.parseColor(stringColor)

        fun getColorNumberByColor(color: Int): Int {
            intColors.keys.forEach {
                if (intColors[it] == color) return it
            }

            return 16
        }
    }
}