package com.mzakialkhairi.githubsearch.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mzakialkhairi.githubsearch.AlarmPrefence
import com.mzakialkhairi.githubsearch.AlarmReceiver
import com.mzakialkhairi.githubsearch.Constant
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.databinding.ActivitySettingBinding
import com.mzakialkhairi.githubsearch.model.AlarmReminder

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmModel: AlarmReminder
    private lateinit var alarmPrefence: AlarmPrefence
    private lateinit var alarmReceiver: AlarmReceiver


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        supportActionBar?.title = "Setting"

        alarmPrefence = AlarmPrefence(this)

        alarmReceiver = AlarmReceiver()

        cekAlarm()

        binding.switchReminder.setOnCheckedChangeListener { buttonView, isChecked ->
            var statusAlarm = alarmModel.isTrue
            statusAlarm = !statusAlarm
            setAlarm(statusAlarm)
        }

        binding.settingLocale.setOnClickListener(this)
        binding.settingAboutme.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if (v == binding.settingAboutme) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.username, "mzakialkhairi13")
            startActivity(intent)
        }
        if (v == binding.settingLocale) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
    }

    fun cekAlarm() {
        alarmModel = alarmPrefence.getAlarm()
        alarmView(alarmModel)
    }

    private fun alarmView(alarmReminder: AlarmReminder) {
        binding.switchReminder.isChecked = alarmReminder.isTrue
    }

    private fun setAlarm(status:Boolean){
        if (status){
            val time = Constant.NotificationTime
            val message = "Check your App now , i miss you"

            alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING,
                time, message)
        }else{
            alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
        val alarmPrefence = AlarmPrefence(this)
        alarmModel.isTrue = status
        alarmPrefence.setAlarm(alarmModel)

    }

}
