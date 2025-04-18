package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class RouteManagementFragment : Fragment(R.layout.fragment_route_management), OnRouteClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var routeAdapter: NewRouteAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinnerDriver: Spinner
    private lateinit var spinnerBus: Spinner
    private lateinit var editTextStart: EditText
    private lateinit var editTextEnd: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Initialize UI elements
        spinnerDriver = view.findViewById(R.id.spinnerDriver)
        spinnerBus = view.findViewById(R.id.spinnerBus)
        editTextStart = view.findViewById(R.id.editTextStart)
        editTextEnd = view.findViewById(R.id.editTextEnd)
        recyclerView = view.findViewById(R.id.recyclerViewRoutes)


        // Setup Spinners
        setupSpinners(spinnerDriver, spinnerBus)

        // Setup RecyclerView
        routeAdapter = NewRouteAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = routeAdapter

        view.findViewById<Button>(com.example.myapplication.R.id.back).setOnClickListener {
            val intent = Intent(requireActivity(), FirstActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.buttonAddRoute).setOnClickListener {
            addRoute(spinnerDriver, spinnerBus, editTextStart, editTextEnd)
        }

        view.findViewById<Button>(R.id.buttonViewRoutes).setOnClickListener {
            updateRouteList()
        }

        view.findViewById<Button>(R.id.buttonDeleteRoutes).setOnClickListener {
            deleteRoute()
        }

        view.findViewById<Button>(R.id.buttonEditRoute).setOnClickListener {
            editRoute(spinnerDriver, spinnerBus, editTextStart, editTextEnd)
        }
    }

    private fun updateRouteList() {
        viewModel.getAllRoutes().observe(viewLifecycleOwner) { routes ->
            routeAdapter.updateRouteList(routes)
            val routeCount = routes.size
            Toast.makeText(requireContext(), "Количество маршрутов: $routeCount", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRouteClick(route: Route) {
        // Обновите EditText и Spinner значениями выбранного автобуса
        editTextEnd.setText(route.end.toString())
        editTextStart.setText(route.start.toString())

        // Установите значение для spinnerFuelType
        val driverPosition = getDriverPosition(route.driver)
        spinnerDriver.setSelection(driverPosition)

        val busPosition = getBusPosition(route.bus)
        spinnerDriver.setSelection(busPosition)
    }

    private fun addRoute(spinnerDriver: Spinner, spinnerBus: Spinner, editTextStart: EditText, editTextEnd: EditText) {
        val driver = spinnerDriver.selectedItem.toString()
        val bus = spinnerBus.selectedItem.toString().toIntOrNull()
        val start = editTextStart.text.toString()
        val end = editTextEnd.text.toString()

        // Validate inputs
        if (!start.isBlank() && driver != null && bus != null && !end.isBlank()) {
            if (start != end){
                val newRoute = Route(driver = driver, bus = bus, start = start, end = end)
                viewModel.addRoute(newRoute) // Add bus to ViewModel
                Toast.makeText(requireContext(), "Маршрут добавлен", Toast.LENGTH_SHORT).show()
                clearInputFields() // Clear input fields
                updateRouteList()
            } else {
                Toast.makeText(requireContext(), "Начальная и конечная станции не должны быть одинавковые", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Введите корректные данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteRoute() {
        val selectedRoute = routeAdapter.getSelectedRoute() // Получите выбранный автобус
        if (selectedRoute != null) {
            viewModel.deleteRoute(selectedRoute) // Удалите автобус через ViewModel
            routeAdapter.clearSelection() // Сбрасываем выбор после удаления
            Toast.makeText(requireContext(), "Маршрут удален", Toast.LENGTH_SHORT).show()
            updateRouteList()
        } else {
            Toast.makeText(requireContext(), "Выберите маршрут для удаления", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editRoute(spinnerDriver: Spinner, spinnerBus: Spinner, editTextStart: EditText, editTextEnd: EditText) {
        val selectedRoute = routeAdapter.getSelectedRoute() // Получите выбранный автобус
        if (selectedRoute != null) {
            val driver = spinnerDriver.selectedItem.toString()
            val bus = spinnerBus.selectedItem.toString().toIntOrNull()
            val start = editTextStart.text.toString()
            val end = editTextEnd.text.toString()

            // Validate inputs
            if (start.isNotEmpty() && driver.isNotEmpty() && bus != null && end.isNotEmpty()) {
                if (start != end) {

                    val updatedRoute = Route(driver = driver, bus = bus, start = start, end = end)
                    Log.d("sfdfd", "${updatedRoute.driver} ${updatedRoute.bus} ${updatedRoute.start} ${updatedRoute.end}")
                    viewModel.updateRoute(updatedRoute) // Update the route through ViewModel
                    routeAdapter.clearSelection() // Clear selection after editing
                    Toast.makeText(requireContext(), "Маршрут обновлен", Toast.LENGTH_SHORT).show()
                    clearInputFields() // Clear input fields
                    updateRouteList()
                }else {
                        Toast.makeText(requireContext(), "Начальная и конечная станции не должны быть одинавковые", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Введите корректные данные", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Выберите маршрут для редактирования", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpinners(spinnerDriver: Spinner, spinnerBus: Spinner) {
        viewModel.getAllDrivers().observe(viewLifecycleOwner) { driversFromDB ->
            Log.d("RouteManagementFragment", "Buses from DB: $driversFromDB")
            val driverNames = driversFromDB?.map { "${it.name} ${it.sname}" }?.toTypedArray() ?: emptyArray() // Extract bus IDs
            val driversAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, driverNames)
            driversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDriver.adapter = driversAdapter
        }

        viewModel.getAllBuses().observe(viewLifecycleOwner) { busesFromDB ->
            Log.d("RouteManagementFragment", "Buses from DB: $busesFromDB")
            val busNames = busesFromDB?.map { it.id }?.toTypedArray() ?: emptyArray() // Extract bus IDs
            val busesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, busNames)
            busesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerBus.adapter = busesAdapter
        }
    }


    private fun clearInputFields() {
        // Clear all input fields
        view?.findViewById<EditText>(R.id.editTextStart)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextEnd)?.text?.clear()
    }

    private fun getDriverPosition(driver: String): Int {
        var position = 0
        viewModel.getAllDrivers().observe(viewLifecycleOwner) { driversFromDB ->
            Log.d("RouteManagementFragment", "Buses from DB: $driversFromDB")
            val driverNames = driversFromDB?.map { "${it.name} ${it.sname}" }?.toTypedArray() ?: emptyArray() // Extract bus IDs
            val driversAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, driverNames)
            driversAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDriver.adapter = driversAdapter
            position = driverNames.indexOf(driver)
        }
        return position
    }

    private fun getBusPosition(bus: Int): Int {
        var position = 0
        viewModel.getAllBuses().observe(viewLifecycleOwner) { busesFromDB ->
            Log.d("RouteManagementFragment", "Buses from DB: $busesFromDB")
            val busNames = busesFromDB?.map { it.id }?.toTypedArray() ?: emptyArray() // Extract bus IDs
            val busesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, busNames)
            busesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerBus.adapter = busesAdapter
            position = busNames.indexOf(bus)
        }
        return position
    }
}