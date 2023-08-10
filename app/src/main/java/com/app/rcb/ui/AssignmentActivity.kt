package com.app.rcb.ui

import Apirequest.ApiClient
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.rcb.R
import com.app.rcb.adatper.assignmentadapter
import com.app.rcb.response.getassignment
import com.app.rcb.response.getdownloadassignment
import com.app.rcb.util.Constant
import com.app.rcb.util.LoadingScreen
import kotlinx.android.synthetic.main.layout_assignment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssignmentActivity : AppCompatActivity() {

    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    var loadmore = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_assignment)

        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()


        back_assignment_layout_detail.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)

        }
        getstudentassignment()
      downloadassignment()


    }

    private fun method() {
        loadmore = true
    }


    fun getstudentassignment() {

        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!
        val getstudentassignment = ApiClient.getClient.getstudentassignment(
            "Bearer " + Constant.accesstoken,
            Constant.objProfileResponse.studentid,
            "getstudentassignment",
            Constant.objProfileResponse.studentsessionid
        )

        Log.d("getstudentassignment", getstudentassignment.toString())

        getstudentassignment.enqueue(object : Callback<getassignment> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<getassignment>, response: Response<getassignment>) {

                try {
                    if (response.isSuccessful) {

                        Constant.arrasignment = response.body()!!.assignment

                        //   Log.d("studentassignmentresponse",response.toString())

                        val adapter = assignmentadapter(
                            this@AssignmentActivity,
                            Constant.arrasignment
                           // Constant.arrdownloadassignment
                        )

                        rrviewassignment.layoutManager =
                            LinearLayoutManager(this@AssignmentActivity)
                        // Setting the Adapter with the recyclerview
                        rrviewassignment.adapter = adapter
                        LoadingScreen.hideLoading()
                    }
                } catch (e: Exception) {
                    Log.d("exception", e.toString())
                    Toast.makeText(
                        this@AssignmentActivity,
                        "Something Went Wrong, Pleasse try Again",
                        Toast.LENGTH_LONG
                    ).show()

                    LoadingScreen.hideLoading()
                }

            }

            override fun onFailure(call: Call<getassignment>, t: Throwable) {


                Log.d("exception", t.message.toString())
                Toast.makeText(
                    this@AssignmentActivity,
                    "Something Went Wrong, Pleasse try Again",
                    Toast.LENGTH_LONG
                ).show()

                LoadingScreen.hideLoading()
            }

        })


    }


     fun downloadassignment() {
           Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

           val downloadassignment = ApiClient.getClient.getdownloadassignment(
               "Bearer " + Constant.accesstoken,
               "downloadassignment",Constant.objProfileResponse.studentid,Constant.objProfileResponse.studentsessionid.toString()
           )

           downloadassignment.enqueue(object : Callback<getdownloadassignment> {
               override fun onResponse(
                   call: Call<getdownloadassignment>,
                   response: Response<getdownloadassignment>
               ) {
                   try {
                       if (response.isSuccessful) {

                           Constant.arrdownloadassignment = response.body()!!.assignment



//                           val adapter = assignmentadapter(
//                               this@AssignmentActivity,
//                               Constant.arrasignment,
//                               Constant.arrdownloadassignment
//                           )
//
//                           rrviewassignment.layoutManager =
//                               LinearLayoutManager(this@AssignmentActivity)
//                           // Setting the Adapter with the recyclerview
//                           rrviewassignment.adapter = adapter
//                           LoadingScreen.hideLoading()
                       }
                   } catch (e: Exception) {
                       Log.d("exception", e.toString())
                       Toast.makeText(
                           this@AssignmentActivity,
                           "Something Went Wrong, Pleasse try Again",
                           Toast.LENGTH_LONG
                       ).show()

                       LoadingScreen.hideLoading()
                   }
               }

               override fun onFailure(call: Call<getdownloadassignment>, t: Throwable) {

                   Log.d("exception", t.message.toString())
                   Toast.makeText(
                       this@AssignmentActivity,
                       "Something Went Wrong, Pleasse try Again",
                       Toast.LENGTH_LONG
                   ).show()

                   LoadingScreen.hideLoading()
               }

           })

       }

}