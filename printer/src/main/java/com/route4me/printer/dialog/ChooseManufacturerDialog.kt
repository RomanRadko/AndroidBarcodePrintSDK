package com.route4me.printer.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.route4me.printer.R
import com.route4me.printer.adapter.ManufacturerAdapter
import com.route4me.printer.model.PrinterManufacturer
import com.route4me.printer.model.prefs.PrintPreferences
import kotlinx.android.synthetic.main.choose_manufacturer_dialog_layout.*

class ChooseManufacturerDialog : DialogFragment() {

    companion object {
        const val TAG: String = "ChooseManufacturerDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_manufacturer_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initManufacturersList()
    }

    private fun initManufacturersList() {
        val adapter = ManufacturerAdapter {
            PrintPreferences.getInstance(context!!).manufacturer = it!!.manufacturerName
            dismiss()
        }
        adapter.setManufacturersList(PrinterManufacturer.values().toList())
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val horizontalDecoration = DividerItemDecoration(
            manufacturerList.context,
            DividerItemDecoration.VERTICAL
        )
        val horizontalDivider =
            ContextCompat.getDrawable(activity!!, R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider!!)
        manufacturerList.addItemDecoration(horizontalDecoration)
        manufacturerList.layoutManager = layoutManager
        manufacturerList.adapter = adapter
    }
}