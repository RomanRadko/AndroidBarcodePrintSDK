package com.route4me.brotherprintersample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.route4me.printer.BrotherPrinter
import com.route4me.printer.ZebraPrinter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


const val PREFS = "PREFS"

class MainActivity : AppCompatActivity() {

    private val selectedPrinterMacAddress: String?
        get() = applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(SearchBTPrinterActivity.MAC_ADDRESS_KEY, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testBrotherBtn.setOnClickListener { printBrother() }
        testZebraBtn.setOnClickListener { printZebra() }
        connectDeviceBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchBTPrinterActivity::class.java
                )
            )
        }
        btDeviceName.text = selectedPrinterMacAddress
        logOutput.movementMethod = ScrollingMovementMethod()
        clearLogBtn.setOnClickListener { logOutput.text = "" }
    }

    @SuppressLint("CheckResult")
    private fun printZebra() {
        if (selectedPrinterMacAddress.isNullOrBlank()) {
            Toast.makeText(this, "No BT Printers are selected.", Toast.LENGTH_LONG)
                .show()
            return
        }
        progressBar.visibility = View.VISIBLE
        Single.fromCallable {
            ZebraPrinter.getInstance(barcodeCode.text.toString()).print(selectedPrinterMacAddress!!)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Print success, status : $it",
                        Toast.LENGTH_LONG
                    )
                        .show()
                },
                {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to print.$it", Toast.LENGTH_LONG)
                        .show()
                })
    }

    @SuppressLint("CheckResult")
    private fun printBrother() {
        if (selectedPrinterMacAddress.isNullOrBlank()) {
            Toast.makeText(this, "No BT Printers are selected.", Toast.LENGTH_LONG)
                .show()
            return
        }
        val printer = BrotherPrinter.getInstance(barcodeCode.text.toString())
        progressBar.visibility = View.VISIBLE
        Single.fromCallable {
            printer.print(this, selectedPrinterMacAddress!!)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    logOutput.text = printer.readLog()
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Print was successful : $it",
                        Toast.LENGTH_LONG
                    )
                        .show()
                },
                {
                    logOutput.text = printer.readLog()
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to print.$it", Toast.LENGTH_LONG)
                        .show()
                })
    }

}
