package com.example.proxy

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

class splashscreen : AppCompatActivity() {
    private val SPLASH_DELAY = 2000L // 2 seconds
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)




        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val logoview: ImageView = findViewById(R.id.logo)
        logoview.alpha = 0f

        // Fade in animation for the logo using ObjectAnimator
        val fadeInAnimator = ObjectAnimator.ofFloat(logoview, "alpha", 0f, 1f)
        fadeInAnimator.duration = 1000 // 1 second
        fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
        fadeInAnimator.start()

//        // Initialize the MediaPlayer with the audio file
//        mediaPlayer = MediaPlayer.create(this, R.raw.music)
//
//        // Start playing the audio
//        mediaPlayer?.start()

        // Using a Handler to delay the transition to the main activity
        Handler().postDelayed({
            val mainIntent = Intent(this, MainActivity::class.java)

            // Adding transition animation between activities
            startActivity(mainIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, SPLASH_DELAY)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer?.release()
//    }

}