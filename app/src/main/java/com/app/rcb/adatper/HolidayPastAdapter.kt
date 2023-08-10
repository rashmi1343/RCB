package com.app.rcb.adatper

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.rcb.R
import com.app.rcb.response.Holiday
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HolidayPastAdapter(ctx: Context, private val mholidayList: List<Holiday>) :
    RecyclerView.Adapter<HolidayPastAdapter.ViewHolder>() {


    private lateinit var mRegular: Typeface

    //   private lateinit var mmontbold: Typeface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holiday_row_past, parent, false)


        mRegular =
            Typeface.createFromAsset(parent.context.assets, "montserrat/Montserrat-Regular.otf")


        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val holidayModel = mholidayList[position]
        val firstDate = holidayModel.holidays_date
       val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
       val date = formatter.parse(firstDate)
        val desiredFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy").format(date)
        Log.d("desiredFormatPast:", desiredFormat)
        holder.tvholidayname.text = holidayModel.holiday_name
         holder.tvholidaydate.text = desiredFormat
        holder.tvholidayname.setTypeface(mRegular)
         holder.tvholidaydate.setTypeface(mRegular)


//       //month extracting from api date
//        var datepast = LocalDate.parse(holidayModel.holidays_date)
//        Log.d("datepast:", datepast.toString())
//        val pastmonth=datepast.monthValue
//        Log.d("pastmonth:", pastmonth.toString())
//
//
//
//
//        //Current Date
//        val cDate = Date()
//        val fDate = SimpleDateFormat("yyyy-MM-dd").format(cDate)
//        Log.d("fDate:", fDate.toString())
//
//
//      //month extracting from current date
//        val cal = Calendar.getInstance()
//        cal.time = Date()
//        val monthpast = cal[Calendar.MONTH] + 1
//        Log.d("monthpast:", monthpast.toString())
//
//        val firstDate = holidayModel.holidays_date
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val date = formatter.parse(firstDate)
//        val desiredFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy").format(date)
//        Log.d("desiredFormat:",desiredFormat)
//
//     if (monthpast >= pastmonth){
//         holder.No_Holiday_txt.visibility = View.GONE
//         holder.tvholidayname.visibility = View.VISIBLE
//         holder.tvholidaydate.visibility = View.VISIBLE
//         holder.tvholidayname.text = holidayModel.holiday_name
//         holder.tvholidaydate.text = desiredFormat
//         holder.tvholidayname.setTypeface(mRegular)
//         holder.tvholidaydate.setTypeface(mRegular)
//     }
//     else {
//
//         holder.No_Holiday_txt.setTypeface(mRegular)
//         holder.No_Holiday_txt.visibility = View.VISIBLE
//         holder.tvholidayname.visibility = View.GONE
//         holder.tvholidaydate.visibility = View.GONE
//         Log.d("Holiday date:", "No Past Holidays found!!")
//     }

    }

    override fun getItemCount(): Int {
        return mholidayList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvholidayname: TextView = itemView.findViewById(R.id.Holiday_Name_val_txt)
        val tvholidaydate: TextView = itemView.findViewById(R.id.Holiday_Date_val_txt)
        val No_Holiday_txt: TextView = itemView.findViewById(R.id.No_Holiday_txt)

    }
}