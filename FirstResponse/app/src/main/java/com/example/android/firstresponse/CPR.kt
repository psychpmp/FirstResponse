package com.example.android.firstresponse

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CPR : AppCompatActivity() {

    private lateinit var webView1: WebView
    private lateinit var webView2: WebView
    private lateinit var fabSave: FloatingActionButton
    private val topicId = "cpr" // Unique ID for the topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpr)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))

        // Change the title of the action bar
        supportActionBar?.title = "CPR"

        // Show the back button on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val callButton1=findViewById<Button>(R.id.callbutton1)
        callButton1.setOnClickListener{
            val Intent= Intent(this,HelplineActivity::class.java)
            startActivity(Intent)
        }

        // Initialize WebViews
        webView1 = findViewById(R.id.webView)
        webView1.settings.javaScriptEnabled = true
        webView1.webViewClient = WebViewClient()
        val videoId1 = "DUaxt8OlT3o"
        val videoUrl1 = "https://www.youtube.com/embed/$videoId1"
        webView1.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl1\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8")

        webView2 = findViewById(R.id.webView2)
        webView2.settings.javaScriptEnabled = true
        webView2.webViewClient = WebViewClient()
        val videoId2 = "6eRwgM2Pa4o"
        val videoUrl2 = "https://www.youtube.com/embed/$videoId2"
        webView2.loadData("<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl2\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8")

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
        } else if (webView2.canGoBack()) {
            webView2.goBack()
        } else {
            super.onBackPressed()
        }
    }
}