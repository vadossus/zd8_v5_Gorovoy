package com.example.myapplication

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate


class DriverActivity : AppCompatActivity() , OnBusClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var busAdapter: BusAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var datePicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.myapplication.R.layout.activity_driver)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Initialize UI elements
        recyclerView = findViewById(com.example.myapplication.R.id.recyclerViewDrivers)
        datePicker = findViewById(com.example.myapplication.R.id.dataPicker)

        // Setup RecyclerView
        busAdapter = BusAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = busAdapter

        findViewById<Button>(com.example.myapplication.R.id.back).setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }


        findViewById<Button>(com.example.myapplication.R.id.buttonViewDrivers).setOnClickListener {
            viewModel.getAllBuses().observe(this) { buses ->
                busAdapter.updateBusList(buses) // Обновление списка водителей в адаптере
            }
        }

        findViewById<Button>(com.example.myapplication.R.id.buttonEditDriver).setOnClickListener {
            editBus(datePicker)
        }
    }

    private fun editBus(dataPicker: DatePicker) {
            val selectedBus = busAdapter.getSelectedBus() // Получите выбранный автобус
            if (selectedBus != null) {
                val day = dataPicker.dayOfMonth
                val month = dataPicker.month + 1 // Месяцы начинаются с 0
                val year = dataPicker.year

                // Форматируем дату
                val lastMaintenanceDate = LocalDate.of(year, month, day)

                // Получаем текущую дату
                val currentDate = LocalDate.now()

                // Получаем дату год назад
                val oneYearAgo = currentDate.minusYears(1)

                // Проверяем, что дата меньше текущей и больше чем год назад
                if (lastMaintenanceDate.isBefore(currentDate)) {
                    if (lastMaintenanceDate.isAfter(oneYearAgo)){
                        val updatedBus = selectedBus.copy(
                            lastMaintenanceDate = lastMaintenanceDate.toString() // Преобразуем дату в строку, если нужно
                        )

                        viewModel.updateBus(updatedBus)
                        viewModel.getAllBuses().observe(this) { buses ->
                            busAdapter.updateBusList(buses) // Обновление списка водителей в адаптере
                        }

                        Toast.makeText(this, "Автобус обновлен", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "Сначала пройдите техобслуживание", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Дата должна быть меньше текущей", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Выберите автобус для обновления", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onBusClick(bus: Bus) {
        // Логика обработки клика по автобусу
        Toast.makeText(this, "Вы выбрали автобус: ${bus.id}", Toast.LENGTH_SHORT).show()
    }
}


