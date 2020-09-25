package com.mzakialkhairi.githubsearch.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.mzakialkhairi.githubsearch.database.DatabaseContract.AUTHORITY
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.mzakialkhairi.githubsearch.database.UserFavoriteHelper

class GithubProvider : ContentProvider() {

    companion object {
        private const val USER_FAVORITE = 1
        private const val USER_FAVORITE_ID = 2
        private lateinit var ufHelper: UserFavoriteHelper

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE)
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                USER_FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        ufHelper = UserFavoriteHelper.getInstance(context as Context)
        ufHelper.open()
        return true
    }
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            USER_FAVORITE -> cursor = ufHelper.queryAll()
            USER_FAVORITE_ID -> cursor = ufHelper.queryByUsername(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER_FAVORITE) {
            sUriMatcher.match(uri) -> ufHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (USER_FAVORITE_ID) {
            sUriMatcher.match(uri) -> ufHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleted: Int = when (USER_FAVORITE_ID) {
            sUriMatcher.match(uri) -> ufHelper.deleteUserFavorite(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }

    fun deleteUser(value : String) {
        ufHelper.deleteUserFavorite(value)
    }
}
