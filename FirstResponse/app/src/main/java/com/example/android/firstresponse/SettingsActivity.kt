package com.example.android.firstresponse

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load the saved language and set it
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("selected_language", "en") // Default to English

        val languageGroup = findViewById<RadioGroup>(R.id.languageGroup)
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
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Refresh the UI by updating visibility or content if needed
        updateUI()
    }


    private fun updateUI() {
        // Manually update UI elements if needed
        // For example, if you have TextViews or other components, you can update their text here
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
