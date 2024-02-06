package Activity

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import com.example.proxy.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class splash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.textView.alpha = 0f

        val fadeInAnimator = ObjectAnimator.ofFloat(binding.textView, "alpha", 0f, 1f)
        fadeInAnimator.duration = 2500 // 2.5 seconds
        fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
        fadeInAnimator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserAuthentication()
        }, 3000) // 3 seconds
    }

    private fun checkUserAuthentication() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            autoLoginFailed()
        } else {
            autoLoginSuccess(currentUser.uid)
        }
    }

    private fun autoLoginSuccess(userId: String) {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun autoLoginFailed() {
        Toast.makeText(this, "Sign in Please", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, signin::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
