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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HolidayUpcomingAdapter(ctx: Context, private val mholidayList: List<Holiday>) :
    RecyclerView.Adapter<HolidayUpcomingAdapter.ViewHolder>() {


    private lateinit var mRegular: Typeface

    //   private lateinit var mmontbold: Typeface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holiday_row_upcoming, parent, false)


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
        Log.d("desiredFormat:", desiredFormat)

                holder.tvholidaynameupcoming.text = holidayModel.holiday_name
                holder.tvholidaydateupcoming.text = desiredFormat
                holder.tvholidaynameupcoming.setTypeface(mRegular)
                holder.tvholidaydateupcoming.setTypeface(mRegular)
        //val date = Date()
//        val format = SimpleDateFormat("EEEE, MMMM dd, ''yyyy")
//        val date = format.format(Date.parse(holidayModel.holidays_date))
        //  Log.d(" date:", date)

        //month extracting from api date
//        var dateupcoming = LocalDate.parse(holidayModel.holidays_date)
//        Log.d("dateupcoming:", dateupcoming.toString())
//        val upcomingmonth = dateupcoming.monthValue
//        Log.d("upcomingmonth:", upcomingmonth.toString())
//
//        //Current Date
//        val cDate = Date()
//        val fDate = SimpleDateFormat("yyyy-MM-dd").format(cDate)
//        Log.d("fDate:", fDate.toString())
//
//        //month extracting from current date
//        val cal = Calendar.getInstance()
//        cal.time = Date()
//        val month = cal[Calendar.MONTH] + 1
//        Log.d("month:", month.toString())
//
//        val firstDate = holidayModel.holidays_date
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val date = formatter.parse(firstDate)
//        val desiredFormat = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy").format(date)
//        Log.d("desiredFormat:", desiredFormat)
//
//
//
//            if (month >= upcomingmonth) {
//                holder.No_Holiday_upcoming_txt.setTypeface(mRegular)
//                holder.No_Holiday_upcoming_txt.visibility = View.VISIBLE
//                holder.tvholidaynameupcoming.visibility = View.GONE
//                holder.tvholidaydateupcoming.visibility = View.GONE
//                Log.d("Holiday date:", "No Holidays found!!")
//            } else {
//                holder.No_Holiday_upcoming_txt.visibility = View.GONE
//                holder.tvholidaynameupcoming.visibility = View.VISIBLE
//                holder.tvholidaydateupcoming.visibility = View.VISIBLE
//                holder.tvholidaynameupcoming.text = holidayModel.holiday_name
//                holder.tvholidaydateupcoming.text = desiredFormat
//                holder.tvholidaynameupcoming.setTypeface(mRegular)
//                holder.tvholidaydateupcoming.setTypeface(mRegular)
//
//
//            }






//
//        if (fDate >= holidayModel.holidays_date.toString()) {
//            holder.No_Holiday_upcoming_txt.setTypeface(mRegular)
//            holder.No_Holiday_upcoming_txt.visibility = View.VISIBLE
//            holder.tvholidaynameupcoming.visibility = View.GONE
//            holder.tvholidaydateupcoming.visibility = View.GONE
//            Log.d("Holiday date:","No Holidays found!!")
//
//
//        }else{
//            holder.No_Holiday_upcoming_txt.visibility = View.GONE
//            holder.tvholidaynameupcoming.visibility = View.VISIBLE
//            holder.tvholidaydateupcoming.visibility = View.VISIBLE
//            holder.tvholidaynameupcoming.text = holidayModel.holiday_name
//            holder.tvholidaydateupcoming.text = holidayModel.holidays_date
//            holder.tvholidaynameupcoming.setTypeface(mRegular)
//            holder.tvholidaydateupcoming.setTypeface(mRegular)
//
//
//        }

    }


    override fun getItemCount(): Int {
        return mholidayList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvholidaynameupcoming: TextView =
            itemView.findViewById(R.id.Holiday_Name_val_upcoming_txt)
        val tvholidaydateupcoming: TextView =
            itemView.findViewById(R.id.Holiday_Date_val_upcoming_txt)
        val No_Holiday_upcoming_txt: TextView = itemView.findViewById(R.id.No_Holiday_upcoming_txt)

    }
}