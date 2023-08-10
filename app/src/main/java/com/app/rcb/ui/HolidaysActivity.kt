package com.app.rcb.ui

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.rcb.R
import com.app.rcb.adatper.HolidayPastAdapter
import com.app.rcb.adatper.HolidayUpcomingAdapter
import com.app.rcb.response.Holiday
import com.app.rcb.util.LoadingScreen
import com.app.rcb.viewmodel.rcbviewModel
import kotlinx.android.synthetic.main.activity_holidays.*
import kotlinx.android.synthetic.main.layout_assignment.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class HolidaysActivity : AppCompatActivity() {
    private var arrpstholidaylist: ArrayList<Holiday> = ArrayList()
    private var arrfutureholidaylist: ArrayList<Holiday> = ArrayList()
    lateinit var rcbdbviewModel: rcbviewModel
    private var mAdapter: HolidayPastAdapter? = null
    private lateinit var mRegular: Typeface
    private var mAdapterupcoming: HolidayUpcomingAdapter? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holidays)
        //  val rvupcomingholidayspast: RecyclerView = findViewById(R.id.rvupcomingholidayspast)
        mRegular =
            Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")

        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        rcbdbviewModel = ViewModelProviders.of(this).get(rcbviewModel::class.java)
        /*   getholidays(
               "Bearer " + Constant.accesstoken,
               "getholiday",
               Constant.objProfileResponse.stdprgname.toInt()
           )*/

        back_holidays_layout_detail.setOnClickListener {

            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)

        }
        //Past Holidays
        val manager = LinearLayoutManager(this@HolidaysActivity)
        //rvupcomingholidayspast.setLayoutManager(manager)
        manager.orientation = LinearLayoutManager.VERTICAL
        rvupcomingholidayspast.setLayoutManager(manager)
        rvupcomingholidayspast.setHasFixedSize(true);
      /*  rcbdbviewModel.holidaylistapi.observe(this, Observer {
            Log.d("holidaylist", it.holiday.toString())
            LoadingScreen.hideLoading()

            mAdapter = HolidayPastAdapter(this@HolidaysActivity, it.holiday)
            rvupcomingholidayspast.adapter = mAdapter
        })*/


        //Upcoming Holidays
        rvupcomingholidaysupcoming.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcbdbviewModel.holidaylistapi.observe(this, Observer {
            Log.d("holidaylist", it.holiday.toString())


            No_Holiday_upcoming_txt.setTypeface(mRegular)
            No_Holiday_past_txt.setTypeface(mRegular)


            arrpstholidaylist = ArrayList()
            arrfutureholidaylist = ArrayList()
            for(holidayitem in it.holiday) {
                var dateupcoming = LocalDate.parse(holidayitem.holidays_date)
                Log.d("dateupcoming:", dateupcoming.toString())
                val upcomingmonth = dateupcoming.monthValue
                Log.d("upcomingmonth:", upcomingmonth.toString())

                //Current Date
                val cDate = Date()
                val fDate = SimpleDateFormat("yyyy-MM-dd").format(cDate)
                Log.d("fDate:", fDate.toString())

                //month extracting from current date
                val cal = Calendar.getInstance()
                cal.time = Date()
                val month = cal[Calendar.MONTH] + 1
                Log.d("month:", month.toString())

                val firstDate = holidayitem.holidays_date
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val date = formatter.parse(firstDate)
                val desiredFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy").format(date)
                Log.d("desiredFormat:", desiredFormat)


                // past holiday
                if (month >= upcomingmonth) {
                   /* holder.No_Holiday_upcoming_txt.setTypeface(mRegular)
                    holder.No_Holiday_upcoming_txt.visibility = View.VISIBLE
                    holder.tvholidaynameupcoming.visibility = View.GONE
                    holder.tvholidaydateupcoming.visibility = View.GONE*/
                    Log.d("Holiday date:", "No Holidays found!!")
                    arrpstholidaylist.add(holidayitem)
                } else {
                /*    holder.No_Holiday_upcoming_txt.visibility = View.GONE
                    holder.tvholidaynameupcoming.visibility = View.VISIBLE
                    holder.tvholidaydateupcoming.visibility = View.VISIBLE
                    holder.tvholidaynameupcoming.text = holidayModel.holiday_name
                    holder.tvholidaydateupcoming.text = desiredFormat
                    holder.tvholidaynameupcoming.setTypeface(mRegular)
                    holder.tvholidaydateupcoming.setTypeface(mRegular)*/

                    arrfutureholidaylist.add(holidayitem)
                }


            }


            LoadingScreen.hideLoading()

            if(arrfutureholidaylist.size>0) {
                mAdapterupcoming =
                    HolidayUpcomingAdapter(this@HolidaysActivity, arrfutureholidaylist)
                rvupcomingholidaysupcoming.adapter = mAdapterupcoming
                No_Holiday_upcoming_txt.visibility=View.GONE
            }else{
                No_Holiday_upcoming_txt.visibility=View.VISIBLE
            }

            if(arrpstholidaylist.size>0) {
                mAdapter =
                    HolidayPastAdapter(this@HolidaysActivity, arrpstholidaylist)
                rvupcomingholidayspast.adapter = mAdapter
                No_Holiday_past_txt.visibility=View.GONE
            }else{
                No_Holiday_past_txt.visibility=View.VISIBLE
            }

            })

        download_layout.setOnClickListener {
//            if (FileUtil.isNetworkAvailable(this)) {
//                val browserIntent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse(Constant.baseurl + "beta/student/download-holiday")
//                )
//                Log.d("filename:", Constant.baseurl + "beta/student/download-holiday")
//                startActivity(browserIntent)
//            } else {
//
//            }
            val intent = Intent(this, HolidaypdfActivity::class.java)
            intent.putExtra("ViewType", "assets")
            startActivity(intent)
//            val i = Intent(applicationContext, WebViewActivity::class.java)
//            startActivity(i)
//            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

        }

    }

    /*
    private fun getholidays(mToken: String, methodname: String, s_prg_id: Int) {
        val getholidayslist =
            ApiClient.getClient.getholidayslist(mToken, methodname, s_prg_id)

        getholidayslist.enqueue(object : Callback<HolidaysResponse> {
            override fun onFailure(call: Call<HolidaysResponse>, t: Throwable) {

                Log.d("Profile Error", t.toString())
                //   LoadingScreen.hideLoading()

            }

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<HolidaysResponse>,
                response: Response<HolidaysResponse>
            ) {

                try {
                    if (response.isSuccessful) {

                        if (response.body()!!.status == 1) {


                            Constant.objHolidayResponse = response.body()!!

                            Log.d("holiday_data", Constant.objHolidayResponse.toString());

                            Holiday_Name_past_val_txt.text =
                                Constant.objHolidayResponse.holiday.get(2).holidayName

                            Holiday_Date_past_val_txt.setText(
                                Constant.objHolidayResponse.holiday.get(4).holidaysDate
                            )


                        }

                    }
                    LoadingScreen.hideLoading()
                } catch (e: Exception) {
                    Log.d("Excepition", e.toString())
                }
            }

        });

    }
*/
}