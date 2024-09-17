package com.example.android.firstresponse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SavedTopicsAdapter(
    private val topics: List<SavedTopic>,
    private val onTopicClick: (String) -> Unit
) : RecyclerView.Adapter<SavedTopicsAdapter.TopicViewHolder>() {

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val topicIcon: ImageView = itemView.findViewById(R.id.topicIcon)

        init {
            itemView.setOnClickListener {
                val topicId = topics[adapterPosition].id
                onTopicClick(topicId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.titleTextView.text = topic.title

        // Set the icon based on the topic
        val iconResId = when (topic.id) {
            "burns" -> R.drawable.burns
            "choking" -> R.drawable.choking
            "cpr" -> R.drawable.cpr
            "splints" -> R.drawable.sling
            "seizures" -> R.drawable.seizure
            "shock" -> R.drawable.shock
            "bleeding" -> R.drawable.bleeding
            "snakebite" -> R.drawable.snake_bite
            "bruises" -> R.drawable.bruise
            "sprains" -> R.drawable.sprain
            "strains" -> R.drawable.strains
            "nosebleeds" -> R.drawable.nosebleeds
            "allergic_reaction" -> R.drawable.allergic_reactions
            "headaches" -> R.drawable.headaches
            "minor_concussions" -> R.drawable.minor_concussions
            "muscle_cramps" -> R.drawable.cramp
            "blisters" -> R.drawable.blisters
            "anxiety_management" -> R.drawable.anxiety_management
            "panic_attack_response" -> R.drawable.panic_attack_response
            "trauma_informed_care" -> R.drawable.trauma_informed_care
            "grounding_techniques" -> R.drawable.grounding_techniques
            "stress_reduction" -> R.drawable.stress_reduction
            "floods" -> R.drawable.floods
            "acute_grief" -> R.drawable.acute_grief
            "volcanic_eruption" -> R.drawable.volcanic
            "epidemic" -> R.drawable.epidemic
            "earthquake" -> R.drawable.earthquake
            // New additions
            "water_safety" -> R.drawable.watersafety
            "road_safety" -> R.drawable.roadsafety
            "daily_food_safety" -> R.drawable.dailyfood
            "emergency_food_safety" -> R.drawable.emergency
            "heatwave" -> R.drawable.heatwave
            else -> R.drawable.ic_default // Fallback icon
        }

        holder.topicIcon.setImageResource(iconResId)
    }

    override fun getItemCount(): Int = topics.size
}
