package Adapter

import DataClass.user_req
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proxy.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.Calendar.DAY_OF_MONTH

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

//        val currentDate = Calendar.getInstance().get(DAY_OF_MONTH)

//        if( currentItem.date.get(DAY_OF_MONTH) > currentDate) {

            holder.name.text = currentItem.name
            holder.clss.text = currentItem.clss
            holder.no.text = currentItem.no
            holder.date.text = currentItem.date

            Glide.with(context)
                .load(currentItem.image)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image)

            if (currentItem.asign != null) {
                holder.asign.setText(currentItem.asign)
            }
            if (currentItem.status != null) {
                holder.status.setText(currentItem.status)

            }

        if(holder.status.text.toString() == "success"){
            Toast.makeText(context, "hi ${holder.status.text}", Toast.LENGTH_SHORT).show()
            holder.asign.setBackgroundColor(ContextCompat.getColor(context, R.color.parrot))
        }

            holder.save.setOnClickListener {
                val assignmentText = holder.asign.text.toString()
                val statusText = holder.status.text.toString()

                if (statusText.isNotEmpty()) {
                    updateUserStatus(currentItem, statusText)
                } else {
                    Toast.makeText(context, "Status can't be Empty", Toast.LENGTH_SHORT).show()
                }

                if (assignmentText.isNotEmpty()) {
                    updateUserData(currentItem, assignmentText)
                } else {
                    Toast.makeText(context, "Assignment text cannot be Empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }
//        }else{
//        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val clss: TextView = itemView.findViewById(R.id.clss)
        val no: TextView = itemView.findViewById(R.id.no)
        val date: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.profile_img)
        val asign: EditText = itemView.findViewById(R.id.asign)
        val save: Button = itemView.findViewById(R.id.save)
        val status: EditText = itemView.findViewById(R.id.status)
    }

    private fun updateUserData(currentItem: user_req, assignmentText: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("request").document(currentItem.docId).update("asign", assignmentText)
            .addOnSuccessListener {
                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserStatus(currentItem: user_req, statusText: String) {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("request").document(currentItem.docId).update("status", statusText)
            .addOnSuccessListener {
                Toast.makeText(context, "Data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}
