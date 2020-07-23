package com.mzakialkhairi.githubsearch.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.USERNAME
import com.mzakialkhairi.githubsearch.model.UserFavorite
import java.util.ArrayList

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

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
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
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteUserFavorite(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = '$username'", null)
    }

    fun getAllNotes(): ArrayList<UserFavorite> {
        val arrayList = ArrayList<UserFavorite>()
        val cursor = database.query(
            DATABASE_TABLE, null, null, null, null, null,
            "${BaseColumns._ID} ASC", null
        )
        cursor.moveToFirst()
        var uf: UserFavorite
        if (cursor.count > 0) {
            do {
                uf = UserFavorite()
                uf.id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                uf.username= cursor.getString(cursor.getColumnIndexOrThrow(USERNAME))

                arrayList.add(uf)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun deleteNote(id: Int): Int {
        return database.delete(TABLE_NAME, "${BaseColumns._ID} = '$id'", null)
    }
}
