package com.example.android.firstresponse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

class HelplineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helpline)

        // Hide the Action Bar for this activity
        supportActionBar?.hide()

        // Initialize LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView?.apply {
            setAnimation(R.raw.emerg) // Ensure this matches your animation file
            playAnimation()
            repeatCount = LottieDrawable.INFINITE // Optional: loop animation
        }

        // Phone numbers for the health facilities
        val phoneNumbers = mapOf(
            R.id.buttonOurLadyOfPerpetualHelp to "09123456789",
            R.id.buttonPolanguiRhu to "09123456789",
            R.id.buttonPerilloGeneralHospital to "09123456789",
            R.id.buttonIsipGeneralHospital to "09123456789",
            R.id.buttonOasDistrictHospital to "09123456789",
            R.id.buttonOasRhu to "09123456789",
            R.id.buttonLigaoCityHospital to "09123456789",
            R.id.buttonLigaoRhu to "09123456789"
        )

        // Initialize buttons and set click listeners
        val buttonOurLadyOfPerpetualHelp = findViewById<Button>(R.id.buttonOurLadyOfPerpetualHelp)
        val buttonPolanguiRhu = findViewById<Button>(R.id.buttonPolanguiRhu)
        val buttonPerilloGeneralHospital = findViewById<Button>(R.id.buttonPerilloGeneralHospital)
        val buttonIsipGeneralHospital = findViewById<Button>(R.id.buttonIsipGeneralHospital)
        val buttonOasDistrictHospital = findViewById<Button>(R.id.buttonOasDistrictHospital)
        val buttonOasRhu = findViewById<Button>(R.id.buttonOasRhu)
        val buttonLigaoCityHospital = findViewById<Button>(R.id.buttonLigaoCityHospital)
        val buttonLigaoRhu = findViewById<Button>(R.id.buttonLigaoRhu)

        buttonOurLadyOfPerpetualHelp.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonOurLadyOfPerpetualHelp]!!)
        }

        buttonPolanguiRhu.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonPolanguiRhu]!!)
        }

        buttonPerilloGeneralHospital.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonPerilloGeneralHospital]!!)
        }

        buttonIsipGeneralHospital.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonIsipGeneralHospital]!!)
        }

        buttonOasDistrictHospital.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonOasDistrictHospital]!!)
        }

        buttonOasRhu.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonOasRhu]!!)
        }

        buttonLigaoCityHospital.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonLigaoCityHospital]!!)
        }

        buttonLigaoRhu.setOnClickListener {
            dialPhoneNumber(phoneNumbers[R.id.buttonLigaoRhu]!!)
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
                R.id.bottomSaved -> {
                    val intent = Intent(this, SavedActivity::class.java)
                    startActivity(intent)
                    // Apply transition animations
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                R.id.bottomHelpline -> {
                    // Already in HelplineActivity, no need to navigate
                    true
                }
                else -> false
            }
        }

        // Set the Helpline item as selected
        bottomNavigationView.menu.findItem(R.id.bottomHelpline).isChecked = true
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }
}
