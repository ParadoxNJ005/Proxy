package com.example.proxy

import android.content.Context
import android.icu.text.AlphabeticIndex.ImmutableIndex
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RequestAdapter(
    private val user: MutableList<user_req>,
    private val context: Context,
   // private val itemClickListener: com.example.proxy.chat_fragment
) : RecyclerView.Adapter<RequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.proxy.R.layout.request_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text =  user[position].name
        holder.clss.text = user[position].clss
        holder.no.text = user[position].no
        holder.date.text = user[position].date

        Glide.with(context)
            .load(user[position].image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

//        holder.itemView.setOnClickListener{
//            itemClickListener.onItemClick(user[position])
//        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val name = itemView.findViewById<TextView>(R.id.name)
       val clss = itemView.findViewById<TextView>(R.id.clss)
       val no = itemView.findViewById<TextView>(R.id.no)
       val date = itemView.findViewById<TextView>(R.id.date)
       val image = itemView.findViewById<ImageView>(R.id.profile_img)

    }
}

