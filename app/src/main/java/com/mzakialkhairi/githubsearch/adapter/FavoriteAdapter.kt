package com.mzakialkhairi.githubsearch.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzakialkhairi.githubsearch.CustomOnItemClickListener
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.model.UserFavorite
import com.mzakialkhairi.githubsearch.view.activity.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_users.view.*

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<UserFavorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_users, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    inner class FavoriteViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(uf: UserFavorite) {
            with(itemView) {
                userlist_username.text = uf.username
                userlist_url.text = "https://github.com/${uf.username}"
                Picasso.with(context).load(uf.avatar).into(userlist_image)

                cv_container.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.username, uf.username)
                                intent.putExtra(DetailActivity.EXTRA_POSITION,position)
                                intent.putExtra(DetailActivity.EXTRA_NOTE,uf)
                                activity.startActivity(intent)

                            }
                        })
                )

            }


        }
    }
}