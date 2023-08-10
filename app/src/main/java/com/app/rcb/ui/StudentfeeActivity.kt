package com.app.rcb.ui

import Apirequest.ApiClient
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.rcb.R
import com.app.rcb.response.getfeedetail
import com.app.rcb.util.Constant
import com.app.rcb.util.LoadingScreen
import kotlinx.android.synthetic.main.layout_student_fee.*
import kotlinx.android.synthetic.main.row_student_fee.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class StudentfeeActivity : AppCompatActivity() {

    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    private lateinit var mRegular: Typeface
    private lateinit var mbold: Typeface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.layout_student_fee)

        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)
        mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()
        mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
        mbold = Typeface.createFromAsset(assets, "montserrat/Montserrat-Bold.otf")

        back_fee_layout_detail.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
        }

        getstudentfee()

    }


    private fun getstudentfee() {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!
        val getstudentfee = ApiClient.getClient.getstudentfees(
            "Bearer " + Constant.accesstoken,
            Constant.objProfileResponse.studentid,
            "getstudentfeedetails"
        )


        getstudentfee.enqueue(object : Callback<getfeedetail> {
            override fun onResponse(call: Call<getfeedetail>, response: Response<getfeedetail>) {

                try {
                    if (response.isSuccessful()) {
                        Log.d("response", response.body()!!.Student_fee.toString())

                        Constant.arrfee = response.body()!!.Student_fee



                        createlayoutforfee()
                        LoadingScreen.hideLoading()
                    }
                } catch (e: Exception) {
                    Log.d("exception", e.toString())
                    Toast.makeText(
                        this@StudentfeeActivity,
                        "Something Went Wrong, Pleasse try Again",
                        Toast.LENGTH_LONG
                    ).show()
                    LoadingScreen.hideLoading()
                }

            }

            override fun onFailure(call: Call<getfeedetail>, t: Throwable) {
                Toast.makeText(
                    this@StudentfeeActivity,
                    "Something Went Wrong, Pleasse try Again",
                    Toast.LENGTH_LONG
                ).show()

            }

        });

    }


    private fun createlayoutforfee() {

        try {
            val lllayoutfee = findViewById<LinearLayout>(R.id.lllayoutfeelist)

            val layoutInflater: LayoutInflater = LayoutInflater.from(StudentfeeActivity@ this)

            var tvsessionyear: TextView
            var tvsessionyearval: TextView
            var tvsemester: TextView
            var tvsemesterval: TextView
            var tvschemename: TextView
            var tvschemenameval: TextView
            var tvschemeprintname: TextView
            var tvschemeprintnameval: TextView
            var tvfeename: TextView
            var tvfeenameval: TextView
            var tvfeeprintname: TextView
            var tvfeeprintnameval: TextView
            var tvfeecode: TextView
            var tvfeecodeval: TextView
            var tvcategory: TextView
            var tvcategoryval: TextView

            var tvfeeamt: TextView
            var tvfeeamtval: TextView
            var tvdiscount: TextView
            var tvdiscountval: TextView

            var tvduedate: TextView
            var tvduedateval: TextView
            var tvtotalpaid: TextView
            var tvtotalpaidval: TextView
            var tvrefund: TextView
            var tvrefundval: TextView
            var tvbalamt: TextView
            var tvbalamtval: TextView
            var tvpaymentstatus: TextView
            var tvpaymentstatusval: TextView
            var tvnetpayment: TextView
            var tvnetpaymentval: TextView


            //for (i in 0..Constant.arrfee.size - 1) {

            val vwrowstudentfee: ViewGroup =
                layoutInflater.inflate(R.layout.row_student_fee, null, false) as ViewGroup

            tvsessionyear = vwrowstudentfee.findViewById(R.id.tvsessionyear)

            tvsessionyear.setTypeface(mbold)



            tvsessionyearval = vwrowstudentfee.findViewById(R.id.tvsessionyearval)

            tvsessionyearval.setText(Constant.arrfee.get(0).session_name)


            tvsessionyearval.setTypeface(mRegular)

            tvsemester = vwrowstudentfee.findViewById(R.id.tvsemester)

            tvsemester.setTypeface(mbold)

            tvsemesterval = vwrowstudentfee.findViewById(R.id.tvsemesterval)

            tvsemesterval.setText(Constant.arrfee.get(0).semester_name)
            tvsemesterval.setTypeface(mRegular)

            tvschemename = vwrowstudentfee.findViewById(R.id.tvschemename)
            tvschemename.setTypeface(mbold)

            tvschemenameval = vwrowstudentfee.findViewById(R.id.tvschemenameval)
            tvschemenameval.setText(Constant.arrfee.get(0).studentfeesch_fs_name)
            tvschemenameval.setTypeface(mRegular)



            tvschemeprintname = vwrowstudentfee.findViewById(R.id.tvschemeprintname)
            tvschemeprintname.setTypeface(mbold)

            tvschemeprintnameval = vwrowstudentfee.findViewById(R.id.tvschemeprintnameval)
            tvschemeprintnameval.setText(Constant.arrfee.get(0).studentfeesch_fs_name)

            tvschemeprintnameval.setTypeface(mRegular)


            tvfeename = vwrowstudentfee.findViewById(R.id.tvfeename)

            tvfeename.setTypeface(mbold)



            tvfeenameval = vwrowstudentfee.findViewById(R.id.tvfeenameval)

            tvfeenameval.setTypeface(mRegular)

            tvfeenameval.setText(Constant.arrfee.get(0).fees_details.get(0).sfsd_fee_name)


            tvfeeprintname = vwrowstudentfee.findViewById(R.id.tvfeeprintname)

            tvfeeprintname.setTypeface(mbold)

            tvfeeprintnameval = vwrowstudentfee.findViewById(R.id.tvfeeprintnameval)

            tvfeeprintnameval.setTypeface(mRegular)

            tvfeeprintnameval.setText(Constant.arrfee.get(0).fees_details.get(0).sfsd_fee_print_name)


            tvfeecode = vwrowstudentfee.findViewById(R.id.tvfeecode)
            tvfeecode.setTypeface(mbold)



            tvfeecodeval = vwrowstudentfee.findViewById(R.id.tvfeecodeval)
            tvfeecodeval.setTypeface(mRegular)
            tvfeecodeval.setText(Constant.arrfee.get(0).fees_details.get(0).sfsd_fee_code)

            tvcategory = vwrowstudentfee.findViewById(R.id.tvcategory)
            tvcategory.setTypeface(mbold)


            tvcategoryval = vwrowstudentfee.findViewById(R.id.tvcategoryval)
            tvcategoryval.setText(Constant.arrfee.get(0).fees_details.get(0).sfsd_fee_cat_id)

            tvcategoryval.setTypeface(mRegular)

            tvfeeamt = vwrowstudentfee.findViewById(R.id.tvfeeamt)
            tvfeeamt.setTypeface(mbold)

            tvfeeamtval = vwrowstudentfee.findViewById(R.id.tvfeeamtval)  //tvfeeamtval
            tvfeeamtval.setTypeface(mRegular)

            tvfeeamtval.setText("INR " + Integer.toString(Constant.arrfee.get(0).fees_details.get(0).sfsd_fsd_amount))

            tvdiscount = vwrowstudentfee.findViewById(R.id.tvdiscount)
            tvdiscount.setTypeface(mbold)

            tvdiscountval = vwrowstudentfee.findViewById(R.id.tvdiscountval)
            tvdiscountval.setTypeface(mRegular)

            tvdiscountval.setText(
                "INR " + Integer.toString(
                    Constant.arrfee.get(0).fees_details.get(
                        0
                    ).sfsd_discount_amount
                )
            )


            tvnetpayment = vwrowstudentfee.findViewById(R.id.tvnetpayment)
            tvnetpayment.setTypeface(mbold)

            tvnetpaymentval = vwrowstudentfee.findViewById(R.id.tvnetpaymentval)
            tvnetpaymentval.setTypeface(mRegular)

            tvnetpaymentval.setText(
                "INR " + Integer.toString(
                    Constant.arrfee.get(0).fees_details.get(
                        0
                    ).sfsd_net_amount
                )
            )




            tvtotalpaid = vwrowstudentfee.findViewById(R.id.tvtotalpaid)
            tvtotalpaid.setTypeface(mbold)


            tvtotalpaidval = vwrowstudentfee.findViewById(R.id.tvtotalpaidval)

            tvtotalpaidval.setTypeface(mRegular)

            tvtotalpaidval.setText(
                "INR " + Integer.toString(
                    Constant.arrfee.get(0).fees_details.get(
                        0
                    ).sfsd_total_paid_amount
                )
            )


            tvduedate = vwrowstudentfee.findViewById(R.id.tvduedate)
            tvduedate.setTypeface(mbold)

            tvduedateval = vwrowstudentfee.findViewById(R.id.tvduedateval)

            tvduedateval.setTypeface(mRegular)

            tvduedateval.setText(Constant.arrfee.get(0).fees_details.get(0).sfsd_due_date)


            tvrefund = vwrowstudentfee.findViewById(R.id.tvrefund)

            tvrefund.setTypeface(mbold)



            tvrefundval = vwrowstudentfee.findViewById(R.id.tvrefundval)

            tvrefundval.setTypeface(mRegular)

            tvrefundval.setText("INR " + Integer.toString(Constant.arrfee.get(0).fees_details.get(0).sfsd_total_refund_amount))

            tvbalamt = vwrowstudentfee.findViewById(R.id.tvbalamt)
            tvbalamt.setTypeface(mbold)
            tvpaymentstatus = vwrowstudentfee.findViewById(R.id.tvpaymentstatus)
            tvpaymentstatus.setTypeface(mbold)

            tvbalamtval = vwrowstudentfee.findViewById(R.id.tvbalamtval)

            tvbalamtval.setTypeface(mRegular)
            tvpaymentstatusval = vwrowstudentfee.findViewById(R.id.tvpaymentstatusval)

            tvpaymentstatusval.setTypeface(mRegular)
            if (Constant.arrfee.get(0).fees_details.get(0).sfsd_payment_status == 1) {
                tvpaymentstatusval.setText("Paid")
            } else {
                tvpaymentstatusval.setText("Unpaid")
            }


            var balamt: Int =
                Constant!!.arrfee.get(0).fees_details.get(0).sfsd_fsd_amount - Constant.arrfee.get(0).fees_details.get(
                    0
                ).sfsd_discount_amount

            tvbalamtval.setText("INR " + Integer.toString(balamt))

            lllayoutfee.addView(vwrowstudentfee)
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }

    }


}