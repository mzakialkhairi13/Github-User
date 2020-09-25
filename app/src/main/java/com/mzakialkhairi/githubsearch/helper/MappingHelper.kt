package com.mzakialkhairi.githubsearch.helper

import android.database.Cursor
import com.mzakialkhairi.githubsearch.database.DatabaseContract
import com.mzakialkhairi.githubsearch.model.UserFavorite
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(ufCursor: Cursor?): ArrayList<UserFavorite> {
        val ufList = ArrayList<UserFavorite>()

        ufCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.AVATAR))
                ufList.add(UserFavorite(id, username,avatar))
            }
        }
        return ufList
    }

    fun mapCursorToObject(ufCursor: Cursor?): UserFavorite {
        var uf = UserFavorite()
        ufCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.AVATAR))

            uf = UserFavorite(id, username, avatar)
        }
        return uf
    }
}
