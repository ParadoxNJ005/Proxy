package Adapter

import Fragment.home_fragment
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
import DataClass.users

class UserAdapter(
    private val user: MutableList<users>,
    private val context: Context,
    private val itemClickListener: home_fragment
): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return user.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = user[position]

        Glide.with(context)
            .load(user[position].image)
            .thumbnail(0.1f)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.image)

        val ranking = position+1
            holder.name.text = user[position].name
            holder.points.text = user[position].points
            holder.rank.text = ranking.toString()

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(user[position])
        }
    }

    inner class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val name: TextView = itemview.findViewById(R.id.name)
        val image: ImageView = itemview.findViewById(R.id.image)
        val points: TextView = itemview.findViewById(R.id.points)
        val rank: TextView = itemview.findViewById(R.id.rank)
    }
}