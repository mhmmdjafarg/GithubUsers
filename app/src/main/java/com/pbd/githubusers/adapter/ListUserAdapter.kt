package com.pbd.githubusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.pbd.githubusers.ItemSearch
import com.pbd.githubusers.R
import de.hdodenhof.circleimageview.CircleImageView

@GlideModule
class ListUserAdapter(private val listUser: ArrayList<ItemSearch>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
    )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (avatarUrl, login) = listUser[position]
        Glide.with(holder.itemView)
            .load(avatarUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_placeholder)
            .centerCrop()
            .into(holder.imgPhoto)

        holder.tvName.text = login

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        var imgPhoto: CircleImageView = itemView.findViewById(R.id.img_item_photo)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemSearch)
    }

    fun setData(data: List<ItemSearch>) {
        listUser.clear()
        listUser.addAll(data)
        notifyDataSetChanged()
    }

}
