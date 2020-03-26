package com.radko.printer.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.radko.printer.R
import com.radko.printer.model.prefs.PrintPreferences
import kotlinx.android.synthetic.main.connection_status_view_layout.view.*

class ConnectionStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.connection_status_view_layout, this, true)
    }

    fun bindData(callback : (result: Any?) -> Unit) {
        val preferences = PrintPreferences.getInstance(context)
        name.text = preferences.deviceName
        macAddress.text = preferences.macAddress
        manufacturer.text = preferences.manufacturer
        reconnectBtn.setOnClickListener {
            callback.invoke(it)
        }
    }

}