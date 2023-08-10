package com.app.rcb.adatper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.rcb.R
import com.app.rcb.response.assignment
import com.app.rcb.util.Constant
import com.app.rcb.util.Constant.Companion.assignmenturl
import com.app.rcb.util.FileUtil


class assignmentadapter(
    ctx: Context,
    private val mList: ArrayList<assignment>
  // private val mdownloadlist: ArrayList<downloadassignment>
) : RecyclerView.Adapter<assignmentadapter.ViewHolder>() {

    private lateinit var mRegular: Typeface
    private lateinit var mmontbold: Typeface


    var context: Context? = ctx
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.assignment_row, parent, false)

        mRegular =
            Typeface.createFromAsset(parent.context.assets, "montserrat/Montserrat-Regular.otf")
        mmontbold =
            Typeface.createFromAsset(parent.context.assets, "montserrat/Montserrat-Bold.otf")

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: assignmentadapter.ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
    //  val downloadlist = mdownloadlist[position]

        holder.tvassignmentval.text = ItemsViewModel.as_name



        holder.tvcoursenameval.text = ItemsViewModel.course_name

        holder.tvdateval.text = ItemsViewModel.due_date

        holder.tvassignmentname.setTypeface(mmontbold)
        holder.tvassignmentval.setTypeface(mRegular)

        holder.tvcoursename.setTypeface(mmontbold)

        holder.tvcoursenameval.setTypeface(mRegular)

        holder.tvduedate.setTypeface(mmontbold)
        holder.tvdateval.setTypeface(mRegular)

        holder.tvaction.setTypeface(mmontbold)
        holder.tvactionval.setTypeface(mRegular)

        holder.tvactionval.setOnClickListener {

            //  var filename: String = ItemsViewModel.assigndocument_file

            if (FileUtil.isNetworkAvailable(context)) {


                //      remotePDFViewPager = RemotePDFViewPager(this, url, this)
                   val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(assignmenturl+ Constant.arrdownloadassignment[position].assigndocument_file))

                Log.d("filename:", Constant.arrdownloadassignment[position].assigndocument_file)

                   context!!.startActivity(browserIntent)
            } else {

            }

        }


    }

    override fun getItemCount(): Int {

        return mList.size
    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val tvassignmentname: TextView = itemView.findViewById(R.id.tvassignmentname)
        val tvassignmentval: TextView = itemView.findViewById(R.id.tvassignmentval)
        val tvcoursename: TextView = itemView.findViewById(R.id.tvcoursename)
        val tvcoursenameval: TextView = itemView.findViewById(R.id.tvcourseval)
        val tvduedate: TextView = itemView.findViewById(R.id.tvduedate)
        val tvdateval: TextView = itemView.findViewById(R.id.tvdateval)
        val tvaction: TextView = itemView.findViewById(R.id.tvaction)
        val tvactionval: TextView = itemView.findViewById(R.id.tvactionval)


    }
}