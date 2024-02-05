package Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.proxy.R
import com.example.proxy.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class signin : AppCompatActivity() {


    private lateinit var signupbtn: Button
    private lateinit var etpass: EditText
    private lateinit var etemail: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    private lateinit var progressBar: ProgressBar

    private lateinit var binding:ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // View Binding
        btnLogin = findViewById(R.id.submit)
        etemail = findViewById(R.id.email)
        etpass = findViewById(R.id.password)
        signupbtn = findViewById(R.id.signup) // Make sure the ID corresponds to the one in your layout
        progressBar = findViewById(R.id.progressBar)
        // Initializing Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            showProgressBar()
            login()
        }

        signupbtn.setOnClickListener {
            startActivity(Intent(this, signup::class.java))
            finish()
        }
    }

    private fun login() {
        val email = etemail.text.toString()
        val pass = etpass.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            hideProgressBar()
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = android.view.View.GONE
    }
}
