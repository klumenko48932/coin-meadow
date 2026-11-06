package com.Lumpexo4.Gotpuc.data.local

import java.util.Calendar

class DayProvider {

    fun dayOfYear(): Int = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    fun dayOfWeekIndex(): Int = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7
}
