package com.example.proxy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                val name = document.getString("name")?:""
                val image = document.getString("image")?:""
                val date = document.getString("date")?:""
                val clss = document.getString("clss")?:""
                val no = document.getString("no")?:""


                usrs.add(user_req(name, clss,no,date,image))
            }
           adapter.notifyDataSetChanged()
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Check Your Internet Connection :: Error in fetching",Toast.LENGTH_SHORT).show()
        }
    }

}