package com.route4me.printer.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.route4me.printer.R
import com.route4me.printer.model.BTPrinter
import kotlinx.android.synthetic.main.bt_device_item.view.*


class BTDeviceViewHolder constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    constructor(parent: ViewGroup) :
            this(
                LayoutInflater.from(parent.context).inflate(R.layout.bt_device_item, parent, false)
            )

    fun bind(item: BTPrinter, callback : (result: BTPrinter?) -> Unit) {
        itemView.apply {
            name.text = item.name
            mac.text = item.macAddress
            setOnClickListener {
                callback.invoke(item)
            }
        }
    }

}