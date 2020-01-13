/**
 * Activity of Searching Bluetooth Printers
 */
package com.route4me.brotherprintersample

import android.annotation.TargetApi
import android.app.ListActivity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.brother.ptouch.sdk.NetPrinter
import java.util.*

class SearchBTPrinterActivity : ListActivity() {

    private var mBluetoothPrinter: MutableList<NetPrinter> = mutableListOf() // list of storing Printer information
    private var mItems: ArrayList<String> = arrayListOf()// List of storing the printer's information

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bleprinterlist)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)
        btnRefresh.setOnClickListener { getPairedPrinters() }
        getPairedPrinters()
    }

    /**
     * get paired printers
     */
    private fun getPairedPrinters() {
        // get the BluetoothAdapter
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled) {
                val enableBtIntent = Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE
                )
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(enableBtIntent)
            }
        } else {
            return
        }
        try {
            mItems.clear()
            mItems = ArrayList()
            val pairedDevices = getPairedBluetoothDevice(bluetoothAdapter)
            if (pairedDevices.isNotEmpty()) {
                for (device in pairedDevices) {
                    var strDev = ""
                    strDev += device.name + "\n" + device.address
                    mItems.add(strDev)
                    val printer = NetPrinter()
                    printer.ipAddress = ""
                    printer.macAddress = device.address
                    mBluetoothPrinter.add(printer)
                }
            } else {
                mItems.add(getString(R.string.no_bluetooth_device))
            }
            this.runOnUiThread {
                val fileList = ArrayAdapter<String>(
                    this@SearchBTPrinterActivity,
                    android.R.layout.test_list_item, mItems
                )
                this@SearchBTPrinterActivity.listAdapter = fileList
            }
        } catch (exception: Exception) {
            Log.e("Test", "exception :$exception")
        }
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

    /**
     * Called when an item in the list is tapped
     */
    override fun onListItemClick(listView: ListView?, view: View?, position: Int, id: Long) {
        val item = listAdapter.getItem(position) as String
        if (!item.equals(getString(R.string.no_bluetooth_device), ignoreCase = true)) {
            val editor: SharedPreferences.Editor =
                PreferenceManager.getDefaultSharedPreferences(this).edit()
            editor.putString(IP_ADDRESS_KEY, mBluetoothPrinter[position].ipAddress)
            editor.putString(MAC_ADDRESS_KEY, mBluetoothPrinter[position].macAddress)
            editor.putString(PRINTER_MODEL_KEY, mBluetoothPrinter[position].modelName)
            editor.apply()
            editor.commit()

        }
        finish()
    }

    companion object {
        const val  IP_ADDRESS_KEY: String = "IP_ADDRESS_KEY"
        const val  MAC_ADDRESS_KEY: String = "MAC_ADDRESS_KEY"
        const val  PRINTER_MODEL_KEY: String = "PRINTER_MODEL_KEY"
    }
}