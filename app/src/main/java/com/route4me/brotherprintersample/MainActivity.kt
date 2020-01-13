package com.route4me.brotherprintersample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.route4me.printer.BrotherPrinter
import com.route4me.printer.ZebraPrinter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testBrotherBtn.setOnClickListener { printBrother() }
        testZebraBtn.setOnClickListener { printZebra() }
        connectDeviceBtn.setOnClickListener { startActivity(Intent(this, SearchBTPrinterActivity::class.java)) }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        val selectedPrinterName = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(SearchBTPrinterActivity.PRINTER_MODEL_KEY, "")
        if (selectedPrinterName!!.isEmpty()) {
            btDeviceName.text = selectedPrinterName
        }
    }

    @SuppressLint("CheckResult")
    private fun printZebra() {
        Single.fromCallable { ZebraPrinter.getInstance("91234567891").print() }
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
        Single.fromCallable { BrotherPrinter.getInstance("/storage/emulated/0/Download/test.jpg").print() }
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
}
