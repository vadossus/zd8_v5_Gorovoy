package com.example.myapplication

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NewRouteAdapter(private var routeList: List<Route>, private val listener: RouteManagementFragment) : RecyclerView.Adapter<NewRouteAdapter.NewRouteViewHolder>() {

    private var selectedRoute: Route? = null
    class NewRouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDriver: TextView = itemView.findViewById(R.id.textViewDriver)
        private val textViewBus: TextView = itemView.findViewById(R.id.textViewBus)
        private val textViewStart: TextView = itemView.findViewById(R.id.textViewStart)
        private val textViewEnd: TextView = itemView.findViewById(R.id.textViewEnd)

        fun bind(route: Route) {
            textViewDriver.text = "Водитель:  ${route.driver.toString()}"
            textViewBus.text = "Автобус: ${route.bus.toString()}"
            textViewStart.text = "Начальная станция: ${route.start.toString()}"
            textViewEnd.text = "Конечная станция: ${route.end.toString()}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return NewRouteViewHolder(view)
    }

    fun clearSelection() {
        selectedRoute = null // Сбрасываем выбранный автобус
        notifyDataSetChanged() // Обновляем адаптер для визуального отображения
    }

    override fun onBindViewHolder(holder: NewRouteViewHolder, position: Int) {
        val route = routeList[position]
        holder.bind(route)

        holder.itemView.setOnClickListener {
            selectedRoute = route // Устанавливаем выбранный автобус
            listener.onRouteClick(route)
            notifyDataSetChanged() // Обновляем адаптер для визуального отображения выбора
        }

        // Изменение фона элемента списка в зависимости от выбора
        holder.itemView.setBackgroundColor(
            if (route == selectedRoute)
                ContextCompat.getColor(holder.itemView.context, R.color.selectItemBackground)
            else
                ContextCompat.getColor(holder.itemView.context, R.color.itemBackground)
        )
    }

    fun getSelectedRoute(): Route? {
        return selectedRoute // Возвращаем выбранный автобус или null, если ничего не выбрано
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    fun updateRouteList(newRouteList: List<Route>) {
        routeList = newRouteList
        notifyDataSetChanged()
    }
}

class RouteAdapter(private var routeList: List<Route>, private val listener: PassengerActivity) : RecyclerView.Adapter<RouteAdapter.NewRouteViewHolder>() {

    private var selectedRoute: Route? = null
    class NewRouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewDriver: TextView = itemView.findViewById(R.id.textViewDriver)
        private val textViewBus: TextView = itemView.findViewById(R.id.textViewBus)
        private val textViewStart: TextView = itemView.findViewById(R.id.textViewStart)
        private val textViewEnd: TextView = itemView.findViewById(R.id.textViewEnd)

        fun bind(route: Route) {
            Log.d("jfd", "${route.driver} ${route.bus} ${route.start} ${route.end}")
            if (route != null){
                textViewDriver.text = "Водитель:  ${route.driver.toString()}"
                textViewBus.text = "Автобус: ${route.bus.toString()}"
                textViewStart.text = "Начальная станция: ${route.start.toString()}"
                textViewEnd.text = "Конечная станция: ${route.end.toString()}"
            }
            else textViewDriver.text = "Пока нет маршрутов"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return NewRouteViewHolder(view)
    }

    fun clearSelection() {
        selectedRoute = null // Сбрасываем выбранный автобус
        notifyDataSetChanged() // Обновляем адаптер для визуального отображения
    }

    override fun onBindViewHolder(holder: NewRouteViewHolder, position: Int) {
        val route = routeList[position]
        holder.bind(route)

        holder.itemView.setOnClickListener {
            selectedRoute = route // Устанавливаем выбранный автобус
            listener.onRouteClick(route)
            notifyDataSetChanged() // Обновляем адаптер для визуального отображения выбора
        }

        // Изменение фона элемента списка в зависимости от выбора
        holder.itemView.setBackgroundColor(
            if (route == selectedRoute)
                ContextCompat.getColor(holder.itemView.context, R.color.selectItemBackground)
            else
                ContextCompat.getColor(holder.itemView.context, R.color.itemBackground)
        )
    }

    fun getSelectedRoute(): Route? {
        return selectedRoute // Возвращаем выбранный автобус или null, если ничего не выбрано
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    fun updateRouteList(newRouteList: List<Route>) {
        routeList = newRouteList
        notifyDataSetChanged()
    }
}