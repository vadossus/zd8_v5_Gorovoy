package com.example.myapplication

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class NewBusAdapter(private var busList: List<Bus>, private val listener: BusManagementFragment) : RecyclerView.Adapter<NewBusAdapter.NewBusViewHolder>() {

    private var selectedBus: Bus? = null
    class NewBusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewYear: TextView = itemView.findViewById(R.id.textViewYear)
        private val textViewSeats: TextView = itemView.findViewById(R.id.textViewSeats)
        private val textViewFuel: TextView = itemView.findViewById(R.id.textViewFuel)
        private val textViewMaxLife: TextView = itemView.findViewById(R.id.textViewMaxLife)
        private val textViewNumber: TextView = itemView.findViewById(R.id.textViewNumber)

        fun bind(bus: Bus) {
            textViewYear.text = "Год выпуска:  ${bus.yearOfManufacture.toString()}"
            textViewSeats.text = "Количество сидячих мест: ${bus.numberOfSeats.toString()}"
            textViewFuel.text = "Тип топлива: ${bus.fuelType}"
            textViewMaxLife.text = "Максимальный срок службы: ${bus.maxServiceLife.toString()}"
            textViewNumber.text = "Номер: ${bus.id.toString()}"
            Log.d("MyActivity", bus.id.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewBusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bus, parent, false)
        return NewBusViewHolder(view)
    }

    fun clearSelection() {
        selectedBus = null // Сбрасываем выбранный автобус
        notifyDataSetChanged() // Обновляем адаптер для визуального отображения
    }

    override fun onBindViewHolder(holder: NewBusViewHolder, position: Int) {
        val bus = busList[position]
        holder.bind(bus)

        holder.itemView.setOnClickListener {
            selectedBus = bus // Устанавливаем выбранный автобус
            listener.onBusClick(bus)
            notifyDataSetChanged() // Обновляем адаптер для визуального отображения выбора
        }

        // Изменение фона элемента списка в зависимости от выбора
        holder.itemView.setBackgroundColor(
            if (bus == selectedBus)
                ContextCompat.getColor(holder.itemView.context, R.color.selectItemBackground)
            else
                ContextCompat.getColor(holder.itemView.context, R.color.itemBackground)
        )
    }

    fun getSelectedBus(): Bus? {
        return selectedBus // Возвращаем выбранный автобус или null, если ничего не выбрано
    }

    override fun getItemCount(): Int {
        return busList.size
    }

    fun updateBusList(newBusList: List<Bus>) {
        busList = newBusList
        notifyDataSetChanged()
    }
}
class BusAdapter(private var busList: List<Bus>, private val listener: DriverActivity) : RecyclerView.Adapter<BusAdapter.NewBusViewHolder>() {

    private var selectedBus: Bus? = null
    class NewBusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewYear: TextView = itemView.findViewById(R.id.textViewYear)
        private val textViewSeats: TextView = itemView.findViewById(R.id.textViewSeats)
        private val textViewFuel: TextView = itemView.findViewById(R.id.textViewFuel)
        private val textViewMaxLife: TextView = itemView.findViewById(R.id.textViewMaxLife)
        private val textViewNumber: TextView = itemView.findViewById(R.id.textViewNumber)
        private val textViewLastMaintenanceDate: TextView = itemView.findViewById(R.id.textViewLastMaintenanceDate)

        fun bind(bus: Bus) {
            textViewYear.text = "Год выпуска:  ${bus.yearOfManufacture.toString()}"
            textViewSeats.text = "Количество сидячих мест: ${bus.numberOfSeats.toString()}"
            textViewFuel.text = "Тип топлива: ${bus.fuelType}"
            textViewMaxLife.text = "Максимальный срок службы: ${bus.maxServiceLife.toString()}"
            textViewNumber.text = "Номер: ${bus.id.toString()}"
            if (bus.lastMaintenanceDate == null){
                textViewLastMaintenanceDate.text = "Дата последнего техобсуживания пока не заполнена"
            }
            else textViewLastMaintenanceDate.text = "Дата последнего техобсуживания: ${bus.lastMaintenanceDate}"
            Log.d("MyActivity", bus.id.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewBusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bus, parent, false)
        return NewBusViewHolder(view)
    }

    fun clearSelection() {
        selectedBus = null // Сбрасываем выбранный автобус
        notifyDataSetChanged() // Обновляем адаптер для визуального отображения
    }

    override fun onBindViewHolder(holder: NewBusViewHolder, position: Int) {
        val bus = busList[position]
        holder.bind(bus)

        holder.itemView.setOnClickListener {
            selectedBus = bus // Устанавливаем выбранный автобус
            listener.onBusClick(bus)
            notifyDataSetChanged() // Обновляем адаптер для визуального отображения выбора
        }

        // Изменение фона элемента списка в зависимости от выбора
        holder.itemView.setBackgroundColor(
            if (bus == selectedBus)
                ContextCompat.getColor(holder.itemView.context, R.color.selectItemBackground)
            else
                ContextCompat.getColor(holder.itemView.context, R.color.itemBackground)        )
    }

    fun getSelectedBus(): Bus? {
        return selectedBus // Возвращаем выбранный автобус или null, если ничего не выбрано
    }

    override fun getItemCount(): Int {
        return busList.size
    }

    fun updateBusList(newBusList: List<Bus>) {
        busList = newBusList
        notifyDataSetChanged()
    }
}