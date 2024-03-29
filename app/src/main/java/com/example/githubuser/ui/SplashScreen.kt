package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySplashScreenBinding
import com.example.githubuser.settings.SettingPreferences
import com.example.githubuser.settings.SettingViewModel
import com.example.githubuser.settings.dataStore

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIMEOUT: Long = 2000

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = ProgressBar.VISIBLE
        binding.splashText.visibility = TextView.VISIBLE

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)

            finish()
        }, SPLASH_TIMEOUT)

        val modePreferences = SettingPreferences.getInstance(dataStore)
        val modeViewModel = ViewModelProvider(
            this,
            SettingViewModel.ViewModelFactory(modePreferences)
        )[SettingViewModel::class.java]

        modeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.splashImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.github_white))
                binding.splashText.setTextColor(resources.getColor(R.color.white))
                binding.root.setBackgroundColor(resources.getColor(R.color.black))

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.splashImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.github))
                binding.splashText.setTextColor(resources.getColor(R.color.black))
                binding.root.setBackgroundColor(resources.getColor(R.color.white))
            }
        }
    }
}