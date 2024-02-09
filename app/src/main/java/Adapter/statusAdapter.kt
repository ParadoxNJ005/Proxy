package Adapter

import DataClass.status
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.proxy.R

class statusAdapter(
    val context: Context,
    val list: MutableList<status>
):RecyclerView.Adapter<statusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.status_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(list[position].image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        holder.name.text = list[position].name

    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val image:ImageView = itemView.findViewById(R.id.statusProfile)
        val name:TextView = itemView.findViewById(R.id.statusName)

    }

}