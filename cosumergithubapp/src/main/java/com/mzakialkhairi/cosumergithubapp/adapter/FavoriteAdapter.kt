package com.mzakialkhairi.cosumergithubapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzakialkhairi.cosumergithubapp.R
import com.mzakialkhairi.cosumergithubapp.model.UserFavorite
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_users.view.*

class FavoriteAdapter(activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<UserFavorite>()
        set(listFavorite) {
            this.listFavorite.clear()
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_users, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    inner class FavoriteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(uf: UserFavorite) {
            with(itemView) {
                userlist_username.text = uf.username
                userlist_url.text = "https://github.com/${uf.username}"
                Picasso.with(context).load(uf.avatar).into(userlist_image)

            }


        }
    }
}