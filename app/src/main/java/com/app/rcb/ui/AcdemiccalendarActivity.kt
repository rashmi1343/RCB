package com.app.rcb.ui

import Apirequest.ApiClient
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.rcb.R
import com.app.rcb.adatper.calendaradapter
import com.app.rcb.response.getstudentcalendar
import com.app.rcb.util.Constant
import com.app.rcb.util.LoadingScreen
import kotlinx.android.synthetic.main.layout_acadamic_calendar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class AcdemiccalendarActivity : AppCompatActivity() {

    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_acadamic_calendar)
        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()


        back_past_layout_detail.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
        }

        getstudentacademiccalendar()


    }


    fun getstudentacademiccalendar() {

        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!
        val callstudentcalendarapi = ApiClient.getClient.getstudentacademiccalendar(
            "Bearer " + Constant.accesstoken,
            "getstudentcalendar",
            Constant.objProfileResponse.studentid,
            0,
            Constant.objProfileResponse.stdinstituteid,
            Constant.objProfileResponse.stdprgname,
            Constant.objProfileResponse.stdassyear,
            Constant.objProfileResponse.studentsessionid.toString()
        )


        callstudentcalendarapi.enqueue(object : Callback<getstudentcalendar> {
            override fun onResponse(
                call: Call<getstudentcalendar>,
                response: Response<getstudentcalendar>
            ) {

                try {
                    if (response.isSuccessful()) {

                        Log.d("acd_calendar", response.body()!!.ac.toString())
                        Constant.arracdemiccalendar = response.body()!!.ac

                        val adapter = calendaradapter(Constant.arracdemiccalendar)

                        rrview.layoutManager = LinearLayoutManager(this@AcdemiccalendarActivity)
                        // Setting the Adapter with the recyclerview
                        rrview.adapter = adapter
                        LoadingScreen.hideLoading()
                    }

                } catch (e: Exception) {
                    Log.d("Exception", e.toString())
                    LoadingScreen.hideLoading()
                    Toast.makeText(
                        this@AcdemiccalendarActivity,
                        "Something Went Wrong, Pleasse try Again",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

            override fun onFailure(call: Call<getstudentcalendar>, t: Throwable) {

                Log.d("failure", t.message.toString())
                LoadingScreen.hideLoading()
                Toast.makeText(
                    this@AcdemiccalendarActivity,
                    "Something Went Wrong, Pleasse try Again",
                    Toast.LENGTH_LONG
                ).show()

            }

        })


    }
}




