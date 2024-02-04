package com.example.proxy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.proxy.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User

class signin : AppCompatActivity() {


    private lateinit var tosignup: TextView
    private lateinit var etpass : EditText
    lateinit var etemail : EditText
    lateinit var btnLogin: Button

    //create firebaseauth object
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // Full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //view Binding
        btnLogin = findViewById(R.id.submit)
        etemail = findViewById(R.id.email)
        etpass = findViewById(R.id.password)

        //initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener(){
            login()
        }


    }



    private fun login() {
        val email = etemail.text.toString()
        val pass = etpass.text.toString()

        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this){
                task->
            if(task.isSuccessful){
                Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: Finish the current activity to prevent going back to the login screen
            }else{
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()

            }
        }
    }
}

//
//package com.example.esehi.Activity
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.WindowManager
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import com.example.esehi.Model.User
//import com.example.esehi.R
//import com.example.esehi.firebase.FirestoreClass
//import com.google.firebase.auth.FirebaseAuth
//
//class signin : AppCompatActivity() {
