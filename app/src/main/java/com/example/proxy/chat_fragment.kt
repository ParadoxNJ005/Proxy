package com.example.proxy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class chat_fragment : Fragment() {

    private lateinit var usrs: MutableList<user_req>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_fragment, container, false)

        recyclerView = view.findViewById(R.id.request_rv)
        usrs = mutableListOf()
        adapter = RequestAdapter(usrs, requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter // Attach the adapter to the RecyclerView

        fetchFromFirestore()

        return view
    }


    private fun fetchFromFirestore() {
        usrs.clear()
        FirebaseFirestore.getInstance().collection("request").get().addOnSuccessListener { documents->
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

//    private fun update(){
//        val db = FirebaseFirestore.getInstance()
//        val batch = db.batch()
//
//        val docRef1 = db.collection("request").document(getCurrentUserId())
//    }

    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser?.uid ?:""
    }

}


//val batch = db.batch()
//
//Update operation 1
//val docRef1 = db.collection("collection1").document("document1")
//batch.update(docRef1, "field1", "newValue1")
//
//Update operation 2
//val docRef2 = db.collection("collection2").document("document2")
//batch.update(docRef2, "field2", "newValue2")
//
//Commit the batch
//batch.commit()
//.addOnSuccessListener {
//     Batched writes successful
//}
//.addOnFailureListener { e ->
//     Handle error
//}



//}