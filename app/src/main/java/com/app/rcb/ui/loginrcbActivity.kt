package com.app.rcb.ui

import Apirequest.ApiClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.rcb.R
import com.app.rcb.response.LoginResponse
import com.app.rcb.util.Constant
import com.app.rcb.util.LoadingScreen
import com.google.gson.JsonObject
import com.premierticketbookingapp.apicall.HttpTask
import kotlinx.android.synthetic.main.activity_login_rcb.*
import kotlinx.android.synthetic.main.activity_otp_rcb.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TextView.OnEditorActionListener

class loginrcbActivity : AppCompatActivity() {

    private lateinit var mbold: Typeface
    private val TAG: String = "rcbActivity"

    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    var emailPattern =  "[a-zA-Z0-9+._%-+]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    private lateinit var mRememCheck: CheckBox

    private lateinit var objectData: JsonObject
    var isrem=false
    var islogin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_rcb)

        mbold = Typeface.createFromAsset(assets, "montserrat/Montserrat-Bold.otf")

        mRememCheck = findViewById(R.id.remem_check)
        // remember_text = findViewById(R.id.)
        mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()

        tvloginviaphone.setTypeface(mbold)

        val name: String = getColoredSpanned("Otp Will be Send,", "#808080")
        val surName: String = getColoredSpanned("on Registered Email", "#f70d1a")


        tvsignup.setText(Html.fromHtml(name + " " + surName))
        tvsignup.setTypeface(mbold)

        edtusername.setTypeface(mbold)
        btnsubmit.setTypeface(mbold)

        isrem= mPrefrences.getBoolean("isrem", false)
        islogin = mPrefrences.getBoolean("isloggedin",false)
        if (isrem && islogin) {
            val mainintent = Intent(this@loginrcbActivity, DashBoardActivity::class.java)
            mainintent.putExtra("studentid", mPrefrences.getString("studentid", ""))
            startActivity(mainintent)
        }

        else if(isrem) {
            edtusername.setText(mPrefrences.getString("username",""))
        }
        else {
            mRememCheck.isChecked = false
        }



        btnsubmit.setOnClickListener {
            if (edtusername.text.toString().isEmpty()) {
                Toast.makeText(applicationContext, "Please Enter Email Id", Toast.LENGTH_SHORT).show()
            }
            else {
                if (edtusername.text.toString().trim { it <= ' ' }.matches(emailPattern.toRegex())) {
                  //  Toast.makeText(applicationContext, "valid email address", Toast.LENGTH_SHORT).show()
                    LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

                     callotplatest(edtusername.text.toString())
                   // getotp(edtusername.text.toString())
                }
                else {
                    Toast.makeText(applicationContext, "Invalid Email Id", Toast.LENGTH_SHORT).show()
                }
            }
//
//            if (edtusername.text.length == 0) {
//                edtusername.setError("Enter Email Id")
//            } else {
//
//                LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
//
//               // callotplatest(edtmobno.text.toString())
//                getotp(edtusername.text.toString())
//            }

        }

        remem_check.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {

                if (edtusername.text.length == 0) {
                    edtusername.setError("Enter Email Id")
                    remem_check.isChecked = false
                } else {
                 //   mEditor.putString("mobileno", edtusername.text.toString())
                    mEditor.putString("username", edtusername.text.toString())
                    mEditor.putBoolean("isrem", isChecked)
                    mEditor.apply()
                    mEditor.commit()
                    mRememCheck.isChecked = true

                }
            }
            else {
                mEditor.remove("username").commit()
                mEditor.remove("isrem").commit()
            }
        }



        /*edtmobno.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (edtmobno.text.length == 0) {

                    edtmobno.setError("Enter Mobile No")
                } else {

                    LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

                    // callotplatest(edtmobno.text.toString())
                    getotp(edtmobno.text.toString())

                }

                true
            } else false
        })*/

       edtusername.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (edtusername.text.length == 0) {

                    edtusername.setError("Enter Email Id")
                } else {

                    LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

                     callotplatest(edtusername.text.toString())
                   // getotp(edtusername.text.toString())

                }

                true
            } else false
        })




        }


    private fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }




    private fun getotp(username:String) {

        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!
        val callaccesapi = ApiClient.getClient.getotp("Bearer "+Constant.accesstoken, username,"login")
        callaccesapi.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                try {
                    if (response.isSuccessful) {

                        Log.d("responsebody",response.body().toString())
                      //  Log.d("status", response.body()!!.otp)
                        Log.d("message", response.body()!!.msg)


                        if (response.body()!!.mstatus =="1") {
                            LoadingScreen.hideLoading()

                            mEditor.putString("studentid", response.body()!!.studentid)
                            mEditor.putBoolean("isloggedin", true)
                            mEditor.apply()
                            mEditor.commit()
                            val mainintent = Intent(this@loginrcbActivity, otpverifyActivity::class.java)
                           // mainintent.putExtra("otp", response.body()!!.otp)
                            mainintent.putExtra("studentid", response.body()!!.studentid)
                           // mainintent.putExtra("mobileno", mobno)
                            mainintent.putExtra("username", username)
                            startActivity(mainintent)
                        }
                        else if (response.body()!!.mstatus =="0") {
                            LoadingScreen.hideLoading()

                            Toast.makeText(this@loginrcbActivity, response.body()!!.msg, Toast.LENGTH_LONG).show()
                        }

                        else {
                            Toast.makeText(this@loginrcbActivity, "Something Went Wrong", Toast.LENGTH_LONG).show()
                            edtusername.text.clear()
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
                Toast.makeText(this@loginrcbActivity,"Something Went Wrong", Toast.LENGTH_LONG).show()
              //  edtusername.text.clear()
                //LoadingScreen.hideLoading()
            }

        })

    }


    private fun callotplatest(username: String) {

        val jsoncallotp = JSONObject()


        jsoncallotp.put("methodname", "login")
        jsoncallotp.put("student_username", username)
        Log.d("jsonevent", jsoncallotp.toString())


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
              //  var otp = eventjsonobj.getString("otp");
                var studentid = eventjsonobj.getString("studentid");


                if (status =="1") {
                    LoadingScreen.hideLoading()

                    mEditor.putString("studentid", studentid)
                    mEditor.apply()
                    mEditor.commit()
                    val mainintent = Intent(this@loginrcbActivity, otpverifyActivity::class.java)
                  //  mainintent.putExtra("otp", otp)
                    mainintent.putExtra("studentid", studentid)
                    mainintent.putExtra("username", username)
                    startActivity(mainintent)
                }
                else {
                    Toast.makeText(loginrcbActivity@ this, "Something Went Wrong", Toast.LENGTH_LONG).show()
                    LoadingScreen.hideLoading()
                    callotplatest(edtusername.text.toString())
                }




            } catch (e: JSONException) {
                println("Exception caught");
            }


        }.execute("POST", Constant.url + "student", jsoncallotp.toString().trim())

    }

}


