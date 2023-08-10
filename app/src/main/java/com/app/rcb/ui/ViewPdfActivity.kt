package com.app.rcb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.app.rcb.R
import com.app.rcb.util.Constant
import kotlinx.android.synthetic.main.viewpdf.*
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.util.FileUtil
import android.view.View
import android.util.Log
import android.widget.LinearLayout
import android.content.Intent
import android.widget.Toast


import es.voghdev.pdfviewpager.library.remote.DownloadFile
import kotlinx.android.synthetic.main.viewpdfnew.*


class ViewPdfActivity : AppCompatActivity(), DownloadFile.Listener {

    private lateinit var docfilename:String
    private var remotePDFViewPager: RemotePDFViewPager? = null
    private var pdfPagerAdapter: PDFPagerAdapter? = null
    private var pdfLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.viewpdfnew)

        docfilename = intent.getStringExtra("docname").toString()

        Log.d("docfilename", docfilename)

        pdfLayout = findViewById(R.id.pdf_layout)


        //  var strpath = "https://rcb.broadwayinfotech.net.au/beta/storage/uploads/users/"+ docfilename
      //  pdfView.fromUri(docfilename.toUri())
      //  pdfView.loadUrl("https://docs.google.com/viewer?url="+docfilename)

       // if (Shared.isNetworkAvailable(MsgfromCmActivity@this)) {
            remotePDFViewPager = RemotePDFViewPager(this, docfilename, this)
      //  }
      //  else {
        /*    Toast.makeText(
                MsgfromCmActivity@ this,
                "Internet Connectivity Not Available",
                Toast.LENGTH_LONG
            ).show()*/
          //  progressBar!!.setVisibility(View.GONE)
        }


    override fun onBackPressed() {
       // finish()
//        val mainIntent = Intent(this@ViewPdfActivity, BankdetailActivity::class.java)
//        startActivity(mainIntent)
        finish()

    }



    override fun onSuccess(url: String?, destinationPath: String?) {
        pdfPagerAdapter = PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url))
        remotePDFViewPager!!.adapter = pdfPagerAdapter
        updateLayout()
        progress_small1!!.visibility = View.GONE

    }

    override fun onFailure(e: Exception?) {

        Toast.makeText(
            ViewPdfActivity@ this,
            "File not available!!",
            Toast.LENGTH_LONG
        ).show()
        finish()

    }

    override fun onProgressUpdate(progress: Int, total: Int) {
    }

    private fun updateLayout() {
        pdfLayout!!.addView(
            remotePDFViewPager,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (pdfPagerAdapter != null) {
            pdfPagerAdapter!!.close()
        }
    }

    }

