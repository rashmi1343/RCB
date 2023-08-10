package com.app.rcb.ui
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.app.rcb.R


class pdfactivity : AppCompatActivity() {
    // Initialize variable
    var btSelect: Button? = null
    var tvUri: TextView? = null
    var tvPath: TextView? = null
    var resultLauncher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_uploading)

        // assign variable
       btSelect = findViewById(R.id.bt_select)
        tvUri = findViewById(R.id.tv_uri)
        tvPath = findViewById(R.id.tv_path)

        // Initialize result launcher
        resultLauncher = registerForActivityResult<Intent, ActivityResult>(
            StartActivityForResult()
        ) { result ->
            // Initialize result data
            val data = result.data
            // check condition
            if (data != null) {
                // When data is not equal to empty
                // Get PDf uri
                val sUri = data.data
                // set Uri on text view
                tvUri!!.setText(
                    Html.fromHtml(
                        "<big><b>PDF Uri</b></big><br>"
                                + sUri
                    )
                )

                // Get PDF path
                val sPath = sUri!!.path
                // Set path on text view
                tvPath!!.setText(
                    Html.fromHtml(
                        (
                                "<big><b>PDF Path</b></big><br>"
                                        + sPath)
                    )
                )
            }
        }

        // Set click listener on button
        btSelect!!.setOnClickListener(
            View.OnClickListener {
                // check condition
                if ((ActivityCompat.checkSelfPermission(
                        this@pdfactivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                            != PackageManager.PERMISSION_GRANTED)
                ) {
                    // When permission is not granted
                    // Result permission
                    ActivityCompat.requestPermissions(
                        this@pdfactivity, arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        1
                    )
                } else {
                    // When permission is granted
                    // Create method
                    selectPDF()
                }
            })
    }

    private fun selectPDF() {
        // Initialize intent
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // set type
        intent.type = "application/pdf"
        // Launch intent
        resultLauncher!!.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )

        // check condition
        if (requestCode == 1 && grantResults.size > 0 && (grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            // When permission is granted
            // Call method
            selectPDF()
        } else {
            // When permission is denied
            // Display toast
            Toast
                .makeText(
                    applicationContext,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }
}
