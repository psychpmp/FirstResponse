package com.example.android.firstresponse

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PanicAttackResponse : BaseActivity() {

    private lateinit var webView1: WebView
    private lateinit var fabSave: FloatingActionButton
    private val topicId = "panic attack response" // Unique ID for the topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panic_attack_response)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))

        // Change title of action bar
        supportActionBar?.title = "PANIC ATTACK RESPONSE"

        // Show back button on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val groundingbutton1=findViewById<Button>(R.id.groundingbutton1)
        groundingbutton1.setOnClickListener{
            val Intent= Intent(this,GroundingTechniques::class.java)
            startActivity(Intent)
        }

        // Setup WebView
        webView1 = findViewById(R.id.webView1)
        webView1.settings.javaScriptEnabled = true
        webView1.webViewClient = WebViewClient()
        val videoId = "t7VjFfsZKE4"
        val videoUrl = "https://www.youtube.com/embed/$videoId"
        webView1.loadData(
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl\" frameborder=\"0\" allowfullscreen></iframe>",
            "text/html",
            "utf-8"
        )

        // Initialize FloatingActionButton for saving the topic
        fabSave = findViewById(R.id.fab_save)
        updateFabIcon()

        fabSave.setOnClickListener {
            toggleSaveTopic()
        }
    }

    // Function to toggle save status
    private fun toggleSaveTopic() {
        val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
        val savedTopicsSet = sharedPref.getStringSet("savedTopics", mutableSetOf()) ?: mutableSetOf()
        val isSaved = savedTopicsSet.contains(topicId)

        if (isSaved) {
            savedTopicsSet.remove(topicId)
            Toast.makeText(this, "Topic removed", Toast.LENGTH_SHORT).show()
        } else {
            savedTopicsSet.add(topicId)
            Toast.makeText(this, "Topic saved", Toast.LENGTH_SHORT).show()
        }

        with(sharedPref.edit()) {
            putStringSet("savedTopics", savedTopicsSet)
            apply()
        }
        updateFabIcon()
    }

    // Function to update FloatingActionButton icon based on save state
    private fun updateFabIcon() {
        val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
        val savedTopicsSet = sharedPref.getStringSet("savedTopics", mutableSetOf()) ?: mutableSetOf()
        val isSaved = savedTopicsSet.contains(topicId)
        val iconResId = if (isSaved) R.drawable.saved_red else R.drawable.saved
        fabSave.setImageDrawable(ContextCompat.getDrawable(this, iconResId))
    }

    // Function to handle the back button in the action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (webView1.canGoBack()) {
            webView1.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
