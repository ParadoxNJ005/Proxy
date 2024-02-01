package com.example.proxy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class home_fragment : Fragment() {

    private lateinit var usrs: MutableList<users>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter // Assuming you have an adapter for your RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        recyclerView = view.findViewById(R.id.rv) // Replace with your RecyclerView id
        usrs = mutableListOf()
        adapter = UserAdapter(usrs, requireContext(),this) // Replace with your adapter initialization

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter



        fetchFromFirestore()

        return view
    }

    private fun fetchFromFirestore() {
        usrs.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image") ?: ""
                val points = document.getString("points") ?:""
                val room = document.getString("room") ?: ""
                val no = document.getString("no") ?: ""


                usrs.add(users(name, image, points,room,no))
            }

            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }
    }

    fun onItemClick(itemview: users){

        val bundal = Bundle()
        bundal.putString("name",itemview.name?:"name")
        bundal.putString("image",itemview.image?:"image")
        bundal.putString("points", itemview.points?:"points")
        bundal.putString("room",itemview.room?:"room")
        bundal.putString("no",itemview.no?:"no")

        val nextfragment = detail_fragment()
        nextfragment.arguments = bundal


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,nextfragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
