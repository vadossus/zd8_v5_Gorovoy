package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FirstActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonNavigateToRegister: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNavigateToRegister = findViewById(R.id.buttonNavigateToRegister)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        buttonLogin.setOnClickListener {
            validateLogin()
        }

        buttonNavigateToRegister.setOnClickListener {
            val intent = Intent(this, RegistrActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateLogin() {
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()

        // Check if the user exists and the password matches
        val user = databaseHelper.getUser(username, password)
        if (user != null) {
            Toast.makeText(this, "Успешный вход", Toast.LENGTH_SHORT).show()
            // Navigate based on user role
            when (user.role) {
                "Диспетчер" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                "Водитель" -> {
                    val intent = Intent(this, DriverActivity::class.java)
                    startActivity(intent)
                }
                "Пассажир" -> {
                    val intent = Intent(this, PassengerActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    // Default case if role is not recognized
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            finish() // Close the FirstActivity
        } else {
            Toast.makeText(this, "Не правильный логин или пароль", Toast.LENGTH_SHORT).show()
        }
    }
}