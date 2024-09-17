package com.example.android.firstresponse

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale() // Apply locale before setting content view
        setContentView(R.layout.activity_settings)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val languageGroup = findViewById<RadioGroup>(R.id.languageGroup)

        // Load the saved language and set the radio button
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"

        when (savedLanguage) {
            "en" -> languageGroup.check(R.id.radioEnglish)
            "fil" -> languageGroup.check(R.id.radioFilipino)
            "bcl" -> languageGroup.check(R.id.radioBicol)
        }

        languageGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioEnglish -> setLocale("en")
                R.id.radioFilipino -> setLocale("fil")
                R.id.radioBicol -> setLocale("bcl")
            }
        }
    }

    private fun setLocale(languageCode: String) {
        // Save the selected language to SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_language", languageCode)
        editor.apply()

        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Animate the fade-out and reload the activity
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val rootView = findViewById<View>(android.R.id.content)
        rootView.startAnimation(fadeOut)

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                recreate() // Recreate activity to apply changes
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun applyLocale() {
        // Retrieve the saved language from SharedPreferences and set it
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") ?: "en"

        val locale = Locale(savedLanguage)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
