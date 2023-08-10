package com.app.rcb.adatper

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.rcb.R
import com.app.rcb.response.academiccalendar

class calendaradapter(private val mList: List<academiccalendar>) : RecyclerView.Adapter<calendaradapter.ViewHolder>() {

    // create new views
    //private lateinit var latoregular: Typeface
    private lateinit var mRegular: Typeface
    private lateinit var mmontbold: Typeface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_sem_row, parent, false)

   //     latoregular = Typeface.createFromAsset(parent.context.assets, "fonts/Lato-Regular.ttf")
        mRegular = Typeface.createFromAsset(parent.context.assets, "montserrat/Montserrat-Regular.otf")
        mmontbold = Typeface.createFromAsset(parent.context.assets, "montserrat/Montserrat-Bold.otf")

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        // holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        // holder.textView.text = ItemsViewModel.text

        holder.tvexamname.text = ItemsViewModel.acd_event_name
        holder.tvstartdateval.text = ItemsViewModel.acd_event_start_date
        holder.tvenddateval.text = ItemsViewModel.acd_event_end_date

        holder.tvexamname.setTypeface(mmontbold)
        holder.tvstartdateval.setTypeface(mRegular)
        holder.tvenddateval.setTypeface(mRegular)
        holder.tvstartdate.setTypeface(mmontbold)
        holder.tvenddate.setTypeface(mmontbold)


      /*  holder.tvdate.setTypeface(latoregular)
        holder.tvslottime.setTypeface(latoregular)
        holder.tvslottimeval.setTypeface(latoregular)
        holder.tvstatename.setTypeface(latoregular)
        holder.tvstatenameval.setTypeface(latoregular)*/




    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvexamname: TextView = itemView.findViewById(R.id.tvexamname)
        val tvstartdate: TextView = itemView.findViewById(R.id.tvstartdate)
        val tvenddate: TextView = itemView.findViewById(R.id.tvenddate)

        val tvstartdateval: TextView = itemView.findViewById(R.id.tvstartdateval)

        val tvenddateval:TextView = itemView.findViewById(R.id.tvenddateval)

    }
}
