package com.route4me.brotherprintersample

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
    }

    @SuppressLint("CheckResult")
    private fun printZebra() {
        if (selectedPrinterMacAddress.isNullOrBlank()) {
            Toast.makeText(this, "No BT Printers are selected.", Toast.LENGTH_LONG)
                .show()
            return
        }
        Single.fromCallable {
            ZebraPrinter.getInstance(barcodeCode.text.toString()).print(selectedPrinterMacAddress!!)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Toast.makeText(
                        this,
                        "Print success, status : $it",
                        Toast.LENGTH_LONG
                    )
                        .show()
                },
                {
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
        Single.fromCallable {
            BrotherPrinter.getInstance(barcodeCode.text.toString())
                .print(selectedPrinterMacAddress!!)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Toast.makeText(
                        this,
                        "Print was successful : $it",
                        Toast.LENGTH_LONG
                    )
                        .show()
                },
                {
                    Toast.makeText(this, "Failed to print.$it", Toast.LENGTH_LONG)
                        .show()
                })
    }

}
