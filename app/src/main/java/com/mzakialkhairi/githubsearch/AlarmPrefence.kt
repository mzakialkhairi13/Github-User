package com.mzakialkhairi.githubsearch

import android.content.Context
import com.mzakialkhairi.githubsearch.model.AlarmReminder

internal class AlarmPrefence(context: Context) {

    companion object{
        private const val PREFS_NAME = "alarm_pref"
        private const val STATUS = "isTrue"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setAlarm(value: AlarmReminder) {
        val editor = preferences.edit()
        editor.putBoolean(STATUS,value.isTrue)
        editor.apply()
    }

    fun getAlarm(): AlarmReminder {
        val model = AlarmReminder()
        model.isTrue= preferences.getBoolean(STATUS,false)
        return model
    }
}
