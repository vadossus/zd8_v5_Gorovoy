package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RegistrActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var spinnerRole: Spinner
    private lateinit var buttonLogin: Button
    private lateinit var buttonNavigate: Button

    private val gson = Gson()
    private val userListType = object : TypeToken<MutableList<User>>() {}.type
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registr)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        spinnerRole = findViewById(R.id.spinnerRole)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNavigate = findViewById(R.id.buttonNavigate)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        // Set up the spinner with roles
        val roles = arrayOf("Диспетчер", "Пассажир")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = adapter

        buttonLogin.setOnClickListener {
            validateInput() // Ваша функция валидации ввода
        }

        // Set up the navigation button
        buttonNavigate.setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput() {
        val email = editTextEmail.text.toString()
        val username = editTextUsername.text.toString()
        val password = editTextPassword.text.toString()
        val role = spinnerRole.selectedItem.toString()

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Не правильный формат email", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate username and password
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Логин и пароль не должны быть пустыми", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate password length
        if (password.length < 8) {
            Toast.makeText(this, "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if user already exists
        if (databaseHelper.userExists(username, email)) {
            Toast.makeText(this, "Пользователь с таким логином или email уже существует", Toast.LENGTH_SHORT).show()
            return
        }
        // Save data to SQLite database
        saveUserData(email, username, password, role)
    }

    private fun saveUserData(email: String, username: String, password: String, role: String) {
        // Create a new user object
        val newUser = User(email, username, password, role)

        // Save user to the database
        databaseHelper.addUser(newUser)

        Toast.makeText(this, "Данные пользователя сохранены", Toast.LENGTH_SHORT).show()

        val selectedRole = spinnerRole.selectedItem.toString() // Получаем выбранную роль
        when (selectedRole) {
            "Диспетчер" -> {
                // Переход на экран диспетчера
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            "Пассажир" -> {
                // Переход на экран пассажира
                val intent = Intent(this, PassengerActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "Выберите корректную роль", Toast.LENGTH_SHORT).show()
            }
        }
    }


}