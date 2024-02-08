package Activity

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.net.Uri
import android.os.Bundle 
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.proxy.R
import DataClass.User
import DataClass.Users
import com.example.proxy.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import kotlin.properties.Delegates

class signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var etEmail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var etName: EditText
    private lateinit var roomno: EditText
    private lateinit var btnSignUp: Button
    private lateinit var signinbtn: TextView
    private lateinit var rollNo: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private val mFireStore = FirebaseFirestore.getInstance()

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        etName = findViewById(R.id.name)
        etEmail = findViewById(R.id.email)
        etConfPass = findViewById(R.id.confirm_password)
        etPass = findViewById(R.id.password)
        btnSignUp = findViewById(R.id.submit)
        rollNo = findViewById(R.id.rollno)
        signinbtn = findViewById(R.id.SignIn)
        progressBar = findViewById(R.id.progressBar)
        roomno = findViewById(R.id.roomno)

        auth = Firebase.auth
        storageRef = Firebase.storage.reference

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
            finish()
        }

        signinbtn.setOnClickListener {
            startActivity(Intent(this, signin::class.java))
            finish()
        }

        // Select and upload image


        binding.select.setOnClickListener {
            select()
        }

        binding.upload.setOnClickListener {
            if (etEmail.text.toString().isEmpty() || etPass.text.toString().isEmpty() ||
                etConfPass.text.toString().isEmpty() || etName.text.toString()
                    .isEmpty() || rollNo.text.toString().isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Upload Image after filling all other entries",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                    showProgressBar()
                    Toast.makeText(
                        this,
                        "please Wait : While your account is being created",
                        Toast.LENGTH_SHORT
                    ).show()
                    upload()


            }
        }
    }

    private fun registerUser(activity: signup, userInfo: User) {

        mFireStore.collection("details")
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("registerUser", "Error registering user", e)
                Toast.makeText(activity, "Error registering user: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun updateProfile(activity: signup, userInfo: Users){

        mFireStore.collection("users")
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                hideProgressBar()
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("registerUser", "Error registering user", e)
                Toast.makeText(activity, "Error registering user: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?: ""
    }

    // Uploading the photos code start

    private fun select() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun upload() {
        if (filePath != null) {
            val ref = storageRef.child("images/${getCurrentUserId()}")

            ref.putFile(filePath!!)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->

                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()

                    ref.downloadUrl.addOnCompleteListener { uriTask ->
                        if (uriTask.isSuccessful) {
                            val downloadUrl = uriTask.result
                            val imageUrl = downloadUrl.toString()
                            // Save imageUrl to your user object or wherever you want

                            // Update the user object with the imageUrl before calling registerUser
                            val email = etEmail.text.toString()
                            val pass = etPass.text.toString()
                            val confirmPassword = etConfPass.text.toString()
                            val name = etName.text.toString()
                            val rollNo = rollNo.text.toString()
                            val room = roomno.text.toString()
                            val points = "50"

                            if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {

                                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
                                return@addOnCompleteListener
                            }
                            if (pass != confirmPassword) {
                                Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                                return@addOnCompleteListener
                            }

                            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result?.user!!
                                    val registeredEmail = firebaseUser.email!!

                                    // Update the user object with the imageUrl
                                    val user = User(firebaseUser.uid, name, imageUrl, rollNo)

                                    //Update the Users object with the data
                                    val users = Users(name,imageUrl,points,room,rollNo,firebaseUser.uid)

                                    // Call registerUser with the updated user object
                                    registerUser(this, user)
                                    updateProfile(this,users)

                                } else {
                                    hideProgressBar()
                                    Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            hideProgressBar()
                            Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    hideProgressBar()
                    Toast.makeText(this, "Image Upload Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            hideProgressBar()
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                Glide.with(this).load(filePath).into(binding.img)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = android.view.View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,signin::class.java))
        finish()
    }
}
