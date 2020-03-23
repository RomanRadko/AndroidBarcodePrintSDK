package com.route4me.printer.adapter

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.route4me.printer.model.PrinterManufacturer
import com.route4me.printer.viewholder.ManufacturerViewHolder

class ManufacturerAdapter(private val callback: (result: PrinterManufacturer?) -> Unit) :
    RecyclerView.Adapter<ManufacturerViewHolder>() {

    private var deviceList: List<PrinterManufacturer> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManufacturerViewHolder {
        return ManufacturerViewHolder(parent)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ManufacturerViewHolder, position: Int) {
        holder.bind(deviceList[position], callback)
    }

    fun setManufacturersList(data: List<PrinterManufacturer>) {
        deviceList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = deviceList.size

}