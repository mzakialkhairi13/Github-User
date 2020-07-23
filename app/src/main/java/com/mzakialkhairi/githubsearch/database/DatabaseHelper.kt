package com.mzakialkhairi.githubsearch.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mzakialkhairi.githubsearch.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "db_github"
        private const val DATABASE_VERSION = 1

        private val CREATE_TABLE_NOTE =
            "CREATE TABLE $TABLE_NAME" +
                    " (${DatabaseContract.UserFavoriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " ${DatabaseContract.UserFavoriteColumns.USERNAME} TEXT NOT NULL," +
                    " ${DatabaseContract.UserFavoriteColumns.AVATAR} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}