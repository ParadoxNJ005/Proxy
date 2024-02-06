package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proxy.R
import DataClass.user_req
import com.google.firebase.firestore.FirebaseFirestore

class RequestAdapter(
    private val user: MutableList<user_req>,
    private val context: Context
) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = user[position]

        holder.name.text = user[position].name
        holder.clss.text = user[position].clss
        holder.no.text = user[position].no
        holder.date.text = user[position].date

        Glide.with(context)
            .load(user[position].image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        if (currentItem.asign != null) {
            holder.asign.setText(currentItem.asign)
        }

        holder.save.setOnClickListener {
            val assignmentText =holder.asign.text.toString()
            if (assignmentText.isNotEmpty()) {
                updateUserData(currentItem, assignmentText)
            } else {
                Toast.makeText(context, "Assignment text cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.name)
        val clss = itemView.findViewById<TextView>(R.id.clss)
        val no = itemView.findViewById<TextView>(R.id.no)
        val date = itemView.findViewById<TextView>(R.id.date)
        val image = itemView.findViewById<ImageView>(R.id.profile_img)
        var asign = itemView.findViewById<EditText>(R.id.asign)
        val save = itemView.findViewById<Button>(R.id.save)
    }

    private fun updateUserData(currentItem: user_req, assignmentText: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("request").document(currentItem.docId).update("asign", assignmentText)
            .addOnSuccessListener {
                // Update successful
                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Error updating data
                Toast.makeText(context, e.localizedMessage , Toast.LENGTH_SHORT).show()
            }
    }


}

