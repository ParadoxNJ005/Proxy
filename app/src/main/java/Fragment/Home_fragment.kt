package Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proxy.R
import Adapter.UserAdapter
import Adapter.statusAdapter
import DataClass.Users
import DataClass.status
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class home_fragment : Fragment() {

    private lateinit var usrs: MutableList<Users>
    private lateinit var statusUsrs: MutableList<status>
    private lateinit var recyclerView: RecyclerView
    private lateinit var StatusRV: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserAdapter
    private lateinit var StatusAdptr : statusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)


        recyclerView = view.findViewById(R.id.rv) // Replace with your RecyclerView id
        StatusRV = view.findViewById(R.id.statusRV)

        usrs = mutableListOf()
        adapter = UserAdapter(usrs, requireContext(),this)        // Replace with your adapter initialization

        statusUsrs = mutableListOf()
        StatusAdptr = statusAdapter(requireContext(), statusUsrs)     // Replace with your adapter initialization

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        StatusRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,true)
        StatusRV.adapter = StatusAdptr

        progressBar = view.findViewById(R.id.progressBar)

        showProgressBar()


        fetchFromFirestore()

        return view
    }

    private fun fetchFromFirestore() {
        usrs.clear()
        statusUsrs.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("users").orderBy("points").get().addOnSuccessListener { documents ->

            hideProgressBar()

            for (document in documents) {
                val name = document.getString("name") ?: ""
                val image = document.getString("image") ?: ""
                val points = document.getString("points") ?:""
                val room = document.getString("room") ?: ""
                val no = document.getString("no") ?: ""
                val id = document.getString("id")?:""

                if(id != FirebaseAuth.getInstance().currentUser?.uid) {

                    usrs.add(Users(name, image, points, room, no, id))

                }
                statusUsrs.add(status(name,image))
            }

            StatusAdptr.notifyDataSetChanged()
            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }
    }

    fun onItemClick(itemview: Users){

        val bundal = Bundle()
        bundal.putString("name",itemview.name?:"name")
        bundal.putString("image",itemview.image?:"image")
        bundal.putString("points", itemview.points?:"points")
        bundal.putString("room",itemview.room?:"room")
        bundal.putString("no",itemview.no?:"no")
        bundal.putString("id",itemview.id?:"id")

        val nextfragment = detail_fragment()
        nextfragment.arguments = bundal


        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,nextfragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showProgressBar() {
        progressBar.visibility = android.view.View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = android.view.View.GONE
    }

}



