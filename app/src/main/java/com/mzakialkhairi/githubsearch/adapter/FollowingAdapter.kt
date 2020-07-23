package com.mzakialkhairi.githubsearch.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzakialkhairi.githubsearch.CustomOnItemClickListener
import com.mzakialkhairi.githubsearch.R
import com.mzakialkhairi.githubsearch.model.UserFollowing
import com.mzakialkhairi.githubsearch.view.activity.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_users.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private val mData = ArrayList<UserFollowing>()
    fun setData(items: ArrayList<UserFollowing>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): FollowingViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_users, viewGroup, false)
        return FollowingViewHolder(mView)
    }

    override fun onBindViewHolder(FollowingViewHolder: FollowingViewHolder, position: Int) {
        FollowingViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserFollowing) {
            with(itemView){
                userlist_username.text = user.username
                userlist_url.text = user.url
                Picasso.with(context).load(user.avatar).into(userlist_image)

                cv_container.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(context, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.username, user.username)
                                context.startActivity(intent)

                            }
                        })
                )
            }
        }
    }
}