package com.mzakialkhairi.cosumergithubapp.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.mzakialkhairi.githubsearch"
    const val SCHEME = "content"

    internal class UserFavoriteColumns : BaseColumns {

        companion object{
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"


            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }

}