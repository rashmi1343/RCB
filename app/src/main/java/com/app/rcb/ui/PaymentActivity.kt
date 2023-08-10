package com.app.rcb.ui

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.app.rcb.R
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class PaymentActivity : AppCompatActivity() {

    private var mHash: String? = null
    private var  merchantId = "UATRCBFSG0000001203";
    private var  serviceId = "RCBAcadFee";

    private var  secretKey = "3f6f31a59a89d0bc33688bca576c2116fdd344fd7f46d5884321593c6f53c0aa";
    //private var  requestDateTime = $date;
    private var messageType = "0100";
    private var  currencyCode = "INR";
    private var mAmount = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   setContentView(R.layout.layout_bank_detail)
        val html = "<html>" +
                "<body onload='document.frm1.submit()'>" +
                "<form action='https://pilot.surepay.ndml.in/SurePayPayment/sp/processRequest' method='post' name='frm1'>" +
                "  <input type='hidden' id='checksum' name='checksum' value='5645644543'><br>" +
                "  <input type='hidden' name='merchantId' value='NDML_NSR'><br>" +
                "  <input type='hidden' name='serviceId' value='NSRREG'><br>" +
                "  <input type='hidden' name='orderId' value='23890'><br>" +
                "  <input type='hidden' name='customerId' value='C345434643'><br>" +
                "  <input type='hidden' name='transactionAmount' value='1'><br>"+
                "  <input type='hidden' name='currencyCode' value='INR'><br>"+
                "  <input type='hidden' name='requestDateTime' value='15-12-2021;15:01:26'><br>"+
                "  <input type='hidden' name='successUrl' value='https://epaytest.rcb.ac.in/paygovindia/success/'><br>"+
                "  <input type='hidden' name='failUrl' value='https://epaytest.rcb.ac.in/paygovindia/fail/'><br>"+
                "</form>" +
                "</body>" +
                "</html>"

        val webview = WebView(this)
        setContentView(webview)

        webview.loadData(html, "text/html", "UTF-8")
        /*mHash = hashCal("SHA-512", merchantId + "|" +
                serviceId + "|" +
                mAmount + "|" +
                mProductInfo + "|" +
                mFirstName + "|" +
                mEmailId + "|||||||||||" +
                mSalt);
        val url = "'https://pilot.surepay.ndml.in/SurePayPayment/sp/processRequest"

        val postData = "username=${URLEncoder.encode(my_username, "UTF-8")}" +
                "&password=${URLEncoder.encode(my_password, "UTF-8")}"
        webview.postUrl(url, postData.toByteArray())*/


    }





    fun hashCal(type: String?, str: String): String? {
        val hashSequence = str.toByteArray()
        val hexString = StringBuffer()
        try {
            val algorithm: MessageDigest = MessageDigest.getInstance(type)
            algorithm.reset()
            algorithm.update(hashSequence)
            val messageDigest: ByteArray = algorithm.digest()
            for (i in messageDigest.indices) {
                val hex = Integer.toHexString(0xFF and messageDigest[i].toInt())
                if (hex.length == 1) hexString.append("0")
                hexString.append(hex)
            }
        } catch (NSAE: NoSuchAlgorithmException) {
        }
        return hexString.toString()
    }
}