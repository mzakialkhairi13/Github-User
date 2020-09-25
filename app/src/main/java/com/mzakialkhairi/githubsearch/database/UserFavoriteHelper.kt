package com.mzakialkhairi.githubsearch.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME

class UserFavoriteHelper (context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserFavoriteHelper? = null

        fun getInstance(context: Context): UserFavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserFavoriteHelper(context)
            }
    }

    @Throws(android.database.SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }


    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} ASC"
        )
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteUserFavorite(id: String): Int {
        Log.d("id",id)
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'", null)
    }


    fun update(username: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$USERNAME = '$username'", arrayOf(username))

    }

    fun deleteById(username: String): Int {
        return database.delete(TABLE_NAME, "$USERNAME = '$username'", null)
    }

}
