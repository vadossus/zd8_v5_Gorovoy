package com.example.myapplication

class DriverRepository(private val driverDao: DriverDao) {
    suspend fun addDriver(driver: Driver) = driverDao.insert(driver)
    suspend fun getAllDrivers() = driverDao.getAllDrivers()
    suspend fun updateDriver(driver: Driver) = driverDao.update(driver)
    suspend fun deleteDriver(driver: Driver) = driverDao.delete(driver)
}