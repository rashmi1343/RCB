package com.app.rcb.ui

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.app.rcb.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    val SAMPLE_FILE = "holidays.pdf"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView.webViewClient = WebViewClient()
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        val myPdfUrl = "file:///C:/Users/rashmi.g/rcb_android/rcb_android/app/src/main/assets/holidays.pdf"
      //  val url = FileUtils.fileFromAsset(this, assets.open("holidays.pdf").toString())
       // val url = FileUtils.getPdfUrl()
        webView.loadUrl("file:///C:/Users/rashmi.g/rcb_android/rcb_android/app/src/main/assets/holidays.pdf")
       // webView.loadUrl("$url")
    }

    fun getPdfUrl(): String {
        return "https://mindorks.s3.ap-south-1.amazonaws.com/courses/MindOrks_Android_Online_Professional_Course-Syllabus.pdf"
    }
}