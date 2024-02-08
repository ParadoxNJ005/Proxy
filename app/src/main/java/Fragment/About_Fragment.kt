package Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.proxy.R
import com.example.proxy.databinding.FragmentAboutBinding
import com.example.proxy.databinding.FragmentSettingFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore

class About_Fragment : Fragment() {

    private lateinit var binding:FragmentAboutBinding
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false)


        progressBar = binding.progressBar

        binding.enter.setOnClickListener{
            showProgressBar()
            EnterAdmin()
        }


        return binding.root
    }

    fun EnterAdmin(){

        if(binding.code.text.toString() == "hello"){
            deleteFromFirestore()
        }else{
            hideProgressBar()
            Toast.makeText(requireContext(), " This page consists of backend of this App (Only Admin is  Allowed to Enter)", Toast.LENGTH_LONG).show()
        }

    }

    fun deleteFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("request").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val docRef = db.collection("request").document(document.id)
                docRef.delete()
                    .addOnSuccessListener {

                    }
                    .addOnFailureListener { e ->
                        println("Error deleting document ${document.id}: $e")
                    }
            }
            hideProgressBar()
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = android.view.View.GONE
    }


}