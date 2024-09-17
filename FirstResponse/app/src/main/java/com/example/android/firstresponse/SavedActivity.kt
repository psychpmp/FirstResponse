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

class SavedActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noSavedTopicsTextView: TextView
    private lateinit var emptyAnimationView: LottieAnimationView
    private lateinit var savedTopicsAdapter: SavedTopicsAdapter
    private var savedTopicsList: MutableList<SavedTopic> = mutableListOf()

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
            val savedTopicsSet = sharedPref.getStringSet("savedTopics", emptySet())?.toMutableSet() ?: mutableSetOf()

            Log.d("SavedActivity", "Loaded topics from preferences: $savedTopicsSet")

            // Convert saved topic IDs into SavedTopic objects
            savedTopicsList = savedTopicsSet.map { id ->
                SavedTopic(id, id)
            }.toMutableList()

            Log.d("SavedActivity", "Converted to saved topics list: $savedTopicsList")

        } catch (e: Exception) {
            Log.e("SavedActivity", "Error loading saved topics", e)
            Toast.makeText(this, "An error occurred while loading saved topics: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveTopicsToPreferences() {
        try {
            val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
            val editor = sharedPref.edit()

            // Convert the list of SavedTopic IDs into a Set of Strings
            val savedTopicsSet = savedTopicsList.map { it.id }.toMutableSet()

            Log.d("SavedActivity", "Saving topics to preferences: $savedTopicsSet")

            // Save the set in SharedPreferences
            editor.putStringSet("savedTopics", savedTopicsSet)

            // Use commit() to ensure changes are saved synchronously
            if (editor.commit()) {
                Log.d("SavedActivity", "Topics saved successfully")
            } else {
                Log.e("SavedActivity", "Failed to save topics")
            }
        } catch (e: Exception) {
            Log.e("SavedActivity", "Error saving topics", e)
            Toast.makeText(this, "An error occurred while saving topics: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun addTopicToSavedList(topicId: String) {
        try {
            val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
            val savedTopicsSet = sharedPref.getStringSet("savedTopics", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            Log.d("SavedActivity", "Current saved topics before adding: $savedTopicsSet")

            // Add the new topic to the existing set
            if (savedTopicsSet.add(topicId)) {
                // Save the updated set to SharedPreferences
                val editor = sharedPref.edit()
                editor.putStringSet("savedTopics", savedTopicsSet)

                // Use commit() to ensure changes are saved synchronously
                if (editor.commit()) {
                    Log.d("SavedActivity", "Added and saved topic: $topicId")
                } else {
                    Log.e("SavedActivity", "Failed to save after adding topic: $topicId")
                }
            }

            // Update the local list after saving
            savedTopicsList = savedTopicsSet.map { SavedTopic(it, it) }.toMutableList()
        } catch (e: Exception) {
            Log.e("SavedActivity", "Error adding topic", e)
            Toast.makeText(this, "An error occurred while adding the topic: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun removeTopicFromSavedList(topicId: String) {
        try {
            val sharedPref = getSharedPreferences("SavedTopics", MODE_PRIVATE)
            val savedTopicsSet = sharedPref.getStringSet("savedTopics", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

            Log.d("SavedActivity", "Current saved topics before removing: $savedTopicsSet")

            // Remove the topic from the set
            if (savedTopicsSet.remove(topicId)) {
                // Save the updated set back to SharedPreferences
                val editor = sharedPref.edit()
                editor.putStringSet("savedTopics", savedTopicsSet)

                // Use commit() to ensure changes are saved synchronously
                if (editor.commit()) {
                    Log.d("SavedActivity", "Removed and saved topic: $topicId")
                } else {
                    Log.e("SavedActivity", "Failed to save after removing topic: $topicId")
                }
            }

            // Update the local list after removing
            savedTopicsList = savedTopicsSet.map { SavedTopic(it, it) }.toMutableList()
        } catch (e: Exception) {
            Log.e("SavedActivity", "Error removing topic", e)
            Toast.makeText(this, "An error occurred while removing the topic: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToTopic(topicId: String) {
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
            "allergic_reaction" to allergicreaction::class.java,
            "headaches" to headache::class.java,
            "minor_concussions" to minorconcussion::class.java,
            "muscle_cramps" to musclecramps::class.java,
            "blisters" to blister::class.java,
            "anxiety_management" to AnxietyManagement::class.java,
            "panic_attack_response" to PanicAttackResponse::class.java,
            "trauma_informed_care" to TraumaInformedCare::class.java,
            "grounding_techniques" to GroundingTechniques::class.java,
            "stress_reduction" to StressReduction::class.java,
            "floods" to Floods::class.java,
            "acute_grief" to AcuteGrief::class.java,
            "volcanic_eruption" to VolcanicEruption::class.java,
            "epidemic" to Epidemic::class.java,
            "earthquake" to Earthquake::class.java,
            // New additions
            "water_safety" to WaterSafety::class.java,
            "road_safety" to RoadSafety::class.java,
            "daily_food_safety" to DailyFoodSafety::class.java,
            "emergency_food_safety" to EmergencyFoodSafety::class.java,
            "heatwave" to Heatwave::class.java
        )

        val activityClass = activityMap[topicId]

        if (activityClass != null) {
            val intent = Intent(this, activityClass)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Unknown topic: $topicId", Toast.LENGTH_SHORT).show()
        }
    }
}
