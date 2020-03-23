package com.route4me.printer.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.route4me.printer.model.BTPrinter
import com.route4me.printer.viewholder.BTDeviceViewHolder


class BTDevicesAdapter(private val callback : (result: BTPrinter?) -> Unit) : RecyclerView.Adapter<BTDeviceViewHolder>() {

    private var deviceList: List<BTPrinter> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BTDeviceViewHolder {
        return BTDeviceViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BTDeviceViewHolder, position: Int) {
        holder.bind(deviceList[position], callback)
    }

    fun setDeviceList(data: MutableList<BTPrinter>) {
        deviceList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = deviceList.size

}