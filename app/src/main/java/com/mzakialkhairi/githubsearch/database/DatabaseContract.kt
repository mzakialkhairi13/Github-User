package com.mzakialkhairi.githubsearch.database

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class UserFavoriteColumns : BaseColumns {

        companion object{
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
        }

    }

}