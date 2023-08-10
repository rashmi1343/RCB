package com.app.rcb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.rcb.R
import kotlinx.android.synthetic.main.activity_holidaypdf.*

class HolidaypdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidaypdf)
        checkPdfAction(intent)
    }

    private fun checkPdfAction(intent: Intent) {
        when (intent.getStringExtra("ViewType")) {
            "assets" -> {
                showPdfFromAssets(getPdfNameFromAssets())
            }
            "storage" -> {
                // perform action to show pdf from storage
            }
            "internet" -> {
                // perform action to show pdf from the internet
            }
        }
    }
    fun getPdfNameFromAssets(): String {
        return "holidays.pdf"
    }
    private fun showPdfFromAssets(pdfName: String) {
        pdfView.fromAsset(pdfName)
            .password(null) // if password protected, then write password
            .defaultPage(0) // set the default page to open
            .onPageError { page, _ ->
                Toast.makeText(
                    this@HolidaypdfActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

}