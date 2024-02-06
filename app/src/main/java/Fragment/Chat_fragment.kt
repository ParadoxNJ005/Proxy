package Fragment

import Activity.signin
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proxy.R
import Adapter.RequestAdapter
import DataClass.user_req
import android.content.Intent
import android.view.WindowManager
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class chat_fragment : Fragment() {

    private lateinit var usrs: MutableList<user_req>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RequestAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_chat_fragment, container, false)



        recyclerView = view.findViewById(R.id.request_rv)
        usrs = mutableListOf()
        adapter = RequestAdapter(usrs, requireContext())

        progressBar = view.findViewById(R.id.progressBar)

        showProgressBar()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter // Attach the adapter to the RecyclerView

        fetchFromFirestore()

        return view
    }


    private fun fetchFromFirestore() {
        usrs.clear()
        FirebaseFirestore.getInstance().collection("request").get().addOnSuccessListener { documents->

            hideProgressBar()

            for(document in documents){
                val docId = document.id
                val name = document.getString("name")?:""
                val image = document.getString("image")?:""
                val date = document.getString("date")?:""
                val clss = document.getString("clss")?:""
                val no = document.getString("no")?:""
                val asign = document.getString("asign")?:""

                usrs.add(user_req(name, clss,no,date,image,asign,docId))
            }
           adapter.notifyDataSetChanged()
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Check Your Internet Connection :: Error in fetching",Toast.LENGTH_SHORT).show()
        }
    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?:""
    }

    private fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = android.view.View.GONE
    }


}
