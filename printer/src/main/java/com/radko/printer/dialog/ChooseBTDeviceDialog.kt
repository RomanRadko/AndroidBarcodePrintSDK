package com.radko.printer.dialog

import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.radko.printer.R
import com.radko.printer.adapter.BTDevicesAdapter
import com.radko.printer.model.BTPrinter
import com.radko.printer.model.prefs.PrintPreferences
import kotlinx.android.synthetic.main.choose_bt_device_dialog_layout.*
import java.util.*

class ChooseBTDeviceDialog : DialogFragment() {

    companion object {
        const val TAG: String = "ChooseBTDeviceDialog"
    }

    private var mPrintersList: MutableList<BTPrinter> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_bt_device_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPairedPrinters()
    }

    /**
     * get paired printers
     */
    private fun getPairedPrinters() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE
            )
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(enableBtIntent)
        }
        val pairedDevices = getPairedBluetoothDevice(bluetoothAdapter)
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                val printer = BTPrinter(device.address, device.name)
                mPrintersList.add(printer)
            }
        } else {
            //todo: show no results view
        }
        initPrintersList()
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun getPairedBluetoothDevice(bluetoothAdapter: BluetoothAdapter): List<BluetoothDevice> {
        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices == null || pairedDevices.size == 0) {
            return ArrayList()
        }
        val devices = arrayListOf<BluetoothDevice>()
        for (device in pairedDevices) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                devices.add(device)
            } else {
                if (device.type != BluetoothDevice.DEVICE_TYPE_LE) {
                    devices.add(device)
                }
            }
        }
        return devices
    }

    private fun initPrintersList() {
        val adapter = BTDevicesAdapter {
            PrintPreferences.getInstance(context!!).deviceName = it!!.name
            PrintPreferences.getInstance(context!!).macAddress = it.macAddress
            showManufacturerDialog()
            dismiss()
        }
        adapter.setDeviceList(mPrintersList)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val horizontalDecoration = DividerItemDecoration(
            devicesList.context,
            DividerItemDecoration.VERTICAL
        )
        val horizontalDivider =
            ContextCompat.getDrawable(activity!!, R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider!!)
        devicesList.addItemDecoration(horizontalDecoration)
        devicesList.layoutManager = layoutManager
        devicesList.adapter = adapter
    }

    private fun showManufacturerDialog() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        ChooseManufacturerDialog().show(transaction,
            ChooseManufacturerDialog.TAG
        )
    }

}