package com.route4me

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.route4me.printer.*
import com.route4me.printer.dialog.ChooseBTDeviceDialog
import com.route4me.printer.model.PrinterManufacturer
import com.route4me.printer.model.prefs.Preferences
import com.route4me.printer.model.prefs.PrintPreferences
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.print_fragment.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KProperty


class PrintFragment : Fragment() {

    companion object {
        const val BARCODE_KEY: String = "BARCODE_KEY"

        fun newInstance(): PrintFragment {
            return PrintFragment()
        }
    }

    private var barcode: String? = ""
    private var macChangedListener = object : Preferences.SharedPrefsListener {
        override fun onSharedPrefChanged(property: KProperty<*>) {
            if (PrintPreferences::manufacturer.name == property.name) {
                connectBtn.visibility = View.GONE
                printBtn.visibility = View.VISIBLE
                connectivityStatusView.visibility = View.VISIBLE
                connectivityStatusView.bindData { startConnectivityFlow() }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PrintPreferences.getInstance(context!!).addListener(macChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        PrintPreferences.getInstance(context!!).removeListener(macChangedListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.print_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barcode = arguments?.getString(BARCODE_KEY)
        initViews()
        checkForPermission()
    }

    @SuppressLint("CheckResult")
    private fun print() {
        progressBar.visibility = View.VISIBLE
        container.alpha = 0.6f
        val printer = when (PrintPreferences.getInstance(context!!).manufacturer) {
            PrinterManufacturer.BROTHER.manufacturerName -> BrotherPrinter.getInstance(context!!)
            PrinterManufacturer.ZEBRA.manufacturerName -> ZebraPrinter.getInstance()
            PrinterManufacturer.CITIZEN.manufacturerName -> CitizenPrinter.getInstance()
            else -> TSCPrinter.getInstance()
        }
        Single.fromCallable {
                printer.print(barcode!!, PrintPreferences.getInstance(context!!).macAddress!!)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .timeout(10, TimeUnit.SECONDS)
            .subscribe(
                {
//                    if (printerType == PrinterType.BROTHER) logOutput.text =
//                        (printer as BrotherPrinter).readLog()
                    progressBar.visibility = View.GONE
                    container.alpha = 1f
                    showToast("Print status : $it")
                },
                {
//                    if (printerType == PrinterType.BROTHER) logOutput.text =
//                        (printer as BrotherPrinter).readLog()
                    activity!!.runOnUiThread {
                        // Stuff that updates the UI
                        progressBar.visibility = View.GONE
                        container.alpha = 1f
                        showToast("Failed to print.$it")
                    }
                })
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkForPermission() {
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (!hasPermissions(
                context!!,
                permissions = *PERMISSIONS
            )
        ) {
            ActivityCompat.requestPermissions(activity!!, PERMISSIONS, PERMISSION_ALL)
        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun startConnectivityFlow() {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        ChooseBTDeviceDialog()
            .show(transaction, ChooseBTDeviceDialog.TAG)
    }

    private fun initViews() {
        barcodeView.bindData(barcode!!)
        connectBtn.setOnClickListener {
            startConnectivityFlow()
        }
        printBtn.setOnClickListener {
            print()
        }
        if (!PrintPreferences.getInstance(context!!).macAddress.isNullOrEmpty() &&
            !PrintPreferences.getInstance(context!!).deviceName.isNullOrEmpty() &&
            !PrintPreferences.getInstance(context!!).manufacturer.isNullOrEmpty()) {
            connectBtn.visibility = View.GONE
            printBtn.visibility = View.VISIBLE
            connectivityStatusView.visibility = View.VISIBLE
            connectivityStatusView.bindData { startConnectivityFlow() }
        }
    }

    private fun showToast(message: String) {
        val layout: View = layoutInflater.inflate(
            R.layout.toast_layout,
            view?.findViewById(R.id.toastContainer) as ViewGroup?
        )
        (layout.findViewById(R.id.toastContainer) as TextView).text = message
        Toast(context).apply {
            setGravity(Gravity.BOTTOM, 0, 100)
            duration = Toast.LENGTH_LONG
            view = layout
        }.show()
    }

}