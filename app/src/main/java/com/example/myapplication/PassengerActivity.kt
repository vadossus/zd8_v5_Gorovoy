package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R.id.buttonViewDrivers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class PassengerActivity : AppCompatActivity(),  OnRouteClickListener{
    private lateinit var viewModel: MainViewModel
    private lateinit var routeAdapter: RouteAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerViewRoutes)

        // Setup RecyclerView
        routeAdapter = RouteAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = routeAdapter


        viewModel.getAllRoutes().observe(this) { routes ->
            routeAdapter.updateRouteList(routes) // Обновление списка водителей в адаптере
            val driverCount = routes.size
            Toast.makeText(this, "Количество маршрутов: $driverCount", Toast.LENGTH_SHORT)
                .show()
        }

        findViewById<Button>(R.id.back).setOnClickListener {
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onRouteClick(route: Route) {
    }

}