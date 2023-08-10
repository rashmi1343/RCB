package com.app.rcb.ui

import Apirequest.ApiClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.app.rcb.R
import com.app.rcb.response.*
import com.app.rcb.util.Constant
import com.app.rcb.util.FileUtil
import com.app.rcb.viewmodel.rcbviewModel
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.premierticketbookingapp.apicall.HttpTask
import kotlinx.android.synthetic.main.rcb_splash.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class SplashActivity : AppCompatActivity()
{
    private lateinit var mPrefrence:SharedPreferences
    private val SPLASH_DELAY: Long = 3000
    private lateinit var mAnimation:Animation
    private lateinit var mMedium: Typeface
    private lateinit var mAppName:TextView
    private lateinit var mAppImage:ImageView
    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    private var splashprg: KProgressHUD? = null


    var appfirsttime :Int =0
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rcb_splash)

        mMedium = Typeface.createFromAsset(assets,"montserrat/Montserrat-Medium.otf")

       mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()

        //val db = AppDatabase(this)
        splashprg = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Loading..")
            .setDetailsLabel("RCB")
            .show();

        Constant.accesstoken = mPrefrences.getString("accesstoken","").toString()
      //  if(Constant.accesstoken=="null") {
         appfirsttime  = FileUtil.appGetFirstTimeRun(this@SplashActivity)
        if(appfirsttime==0) {


           // callaccessApi()
            callaccessapibyhttptask()
           // fireAndForgetNetworkCall()
        }
        else {
            Log.d("store_Accesstoken",Constant.accesstoken)

            splashprg!!.dismiss()

             val mainIntent = Intent(this@SplashActivity, loginrcbActivity::class.java)
              startActivity(mainIntent)
              finish()
              overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)


        }






    }










     fun callaccessApi() {

        try {
           // val ja = JSONObject()

            // old beta server
        //   val callaccesapi = ApiClient.getClient.getaccesstoken("vivek.chandra@broadwayinfotech.com", "123456")
          // new beta server
            val callaccesapi = ApiClient.getClient.getaccesstoken("naveen.kumar@rcb.res.in", "bwit@123!")

            callaccesapi.enqueue(object : Callback<RequestAccess> {

                override fun onFailure(call: Call<RequestAccess>, t: Throwable) {

                    Log.d("LoginError", t.toString())
                    Toast.makeText(this@SplashActivity, "Something Went Wrong", Toast.LENGTH_LONG).show()

                }

                override fun onResponse(call: Call<RequestAccess>, response: Response<RequestAccess>) {

                    try {
                        if (response.isSuccessful) {

                            Log.d("responsebody",response.body().toString())
                            Log.d("status", response.body()!!.status);
                            Log.d("message", response.body()!!.message);
                            Log.d("token", response.body()!!.access_token);

                            Constant.accesstoken = response.body()!!.access_token
                            mEditor.putString("accesstoken", Constant.accesstoken)
                            mEditor.apply()
                            mEditor.commit()

                            splashprg!!.dismiss()

                           // btnlogin.setBackgroundResource(R.color.mapboxRed);
                          //  btnlogin.setTextColor(getResources().getColor(R.color.mapboxWhite));
                           // Handler().postDelayed({


                          //  }, SPLASH_DELAY)

                        }
                    }catch(e:Exception) {
                        Log.d("Exception",e.toString())
                        splashprg!!.dismiss()

                    }
                }

            });
        }
        catch(e:Exception) {
            Log.d("Exception",e.toString())
            splashprg!!.dismiss()

        }

    }



    fun callaccessapibyhttptask() {

        val reqparam = JSONObject()
        reqparam.put("email", "naveen.kumar@rcb.res.in")
        reqparam.put("password", "bwit@123!")

        Log.d("Request_param",reqparam.toString())
        HttpTask { ""
            if (it == null) {
                Log.d("connection error", "Some thing Went Wrong")


                return@HttpTask
            }
            try {


                var requestparam = JSONObject(it.toString())

                var status = requestparam.getString("status")
                var message = requestparam.getString("message")
                var access_token = requestparam.getString("access_token")


                //Constant.accesstoken = response.body()!!.access_token
                mEditor.putString("accesstoken", access_token)
                mEditor.apply()
                mEditor.commit()

                if(status=="1") {

                    splashprg!!.dismiss()
                    Handler().postDelayed({
                        val mainIntent = Intent(this@SplashActivity, loginrcbActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                        overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                    }, SPLASH_DELAY)
                }

            } catch(e: JSONException)
            {
              Log.d("exception",e.toString())
            }



        }.execute("POST", Constant.url+ "login", reqparam.toString())

    }

























}
