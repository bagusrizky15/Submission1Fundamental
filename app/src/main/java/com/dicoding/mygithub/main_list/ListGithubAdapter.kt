package com.dicoding.mygithub.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithub.api.ItemsItem
import com.dicoding.mygithub.R

class ListGithubAdapter(
    private val listGithub: List<ItemsItem>
    ) : RecyclerView.Adapter<ListGithubAdapter.ViewHolder> () {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_type: TextView = itemView.findViewById(R.id.user_github)
        val tv_username: TextView = itemView.findViewById(R.id.username_github)
        val image_list: ImageView = itemView.findViewById(R.id.image_list)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usersGithub = listGithub[position]
        holder.tv_type.text = usersGithub.type
        holder.tv_username.text = usersGithub.login
        Glide.with(holder.itemView.context)
            .load(usersGithub.avatarUrl)
            .into(holder.image_list)

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listGithub[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listGithub.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}