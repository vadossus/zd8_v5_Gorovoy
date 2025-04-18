package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NewDriverAdapter(private var driverList: List<Driver>, private val listener: DriverManagementFragment) : RecyclerView.Adapter<NewDriverAdapter.NewDriverViewHolder>() {

    private var selectedDriver: Driver? = null
    class NewDriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewSname: TextView = itemView.findViewById(R.id.textViewSname)
        private val textViewExperience: TextView = itemView.findViewById(R.id.textViewExperience)

        fun bind(driver: Driver) {
            textViewName.text = "Имя:  ${driver.name.toString()}"
            textViewSname.text = "Фамилия: ${driver.sname.toString()}"
            textViewExperience.text = "Стаж: ${driver.experience.toString()}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewDriverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_driver, parent, false)
        return NewDriverViewHolder(view)
    }

    fun clearSelection() {
        selectedDriver = null // Сбрасываем выбранного водителя
        notifyDataSetChanged() // Обновляем адаптер для визуального отображения
    }

    override fun onBindViewHolder(holder: NewDriverViewHolder, position: Int) {
        val driver = driverList[position]
        holder.bind(driver)

        holder.itemView.setOnClickListener {
            selectedDriver = driver // Устанавливаем выбранного водителя
            listener.onDriverClick(driver)
            notifyDataSetChanged() // Обновляем адаптер для визуального отображения выбора
        }

        // Изменение фона элемента списка в зависимости от выбора
        holder.itemView.setBackgroundColor(
            if (driver == selectedDriver)
                ContextCompat.getColor(holder.itemView.context, R.color.selectItemBackground)
            else
                ContextCompat.getColor(holder.itemView.context, R.color.itemBackground)
        )
    }

    fun getSelectedDriver(): Driver? {
        return selectedDriver // Возвращаем выбранного водителя или null, если ничего не выбрано
    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    fun updateDriverList(newDriverList: List<Driver>) {
        driverList = newDriverList
        notifyDataSetChanged()
    }
}

