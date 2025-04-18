package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BusManagementFragment : Fragment(R.layout.fragment_bus_management), OnBusClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var busAdapter: NewBusAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerFuelType: Spinner
    private lateinit var editTextYearOfManufacture: EditText
    private lateinit var editTextNumberOfSeats: EditText
    private lateinit var editTextMaxServiceLife: EditText


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Initialize UI elements
        spinnerFuelType = view.findViewById(R.id.spinnerFuelType)
        editTextYearOfManufacture = view.findViewById(R.id.editTextYearOfManufacture)
        editTextNumberOfSeats = view.findViewById(R.id.editTextNumberOfSeats)
        editTextMaxServiceLife = view.findViewById(R.id.editTextMaxServiceLife)
        recyclerView = view.findViewById(R.id.recyclerViewBuses)

        // Setup Spinners
        setupSpinners(spinnerFuelType)

        // Setup RecyclerView
        busAdapter = NewBusAdapter(emptyList(), this) // Передаем текущий фрагмент как слушатель
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = busAdapter

        view.findViewById<Button>(com.example.myapplication.R.id.back).setOnClickListener {
            val intent = Intent(requireActivity(), FirstActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.buttonAddBus).setOnClickListener {
            addBus(spinnerFuelType, editTextYearOfManufacture, editTextNumberOfSeats, editTextMaxServiceLife)
            viewModel.getAllBuses().observe(viewLifecycleOwner) { buses ->
                busAdapter.updateBusList(buses) // Update bus list in adapter
                val busCount = buses.size
                Toast.makeText(requireContext(), "Количество автобусов: $busCount", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.buttonViewBuses).setOnClickListener {
            viewModel.getAllBuses().observe(viewLifecycleOwner) { buses ->
                busAdapter.updateBusList(buses) // Update bus list in adapter
                val busCount = buses.size
                Toast.makeText(requireContext(), "Количество автобусов: $busCount", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.buttonDeleteBuses).setOnClickListener {
            deleteBus()
            viewModel.getAllBuses().observe(viewLifecycleOwner) { buses ->
                busAdapter.updateBusList(buses) // Update bus list in adapter
                val busCount = buses.size
                Toast.makeText(requireContext(), "Количество автобусов: $busCount", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.buttonEditBus).setOnClickListener {
            editBus(spinnerFuelType, editTextYearOfManufacture, editTextNumberOfSeats, editTextMaxServiceLife)
            viewModel.getAllBuses().observe(viewLifecycleOwner) { buses ->
                busAdapter.updateBusList(buses) // Update bus list in adapter
                val busCount = buses.size
                Toast.makeText(requireContext(), "Количество автобусов: $busCount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBusClick(bus: Bus) {
        // Обновите EditText и Spinner значениями выбранного автобуса
        editTextYearOfManufacture.setText(bus.yearOfManufacture.toString())
        editTextNumberOfSeats.setText(bus.numberOfSeats.toString())
        editTextMaxServiceLife.setText(bus.maxServiceLife.toString())

        // Установите значение для spinnerFuelType
        val fuelTypePosition = getFuelTypePosition(bus.fuelType)
        spinnerFuelType.setSelection(fuelTypePosition)
    }

    private fun addBus(spinnerFuelType: Spinner, editTextYearOfManufacture: EditText, editTextNumberOfSeats: EditText, editTextMaxServiceLife: EditText) {
        val fuelType = spinnerFuelType.selectedItem.toString()
        val yearOfManufacture = editTextYearOfManufacture.text.toString().toIntOrNull()
        val numberOfSeats = editTextNumberOfSeats.text.toString().toIntOrNull()
        val maxServiceLife = editTextMaxServiceLife.text.toString().toIntOrNull()

        // Validate inputs
        if (yearOfManufacture != null && yearOfManufacture > 1990 && yearOfManufacture < 2024 &&
            numberOfSeats != null && numberOfSeats > 15 && numberOfSeats < 50 &&
            maxServiceLife != null && maxServiceLife > 5 && maxServiceLife < 25) {

            val newBus = Bus(yearOfManufacture = yearOfManufacture, numberOfSeats = numberOfSeats, fuelType = fuelType, maxServiceLife = maxServiceLife)
            viewModel.addBus(newBus) // Add bus to ViewModel
            Toast.makeText(requireContext(), "Автобус добавлен", Toast.LENGTH_SHORT).show()
            clearInputFields() // Clear input fields
        } else {
            Toast.makeText(requireContext(), "Введите корректные данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteBus() {
        val selectedBus = busAdapter.getSelectedBus() // Получите выбранный автобус
        if (selectedBus != null) {
            viewModel.deleteBus(selectedBus) // Удалите автобус через ViewModel
            busAdapter.clearSelection() // Сбрасываем выбор после удаления
            Toast.makeText(requireContext(), "Автобус удален", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Выберите автобус для удаления", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editBus(spinnerFuelType: Spinner, editTextYearOfManufacture: EditText, editTextNumberOfSeats: EditText, editTextMaxServiceLife: EditText) {
        val selectedBus = busAdapter.getSelectedBus() // Получите выбранный автобус
        if (selectedBus != null) {
            val fuelType = spinnerFuelType.selectedItem.toString()
            val yearOfManufacture = editTextYearOfManufacture.text.toString().toIntOrNull()
            val numberOfSeats = editTextNumberOfSeats.text.toString().toIntOrNull()
            val maxServiceLife = editTextMaxServiceLife.text.toString().toIntOrNull()

            // Validate inputs
            if (yearOfManufacture != null && yearOfManufacture > 1990 && yearOfManufacture < 2024 &&
                numberOfSeats != null && numberOfSeats > 15 && numberOfSeats < 50 &&
                maxServiceLife != null && maxServiceLife > 5 && maxServiceLife < 25) {

                // Обновляем данные автобуса
                val updatedBus = selectedBus.copy(
                    yearOfManufacture = yearOfManufacture,
                    numberOfSeats = numberOfSeats,
                    fuelType = fuelType,
                    maxServiceLife = maxServiceLife
                )
                viewModel.updateBus(updatedBus) // Обновите автобус через ViewModel
                busAdapter.clearSelection() // Сбрасываем выбор после редактирования
                Toast.makeText(requireContext(), "Автобус обновлен", Toast.LENGTH_SHORT).show()
                clearInputFields() // Очищаем поля ввода
            } else {
                Toast.makeText(requireContext(), "Введите корректные данные", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Выберите автобус для редактирования", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinners(spinnerFuelType: Spinner) {
        val fuelTypes = arrayOf("Дизельное", "Карбюраторное", "Сжатый газ", "Сжиженный газ")
        val fuelTypeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fuelTypes)
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFuelType.adapter = fuelTypeAdapter
    }

    private fun clearInputFields() {
        // Clear all input fields
        view?.findViewById<EditText>(R.id.editTextYearOfManufacture)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextNumberOfSeats)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextMaxServiceLife)?.text?.clear()
    }

    private fun getFuelTypePosition(fuelType: String): Int {
        val fuelTypes = arrayOf("Дизельное", "Карбюраторное", "Сжатый газ", "Сжиженный газ")
        return fuelTypes.indexOf(fuelType) // Возвращает индекс типа топлива или -1, если не найдено
    }
}