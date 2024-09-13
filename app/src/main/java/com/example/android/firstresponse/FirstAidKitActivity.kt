package com.example.android.firstresponse

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.MenuItem

class FirstAidKitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid_kit)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))
        supportActionBar?.title = "FIRST AID KITS"

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = FirstAidKitAdapter(getFirstAidKits())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press to go back to the parent activity
                finish() // or onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFirstAidKits(): List<FirstAidKit> {
        return listOf(
            FirstAidKit(R.drawable.bandage, "Bandage", R.string.bandage_description, "Shop Now"),
            FirstAidKit(R.drawable.antiseptic, "Antiseptic", R.string.antiseptic_description, "Shop Now"),
            FirstAidKit(R.drawable.gauze, "Gauze", R.string.gauze_description, "Shop Now"),
            FirstAidKit(R.drawable.adhesive_tape, "Adhesive Tape", R.string.adhesive_tape_description, "Shop Now"),
            FirstAidKit(R.drawable.scissors, "Scissors", R.string.scissors_description, "Shop Now"),
            FirstAidKit(R.drawable.pain_reliever, "Pain Reliever", R.string.pain_reliever_description, "Shop Now"),
            FirstAidKit(R.drawable.thermometer, "Thermometer", R.string.thermometer_description, "Shop Now"),
            FirstAidKit(R.drawable.alcohol_wipes, "Alcohol Wipes", R.string.alcohol_wipes_description, "Shop Now"),
            FirstAidKit(R.drawable.gloves, "Gloves", R.string.gloves_description, "Shop Now"),
            FirstAidKit(R.drawable.first_aid_manual, "First Aid Manual", R.string.first_aid_manual_description, "Shop Now"),
            FirstAidKit(R.drawable.cold_compress, "Cold Compress", R.string.cold_compress_description, "Shop Now"),
            FirstAidKit(R.drawable.tweezers, "Tweezers", R.string.tweezers_description, "Shop Now"),
            FirstAidKit(R.drawable.safety_pins, "Safety Pins", R.string.safety_pins_description, "Shop Now"),
            FirstAidKit(R.drawable.emergency_blanket, "Emergency Blanket", R.string.emergency_blanket_description, "Shop Now"),
            FirstAidKit(R.drawable.mask, "Mask", R.string.mask_description, "Shop Now")
        )
    }
}
