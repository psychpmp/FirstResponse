package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SavedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noSavedTopicsTextView: TextView
    private lateinit var emptyAnimationView: LottieAnimationView
    private lateinit var savedTopicsAdapter: SavedTopicsAdapter
    private var savedTopicsList: List<SavedTopic> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_saved)

            // Hide the Action Bar for this activity
            supportActionBar?.hide()

            recyclerView = findViewById(R.id.recyclerView)
            noSavedTopicsTextView = findViewById(R.id.no_saved_topics_text)
            emptyAnimationView = findViewById(R.id.empty_animation)

            recyclerView.layoutManager = LinearLayoutManager(this)

            // Load saved topics
            loadSavedTopics()

            if (savedTopicsList.isEmpty()) {
                // Show "No saved topics" message and Lottie animation
                recyclerView.visibility = View.GONE
                noSavedTopicsTextView.visibility = View.VISIBLE
                emptyAnimationView.visibility = View.VISIBLE
                emptyAnimationView.playAnimation() // Ensure this line is executed
            } else {
                // Show the RecyclerView with saved topics
                savedTopicsAdapter = SavedTopicsAdapter(savedTopicsList) { topicId ->
                    navigateToTopic(topicId)
                }
                recyclerView.adapter = savedTopicsAdapter
                recyclerView.visibility = View.VISIBLE
                noSavedTopicsTextView.visibility = View.GONE
                emptyAnimationView.visibility = View.GONE
            }


            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.bottomHome -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        // Apply transition animations
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        true
                    }
                    R.id.bottomHelpline -> {
                        val intent = Intent(this, HelplineActivity::class.java)
                        startActivity(intent)
                        // Apply transition animations
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        true
                    }
                    R.id.bottomSaved -> {
                        // Already in SavedActivity, no need to navigate
                        true
                    }
                    else -> false
                }
            }
        } catch (e: Exception) {
            Log.e("SavedActivity", "Error during onCreate", e)
            Toast.makeText(this, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadSavedTopics() {
        try {
            val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
            val savedTopicsSet = sharedPref.getStringSet("savedTopics", mutableSetOf()) ?: mutableSetOf()

            // Convert saved topic IDs into SavedTopic objects
            savedTopicsList = savedTopicsSet.map { id ->
                // Create SavedTopic objects using the ID as the title
                SavedTopic(id, id)
            }

        } catch (e: Exception) {
            Log.e("SavedActivity", "Error loading saved topics", e)
            Toast.makeText(this, "An error occurred while loading saved topics: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToTopic(topicId: String) {
        // Define a map of topic IDs to their corresponding activity classes
        val activityMap = mapOf(
            "burns" to burns::class.java,
            "choking" to choking::class.java,
            "cpr" to CPR::class.java,
            "splints" to splints::class.java,
            "seizures" to seizures::class.java,
            "shock" to shock::class.java,
            "bleeding" to bleeding::class.java,
            "snakebite" to snakebite::class.java,
            "bruises" to bruises::class.java,
            "sprains" to sprain::class.java,
            "strains" to strain::class.java,
            "nosebleeds" to nosebleed::class.java,
            "allergic reaction" to allergicreaction::class.java,
            "headaches" to headache::class.java,
            "minor concussions" to minorconcussion::class.java,
            "muscle cramp" to musclecramps::class.java,
            "blister" to blister::class.java,
            "anxiety management" to AnxietyManagement::class.java,
            "panic attack response" to PanicAttackResponse::class.java,
            "trauma-informed care" to TraumaInformedCare::class.java,
            "grounding techniques" to GroundingTechniques::class.java,
            "stress reduction" to StressReduction::class.java,
            "floods" to Floods::class.java,
            "acute grief" to AcuteGrief::class.java,
            "volcanic eruption" to VolcanicEruption::class.java,
            "epidemic" to Epidemic::class.java,
            "earthquake" to Earthquake::class.java,
            // New additions
            "water safety" to WaterSafety::class.java,
            "road safety" to RoadSafety::class.java,
            "daily food safety" to DailyFoodSafety::class.java,
            "emergency food safety" to EmergencyFoodSafety::class.java,
            "heatwave" to Heatwave::class.java
        )

        // Find the activity class for the given topic ID
        val activityClass = activityMap[topicId]

        // If the activity class is found, start the activity
        if (activityClass != null) {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Unknown topic: $topicId", Toast.LENGTH_SHORT).show()
        }
    }
}
