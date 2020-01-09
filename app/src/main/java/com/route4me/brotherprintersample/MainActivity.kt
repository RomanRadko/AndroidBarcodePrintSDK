package com.route4me.brotherprintersample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.route4me.printer.BarPrinter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testBtn.setOnClickListener { printBarCode() }
    }

    @SuppressLint("CheckResult")
    private fun printBarCode() {
        Single.fromCallable { BarPrinter.getInstance("/storage/emulated/0/Download/test.jpg").print() }
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
