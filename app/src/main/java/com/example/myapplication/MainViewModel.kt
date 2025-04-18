package com.example.myapplication

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val busRepository: BusRepository
    private val driverRepository: DriverRepository
    private val routeRepository: RouteRepository

    init {
        val database = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "autopark_database"
        ).build()

        busRepository = BusRepository(database.busDao())
        driverRepository = DriverRepository(database.driverDao())
        routeRepository = RouteRepository(database.routeDao())
    }

    fun addBus(bus: Bus) = viewModelScope.launch { busRepository.addBus(bus) }
    fun getAllBuses() = liveData { emit(busRepository.getAllBuses()) }
    fun updateBus(bus: Bus) = viewModelScope.launch { busRepository.updateBus(bus) }
    fun deleteBus(bus: Bus) = viewModelScope.launch { busRepository.deleteBus(bus) }

    fun addDriver(driver: Driver) = viewModelScope.launch { driverRepository.addDriver(driver) }
    fun getAllDrivers() = liveData { emit(driverRepository.getAllDrivers()) }
    fun updateDriver(driver: Driver) = viewModelScope.launch { driverRepository.updateDriver(driver) }
    fun deleteDriver(driver: Driver) = viewModelScope.launch { driverRepository.deleteDriver(driver) }

    fun addRoute(route: Route) = viewModelScope.launch { routeRepository.addRoute(route) }
    fun getAllRoutes() = liveData { emit(routeRepository.getAllRoutes()) }
    fun updateRoute(route: Route) = viewModelScope.launch { routeRepository.updateRoute(route) }
    fun deleteRoute(route: Route) = viewModelScope.launch { routeRepository.deleteRoute(route) }
}