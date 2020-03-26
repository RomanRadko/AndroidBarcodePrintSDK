package com.radko.printer.viewholder

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.radko.printer.R
import com.radko.printer.model.PrinterManufacturer
import kotlinx.android.synthetic.main.bt_device_item.view.name
import kotlinx.android.synthetic.main.manufacturer_item.view.*


class ManufacturerViewHolder constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) :
            this(
                LayoutInflater.from(parent.context).inflate(R.layout.manufacturer_item, parent, false)
            )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(item: PrinterManufacturer, callback : (result: PrinterManufacturer?) -> Unit) {
        itemView.apply {
            name.text = item.manufacturerName
            icon.setImageDrawable(itemView.context.getDrawable(item.icon))
            setOnClickListener {
                callback.invoke(item)
            }
        }
    }

}