package com.route4me.brotherprintersample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.route4me.printer.BrotherPrinter
import com.route4me.printer.ZebraPrinter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selectedPrinterName: String? = ""

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
        generateBarcode(barcodeCode.text.toString())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        selectedPrinterName = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(SearchBTPrinterActivity.MAC_ADDRESS_KEY, "")
        btDeviceName.text = selectedPrinterName
    }

    @SuppressLint("CheckResult")
    private fun printZebra() {
        Single.fromCallable {
            ZebraPrinter.getInstance(barcodeCode.text.toString()).print(selectedPrinterName)
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
        Single.fromCallable {
            BrotherPrinter.getInstance("/storage/emulated/0/Download/test.jpg")
                .print(selectedPrinterName)
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

    private fun generateBarcode(input: String) {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode(input, BarcodeFormat.CODABAR, 800, 200)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            barcodeView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
