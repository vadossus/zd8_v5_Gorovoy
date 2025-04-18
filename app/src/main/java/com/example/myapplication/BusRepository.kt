package com.example.myapplication

class BusRepository(private val busDao: BusDao) {
    suspend fun addBus(bus: Bus) = busDao.insert(bus)
    suspend fun getAllBuses() = busDao.getAllBuses()
    suspend fun updateBus(bus: Bus) = busDao.update(bus)
    suspend fun deleteBus(bus: Bus) = busDao.delete(bus)
}
