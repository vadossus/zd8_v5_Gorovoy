package com.example.myapplication

class RouteRepository(private val routeDao: RouteDao) {
    suspend fun addRoute(route: Route) = routeDao.insert(route)
    suspend fun getAllRoutes() = routeDao.getAllRoutes()
    suspend fun updateRoute(route: Route) = routeDao.update(route)
    suspend fun deleteRoute(route: Route) = routeDao.delete(route)
}