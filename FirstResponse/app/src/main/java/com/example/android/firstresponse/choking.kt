package com.example.android.firstresponse

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.widget.Toast
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton

class choking : BaseActivity() {

    private lateinit var webView1: WebView
    private lateinit var webView2: WebView
    private lateinit var fabSave: FloatingActionButton
    private val topicId = "choking" // Unique ID for the topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choking)

        // Set up Action Bar
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@choking, R.color.shadow2)))
            title = "CHOKING"
            setDisplayHomeAsUpEnabled(true)
        }

        // Find the button by its ID
        val callButton1 = findViewById<Button>(R.id.callbutton1)
        callButton1.setOnClickListener {
            val intent = Intent(this, HelplineActivity::class.java)
            startActivity(intent)
        }

// If you have another button with a different ID (callbutton2), add the corresponding code below
        val callButton2 = findViewById<Button>(R.id.callbutton2)
        callButton2.setOnClickListener {
            val intent = Intent(this, HelplineActivity::class.java)
            startActivity(intent)
        }


        // Initialize WebViews
        webView1 = findViewById(R.id.webView)
        webView1.settings.javaScriptEnabled = true
        webView1.webViewClient = WebViewClient()

        val videoId1 = "6E9AXXRdkfE"
        val videoUrl1 = "https://www.youtube.com/embed/$videoId1"
        webView1.loadData(
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl1\" frameborder=\"0\" allowfullscreen></iframe>",
            "text/html",
            "utf-8"
        )

        webView2 = findViewById(R.id.webView2)
        webView2.settings.javaScriptEnabled = true
        webView2.webViewClient = WebViewClient()

        val videoId2 = "ePodw7L_mFM"
        val videoUrl2 = "https://www.youtube.com/embed/$videoId2"
        webView2.loadData(
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl2\" frameborder=\"0\" allowfullscreen></iframe>",
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
        } else if (webView2.canGoBack()) {
            webView2.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
