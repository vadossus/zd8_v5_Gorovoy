package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DriverManagementFragment : Fragment(R.layout.fragment_driver_management), OnDriverClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var driverAdapter: NewDriverAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextExperience: EditText
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText

    private val gson = Gson()
    private val userListType = object : TypeToken<MutableList<User>>() {}.type
    private lateinit var databaseHelper: DatabaseHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        databaseHelper = DatabaseHelper(requireContext())

        // Инициализация элементов интерфейса
        editTextName = view.findViewById<EditText>(R.id.editTextDriverName)
        editTextSurname = view.findViewById<EditText>(R.id.editTextDriverSurname)
        editTextExperience = view.findViewById<EditText>(R.id.editTextDriverExperience)
        editTextLogin = view.findViewById<EditText>(R.id.editTextDriverLogin)
        editTextPassword = view.findViewById<EditText>(R.id.editTextDriverPassword)
        recyclerView = view.findViewById(R.id.recyclerViewDrivers)

        // Setup RecyclerView
        driverAdapter = NewDriverAdapter(emptyList(), this) // Передаем текущий фрагмент как слушатель
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = driverAdapter

        view.findViewById<Button>(R.id.back).setOnClickListener {
            val intent = Intent(requireActivity(), FirstActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.buttonAddDriver).setOnClickListener {
            addDriver( editTextName, editTextSurname, editTextExperience)
            viewModel.getAllDrivers().observe(viewLifecycleOwner) { drivers ->
                driverAdapter.updateDriverList(drivers) // Update bus list in adapter
                val driverCount = drivers.size
                Toast.makeText(requireContext(), "Количество водителей: $driverCount",  Toast.LENGTH_SHORT).show()
            }
            clearInputFields()

        }

        view.findViewById<Button>(R.id.buttonViewDrivers).setOnClickListener {
            viewModel.getAllDrivers().observe(viewLifecycleOwner) { drivers ->
                driverAdapter.updateDriverList(drivers) // Update bus list in adapter
                val driverCount = drivers.size
                Toast.makeText(requireContext(), "Количество водителей: $driverCount", Toast.LENGTH_SHORT).show()
            }
            clearInputFields()
        }

        view.findViewById<Button>(R.id.buttonDeleteDriver).setOnClickListener {
            deleteDriver()
            viewModel.getAllDrivers().observe(viewLifecycleOwner) { drivers ->
                driverAdapter.updateDriverList(drivers) // Update bus list in adapter
                val driverCount = drivers.size
                Toast.makeText(requireContext(), "Количество водителей: $driverCount", Toast.LENGTH_SHORT).show()
            }
            clearInputFields()
        }

        view.findViewById<Button>(R.id.buttonEditDriver).setOnClickListener {
            editDriver(editTextName, editTextSurname, editTextExperience)
            viewModel.getAllDrivers().observe(viewLifecycleOwner) { drivers ->
                driverAdapter.updateDriverList(drivers) // Update bus list in adapter
                val driverCount = drivers.size
                Toast.makeText(requireContext(), "Количество водителей: $driverCount", Toast.LENGTH_SHORT).show()
            }
            clearInputFields()
        }
    }

    private fun addDriver(editTextYearOfManufacture: EditText, editTextNumberOfSeats: EditText, editTextMaxServiceLife: EditText) {
        val name = editTextName.text.toString()
        val sname = editTextSurname.text.toString()
        val experience = editTextExperience.text.toString().toIntOrNull()
        val login = editTextLogin.text.toString()
        val password = editTextPassword.text.toString()
        if (name != null  && sname != null && experience != null && experience > 5 && login != null && password != null) {
            if (password.length < 8) {
                Toast.makeText(requireContext(), "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show()
                return
            }else{
                validateInput()
                clearInputFields() // Clear input fields

            }
        } else {
            Toast.makeText(requireContext(), "Введите корректные данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteDriver() {
        val selectedDriver = driverAdapter.getSelectedDriver() // Получите выбранный автобус
        if (selectedDriver != null) {
            viewModel.deleteDriver(selectedDriver) // Удалите автобус через ViewModel
            driverAdapter.clearSelection() // Сбрасываем выбор после удаления
            Toast.makeText(requireContext(), "Водитель удален", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Выберите водителя для удаления", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editDriver(editTextName: EditText, editTextSurname: EditText, editTextExperience: EditText) {
        val selectedDriver = driverAdapter.getSelectedDriver() // Получите выбранный автобус
        if (selectedDriver != null) {
            val name = editTextName.text.toString()
            val sname = editTextSurname.text.toString()
            val experience = editTextExperience.text.toString().toIntOrNull()
            val login = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            // Validate inputs
            if (name != null  && sname != null && experience != null && experience > 5 && login != null && password != null) {
                if (password.length < 8) {
                    Toast.makeText(requireContext(), "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show()
                    return
                }else{
                // Обновляем данные автобуса
                val updatedDriver = selectedDriver.copy(
                    name = name,
                    sname = sname,
                    experience = experience,
                )
                viewModel.updateDriver(updatedDriver) // Обновите автобус через ViewModel
                driverAdapter.clearSelection() // Сбрасываем выбор после редактирования
                Toast.makeText(requireContext(), "водитель обновлен", Toast.LENGTH_SHORT).show()
                validateInput()
                clearInputFields() // Очищаем поля ввода
                }
            } else {
                Toast.makeText(requireContext(), "Введите корректные данные", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Выберите водителя для редактирования", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        // Clear all input fields
        view?.findViewById<EditText>(R.id.editTextDriverName)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextDriverSurname)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextDriverExperience)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextDriverLogin)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextDriverPassword)?.text?.clear()
    }

    override fun onDriverClick(driver: Driver) {
        // Обновите поля ввода значениями выбранного водителя
        editTextName.setText(driver.name)
        editTextSurname.setText(driver.sname)
        editTextExperience.setText(driver.experience.toString())
        editTextLogin.setText(driver.login)
        editTextPassword.setText(driver.password)
        // Здесь можно добавить логику для обновления других полей, если необходимо
    }

    private fun saveUserData(email: String, username: String, password: String, role: String) {
        databaseHelper = DatabaseHelper(requireContext())

        val newUser = User(email = email,username = username, password =  password,role =  role)

        databaseHelper.addUser(newUser)

        Toast.makeText(requireContext(), "Данные пользователя сохранены", Toast.LENGTH_SHORT).show()
        }

    private fun validateInput() {
        val username = editTextLogin.text.toString()
        val password = editTextPassword.text.toString()

        // Validate username and password
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Логин и пароль не должны быть пустыми", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate password length
        if (password.length < 8) {
            Toast.makeText(requireContext(), "Пароль должен содержать не менее 8 символов", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if user already exists
        if (databaseHelper.userExists(username, "${username}@gmail.com")) {
            Toast.makeText(requireContext(), "Пользователь с таким логином или email уже существует", Toast.LENGTH_SHORT).show()
            return
        }

        // Save data to SQLite database
        saveUserData("${username}@gmail.com", username, password, "Водитель")
        val name = editTextName.text.toString()
        val sname = editTextSurname.text.toString()
        val experience = editTextExperience.text.toString().toIntOrNull()
        val login = editTextLogin.text.toString()
        if (experience == null) {
            return
        }
        val newDriver = Driver(name = name, sname = sname, experience = experience, login = login, password = password)
        viewModel.addDriver(newDriver) // Add bus to ViewModel
        Toast.makeText(requireContext(), "Водитель добавлен", Toast.LENGTH_SHORT).show()

    }
}