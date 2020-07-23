package com.mzakialkhairi.githubsearch

import android.view.View


class CustomOnItemClickListener(private val position: Int, private val onItemClickCallBack : OnItemClickCallback) : View.OnClickListener {

    override fun onClick(view: View){
        onItemClickCallBack.onItemClicked(view,position)
    }

    interface OnItemClickCallback {
        fun onItemClicked(view: View, position: Int)
    }
}