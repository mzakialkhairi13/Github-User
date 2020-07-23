package com.mzakialkhairi.githubsearch.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.model.UserItems
import com.mzakialkhairi.githubsearch.view.activity.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_users.view.*

class UserItemAdapter : RecyclerView.Adapter<UserItemAdapter.UserViewHolder>() {

    private val mData = ArrayList<UserItems>()

    fun setData(items: ArrayList<UserItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_users, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
        val mContext = holder.itemView.context

        val username = mData.get(position).username.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra(DetailActivity.username, username)
            mContext.startActivity(intent)
        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItems: UserItems) {
            with(itemView) {
                userlist_username.text = userItems.username
                userlist_url.text = userItems.url
                Picasso.with(context)
                    .load(userItems.avatar)
                    .into(userlist_image)
            }
        }
    }
}