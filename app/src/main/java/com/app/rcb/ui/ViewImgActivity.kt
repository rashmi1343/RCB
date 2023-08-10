package com.app.rcb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.rcb.R
import com.app.rcb.util.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_img.*

class ViewImgActivity : AppCompatActivity() {

    private lateinit var imgfilename:String
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img)

        imgfilename = intent.getStringExtra("imgname").toString()


        Picasso
                .get()
                .load(imgfilename)
                .resize(200, 200)
                .noFade()
                .into(imgView);


    }
}