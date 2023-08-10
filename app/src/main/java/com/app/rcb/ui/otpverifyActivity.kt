package com.app.rcb.ui

import Apirequest.ApiClient
import android.R.attr
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.app.rcb.response.LoginResponse
import com.app.rcb.response.VerifyotpResponse
import com.app.rcb.util.Constant
import com.premierticketbookingapp.apicall.HttpTask
import kotlinx.android.synthetic.main.activity_otp_rcb.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.app.rcb.R
import android.widget.TextView

import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import com.app.rcb.util.LoadingScreen
import kotlinx.android.synthetic.main.activity_login_rcb.*


class otpverifyActivity : AppCompatActivity() {

    private lateinit var mbold: Typeface
    private lateinit var mlight: Typeface
    var strstudentid: String? = null
    var strotp:String? = null

    var username:String?= null

    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_rcb)

        mbold = Typeface.createFromAsset(assets,"montserrat/Montserrat-Bold.otf")

        mlight = Typeface.createFromAsset(assets,"montserrat/Montserrat-Light.otf")

        tvverifycode.setTypeface(mbold)

        otp_edit_box1.setTypeface(mbold)

        otp_edit_box2.setTypeface(mbold)

        otp_edit_box3.setTypeface(mbold)

        otp_edit_box4.setTypeface(mbold)

        btnverify.setTypeface(mbold)

        tvsendcodeagain.setTypeface(mlight)

        tvchangeemail.setTypeface(mlight)

        mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()

        tvchangeemail.setOnClickListener {

            val mainIntent = Intent(this@otpverifyActivity, loginrcbActivity::class.java)
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(mainIntent)
            finish()
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

        }


        tvsendcodeagain.setOnClickListener {

          if(username!!.length>0) {
              getotp(username.toString())
          }

        }


     //   var strotp: String? = intent.getStringExtra("otp")
        strstudentid= intent.getStringExtra("studentid")

        username = intent.getStringExtra("username")

      /*  val substringone = strotp!!.subSequence(0, 1)
        otp_edit_box1.setText(substringone)

        val substringtwo = strotp!!.subSequence(1,2)
        otp_edit_box2.setText(substringtwo)

        val substringthree = strotp!!.subSequence(2,3)
        otp_edit_box3.setText(substringthree)

        val substringfour = strotp!!.subSequence(3,4)
        otp_edit_box4.setText(substringfour)*/

        otp_edit_box1.addTextChangedListener(textWatcher)

        otp_edit_box2.addTextChangedListener(textWatcher)

        otp_edit_box3.addTextChangedListener(textWatcher)

        otp_edit_box4.addTextChangedListener(textWatcher)



      /*  otp_edit_box1.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.ACTION_DOWN) {
                //Perform Code
                otp_edit_box1.setText("")
                otp_edit_box1.requestFocus()
                return@OnKeyListener true
            }
            false
        })


        otp_edit_box2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_CLEAR) {
                //Perform Code
                otp_edit_box2.setText("")
                otp_edit_box2.requestFocus()
                return@OnKeyListener true
            }
            false
        })

        otp_edit_box3.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_CLEAR) {
                //Perform Code
                otp_edit_box3.setText("")
                otp_edit_box3.requestFocus()
                return@OnKeyListener true
            }
            false
        })


        otp_edit_box4.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_CLEAR) {
                //Perform Code
                otp_edit_box4.setText("")
                otp_edit_box4.requestFocus()
                return@OnKeyListener true
            }
            false
        })*/





        otp_edit_box4.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (otp_edit_box1.text.length == 0 || otp_edit_box2.text.length == 0 || otp_edit_box3.text.length == 0 || otp_edit_box4.text.length == 0) {

                    Toast.makeText(otpverifyActivity@ this, "Enter Otp", Toast.LENGTH_LONG)
                        .show()
                } else {

                    val strotp: String =
                        otp_edit_box1.getText().toString() + otp_edit_box2.getText()
                            .toString() + otp_edit_box3.getText().toString() +
                                otp_edit_box4.getText().toString()

                    Log.d("strotp", strotp.toString())

                    //verifyotplatest(strotp, strstudentid)

                    verifyotp(strotp, strstudentid)

                }

                true
            } else false
        })



        btnverify.setOnClickListener {

           /* val mainIntent = Intent(this@otpverifyActivity, DashBoardActivity::class.java)
            startActivity(mainIntent )
            finish()
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left) */

            if(otp_edit_box1.text.length==0 || otp_edit_box2.text.length==0 || otp_edit_box3.text.length==0 || otp_edit_box4.text.length==0) {
                Toast.makeText(otpverifyActivity@this, "Enter Otp", Toast.LENGTH_LONG).show()
            }else {

                val strotp: String =
                    otp_edit_box1.getText().toString() +  otp_edit_box2.getText()
                        .toString() +  otp_edit_box3.getText().toString() +
                            otp_edit_box4.getText().toString()

                Log.d("strotp", strotp.toString())

                //verifyotplatest(strotp, strstudentid)

                verifyotp(strotp, strstudentid)
            }

        }







    }



    val textWatcher = object : TextWatcher {


        override fun afterTextChanged(s: Editable?) {

            if (s == otp_edit_box1.editableText) {
                if(otp_edit_box1.length()==1) {
                    otp_edit_box2.requestFocus()
                }

            }
            else if(s==otp_edit_box2.editableText) {

                if(otp_edit_box2.length()==1) {
                    otp_edit_box3.requestFocus()
                }
                else if (otp_edit_box2.length()==0) {
                    otp_edit_box1.requestFocus()
                }
            }

            else if(s==otp_edit_box3.editableText) {
                if(otp_edit_box3.length()==1) {
                    otp_edit_box4.requestFocus()
                }
                else if (otp_edit_box3.length()==0) {
                    otp_edit_box2.requestFocus()
                }

            }

            else if (s==otp_edit_box4.editableText) {

                if (otp_edit_box4.length()==0) {
                    otp_edit_box3.requestFocus()
                }
            }


        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {



        }
    }





      fun  onKey(v: View, keyCode: Int, event: KeyEvent?): Boolean {
        when (v.getId()) {
          R.id.otp_edit_box1 -> {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_edit_box1.setText("")
                    otp_edit_box1.requestFocus()
                }
            }
            R.id.otp_edit_box2 -> {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_edit_box2.setText("")
                    otp_edit_box2.requestFocus()
                }
            }
            R.id.otp_edit_box3 -> {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_edit_box3.setText("")
                    otp_edit_box3.requestFocus()
                }
            }
            R.id.otp_edit_box4 -> {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    otp_edit_box4.setText("")
                    otp_edit_box4.requestFocus()
                }
            }

        }
        return false
    }


    private fun verifyotplatest(strotp:String? , stdid:String?){


        val jsonotpverify = JSONObject()


        jsonotpverify.put("methodname", "verifyotp")
        jsonotpverify.put("otpentered", strotp)

        Log.d("jsonevent", jsonotpverify.toString())


        HttpTask {
            ""
            if (it == null) {
                Log.d("connection error", "Some thing Went Wrong")

                return@HttpTask
            }
            try {


                var eventjsonobj = JSONObject(it.toString())
                Log.d("respond", eventjsonobj.toString())

                var status = eventjsonobj.getString("status");
                var message = eventjsonobj.getString("message");
//                var otp = eventjsonobj.getString("otp");
             //   var studentid = eventjsonobj.getString("studentid");

                if (status == "1") {

                    Toast.makeText(loginrcbActivity@this, message, Toast.LENGTH_LONG).show()

                    val mainintent = Intent(this@otpverifyActivity, DashBoardActivity::class.java)
                    mainintent.putExtra("studentid", strstudentid)
                    startActivity(mainintent)
                }
                else {
                    Toast.makeText(loginrcbActivity@this, message, Toast.LENGTH_LONG).show()

                }


            } catch (e: JSONException) {
               // println("Exception caught");
                Log.d("Exception", e.toString())
            }


        }.execute("POST", Constant.url + "student", jsonotpverify.toString().trim())

    }

    private fun verifyotp(strotp:String?, stdid:String?) {

        val callotpverifyapi = ApiClient.getClient.getverifyotp("Bearer "+Constant.accesstoken,"verifyotp",stdid, strotp)

        callotpverifyapi.enqueue(object : Callback<VerifyotpResponse> {
            override fun onFailure(call: Call<VerifyotpResponse>, t: Throwable) {

                Log.d("VerifyError", t.toString())


            }

            override fun onResponse(
                call: Call<VerifyotpResponse>,
                response: Response<VerifyotpResponse>
            ) {
                try {
                    if (response.isSuccessful) {
                        Log.d("responsebody", response.body().toString())
                        Log.d("status", response.body()!!.msg)
                        Log.d("msg", response.body()!!.mstatus)

                            if(response.body()!!.mstatus=="1") {
                                Toast.makeText(
                                    this@otpverifyActivity,
                                    response.body()!!.msg,
                                    Toast.LENGTH_LONG
                                ).show()
                                val mainIntent =
                                    Intent(this@otpverifyActivity, DashBoardActivity::class.java)
                                mainIntent.putExtra("studentid", stdid)
                                startActivity(mainIntent)
                                finish()
                                overridePendingTransition(
                                    R.anim.slidein_right,
                                    R.anim.slide_out_left
                                )
                            }
                        else {
                                Toast.makeText(
                                    this@otpverifyActivity,
                                    response.body()!!.msg,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("ThrowingData", e.toString())
                }
            }

        });


    }



    private fun getotp(mobno:String) {

        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!
        val callaccesapi = ApiClient.getClient.getotp("Bearer "+Constant.accesstoken, mobno,"login")
        callaccesapi.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                try {
                    if (response.isSuccessful) {

                        Log.d("responsebody",response.body().toString())
                        //  Log.d("status", response.body()!!.otp)
                        Log.d("message", response.body()!!.msg)


                        if (response.body()!!.mstatus =="1") {
                            LoadingScreen.hideLoading()

                            Toast.makeText(this@otpverifyActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                        }
                        else if (response.body()!!.mstatus =="0") {
                            LoadingScreen.hideLoading()

                            Toast.makeText(this@otpverifyActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                        }

                        else {
                            Toast.makeText(this@otpverifyActivity, "Something Went Wrong", Toast.LENGTH_LONG).show()

                            LoadingScreen.hideLoading()
                            // getotp(edtmobno.text.toString())
                        }
                    }

                }catch(e:Exception) {
                    Log.d("Exception",e.toString())

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                Log.d("LoginError", t.toString())
                Toast.makeText(this@otpverifyActivity, "Something Went Wrong", Toast.LENGTH_LONG).show()

            }

        })

    }




}