package com.example.android.firstresponse

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView

class SplashScreenActivity : AppCompatActivity() {
    private val delayMillis: Long = 5000 // 2 seconds
    private val pauseProgress: Float = 0.9f // Pause the animation at 80% completion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        supportActionBar?.hide()

        // Initialize the LottieAnimationView
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView.apply {
            setAnimation(R.raw.start)
            playAnimation()
            speed = 1.0f // Normal speed

            // Set up a listener to pause the animation at a specific progress
            addAnimatorUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                if (progress >= pauseProgress) {
                    pauseAnimation()
                }
            }
        }

        // Initialize and hide the start button initially
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.alpha = 0f
        startButton.isEnabled = false

        // Show the start button after a delay
        Handler(mainLooper).postDelayed({
            startButton.alpha = 1f
            startButton.isEnabled = true
        }, delayMillis)

        // Add button click listener
        startButton.setOnClickListener {
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
