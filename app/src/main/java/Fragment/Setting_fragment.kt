package Fragment

import Activity.signin
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proxy.R
import com.example.proxy.databinding.FragmentSettingFragmentBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.Calendar

class setting_fragment : Fragment() {

    private lateinit var binding: FragmentSettingFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private val mark = null
    private lateinit var img: Any
    private val calendar = Calendar.getInstance()

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {3


        // Inflate the layout for this fragment
        binding = FragmentSettingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        progressBar = binding.progressBar

        showProgressBar()

        auth = Firebase.auth
        storageRef = Firebase.storage.reference

        binding.requestDate.setOnClickListener {

            showDatePicker()
        }

        // Check if the user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userUid = currentUser.uid
            fetchFromFirestore(userUid)
        }else{
            Toast.makeText(requireContext(), "current user is null", Toast.LENGTH_SHORT).show()
        }

        binding.submit.setOnClickListener {
            registerProxy(
                binding.requestDate,
                binding.requestNo,
                binding.requestClass,
                binding.requestName,
                img
            )
            binding.submit.isEnabled = false
        }

        binding.insta.setOnClickListener{
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse("https://www.instagram.com/itz_naitik_2002/")
            startActivity(openUrl)
        }

        binding.gmail.setOnClickListener{
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse("https://mail.google.com/mail/u/0/#inbox")
            startActivity(openUrl)
        }

        binding.git.setOnClickListener{
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse("https://github.com/ParadoxNJ005")
            startActivity(openUrl)
        }

        //Sign Out User From Firestore

        binding.signOut.setOnClickListener{
            Toast.makeText(requireContext(), "You are Signed Out!!", Toast.LENGTH_SHORT).show()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), signin::class.java))
        }
    }

    private fun fetchFromFirestore(userUid: String) {
        if (isAdded && context != null) { // Check if the fragment is added to an activity and has a non-null context
            FirebaseFirestore.getInstance().collection("details").document(userUid)
                .get()
                .addOnSuccessListener { documents ->
                    context?.let { ctx ->
                        binding.requestName.text = documents.getString("name") ?: ""
                        img = documents.getString("image") ?: ""
                        binding.requestNo.text = documents.getString("no") ?: ""

                        Glide.with(ctx)
                            .load(img)
                            .thumbnail(0.1f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.boy) // Replace with the appropriate placeholder drawable resource
                            .into(binding.img)

                        hideProgressBar()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "failed to fetch", Toast.LENGTH_SHORT).show()
                }
        }
    }




    private fun showDatePicker() {
        // Show date picker
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(requireContext(), { _, year, month, day ->
            val month = month + 1
            val msg = "$day/$month/$year"
            binding.requestDate.text = msg
        }, currentYear, currentMonth, currentDay)
        dialog.show()
    }

    private fun registerProxy(
        requestDate: TextView,
        requestNo: TextView,
        requestClass: TextInputEditText,
        requestName: TextView,
        img: Any?
    ) {
        val firestore = FirebaseFirestore.getInstance()
        val requestCollection = firestore.collection("request")

        val requestData = hashMapOf(
            "date" to requestDate.text.toString(),
            "no" to requestNo.text.toString(),
            "clss" to requestClass.text.toString(),
            "name" to requestName.text.toString(),
            "image" to img.toString(),
            "asign" to mark
        )

        requestCollection.add(requestData)
            .addOnSuccessListener { documentReference ->
                // Document added successfully
                // You can perform any additional actions here if needed
                binding.submit.isEnabled = true
                Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                binding.requestName.text = null
            }
            .addOnFailureListener { e ->
                // Error occurred while adding the document
                // Handle the error here
            }
    }

    private fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = android.view.View.GONE
    }




}
