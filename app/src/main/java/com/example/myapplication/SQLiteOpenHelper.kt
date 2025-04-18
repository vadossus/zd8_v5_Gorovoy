package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_PASSWORD TEXT, "
                + "$COLUMN_ROLE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: User) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, user.email)
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_PASSWORD, user.password)
            put(COLUMN_ROLE, user.role)
        }
        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    fun getUser(username: String, password: String): User? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_EMAIL, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_ROLE),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val emailIndex = cursor.getColumnIndex(COLUMN_EMAIL)
            val usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME)
            val passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD)
            val roleIndex = cursor.getColumnIndex(COLUMN_ROLE)

            // Check if indices are valid
            if (emailIndex != -1 && usernameIndex != -1 && passwordIndex != -1 && roleIndex != -1) {
                val userEmail = cursor.getString(emailIndex)
                val userUsername = cursor.getString(usernameIndex)
                val userPassword = cursor.getString(passwordIndex)
                val role = cursor.getString(roleIndex)
                cursor.close()
                User(userEmail, userUsername, userPassword, role)
            } else {
                cursor.close()
                null // Return null if any index is invalid
            }
        } else {
            cursor.close()
            null // Return null if no user found
        }
    }

    fun userExists(username: String, email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE username = ? OR email = ?"
        val cursor = db.rawQuery(query, arrayOf(username, email))

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}