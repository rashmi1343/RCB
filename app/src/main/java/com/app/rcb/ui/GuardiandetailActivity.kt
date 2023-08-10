package com.app.rcb.ui


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.app.rcb.R
import com.app.rcb.db.entity.Dropdown
import com.app.rcb.db.entity.State
import com.app.rcb.util.Constant
import com.app.rcb.util.FileUtil
import com.app.rcb.viewmodel.rcbviewModel
import com.premierticketbookingapp.apicall.HttpTask
import kotlinx.android.synthetic.main.layout_guradian_detail.*
import kotlinx.android.synthetic.main.layout_personal_detail.*
import org.json.JSONException
import org.json.JSONObject

class GuardiandetailActivity : AdapterView.OnItemSelectedListener, AppCompatActivity() {
    private lateinit var mMedium: Typeface
    private lateinit var mHelpLayout: RelativeLayout

    private lateinit var strfathername: String
    private lateinit var strmothername: String
    private lateinit var strcategory: String
    private lateinit var strnationality: String
    private lateinit var strdomicile: String
    private lateinit var permanentstraddressline: String
    private lateinit var permanentstrpincode: String
    private lateinit var correspondencestraddressline: String
    private lateinit var correspondencestrpincode: String
    private var strstate: Int = 0
    private var statecor: Int = 0
    private var submitforreview: Int = 0
    private lateinit var mRegular: Typeface
    private lateinit var stremergencycontact: String
    private lateinit var relation: String
    private lateinit var adhaarno: String
    private lateinit var passportno: String
    var strstatename: String? = null
    var strstatenamecor: String? = null

    private var marrcategory: ArrayList<String> = ArrayList()
    private var marrnationality: ArrayList<String> = ArrayList()
    private var marrdomicile: ArrayList<String> = ArrayList()

    private var marrbloodgrp: ArrayList<String> = ArrayList()

    //private var marrstatelist:List<State> = ArrayList()

    private var marrstrstatelist: ArrayList<String> = ArrayList()
    private var marrstrstatelistcorr: ArrayList<String> = ArrayList()

    lateinit var typefacefontawesome: Typeface

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_guradian_detail)
        Log.d("OncREATE", "ONCREATE")


        //  strstate=0
        strfathername = intent.getStringExtra("fathername").toString()
        strmothername = intent.getStringExtra("mothername").toString()
        strcategory = intent.getStringExtra("category").toString()
        strnationality = intent.getStringExtra("nationality").toString()
        strdomicile = intent.getStringExtra("domicile").toString()
        permanentstraddressline = intent.getStringExtra("permanentaddressline").toString()
        permanentstrpincode = intent.getStringExtra("permanentaddresslinepincode").toString()
        correspondencestraddressline = intent.getStringExtra("correspondenceaddressline").toString()
        correspondencestrpincode = intent.getStringExtra("correspondencepincode").toString()
        strstate = intent.getIntExtra("state", 0)
        statecor = intent.getIntExtra("statecor", 0)
        Log.d("strstate", strstate.toString())
        stremergencycontact = intent.getStringExtra("emergencycontact").toString()
        relation = intent.getStringExtra("relation").toString()
        adhaarno = intent.getStringExtra("adhaarno").toString()
        passportno = intent.getStringExtra("passportno").toString()
        statepst = 0
        rcbdbviewModel = ViewModelProviders.of(this).get(rcbviewModel::class.java)
        spinstate!!.setSelection(1)


        mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")


        tv_father_name.setTypeface(mRegular, Typeface.BOLD)
        tv_mother_name.setTypeface(mRegular, Typeface.BOLD)
        tv_category_name.setTypeface(mRegular, Typeface.BOLD)
        tv_nationality_name.setTypeface(mRegular, Typeface.BOLD)
        tv_domicile_name.setTypeface(mRegular, Typeface.BOLD)
        tv_emergency_contact_name.setTypeface(mRegular, Typeface.BOLD)
        tvbloodgroup.setTypeface(mRegular, Typeface.BOLD)
        tv_relation.setTypeface(mRegular, Typeface.BOLD)
        tv_adhaar.setTypeface(mRegular, Typeface.BOLD)
        tv_passport.setTypeface(mRegular, Typeface.BOLD)
        tv_address_name.setTypeface(mRegular, Typeface.BOLD)
        tv_pincode_name.setTypeface(mRegular, Typeface.BOLD)
        tv_state_name.setTypeface(mRegular, Typeface.BOLD)
        tv_coraddress_name.setTypeface(mRegular, Typeface.BOLD)
        tv_corpincode_name.setTypeface(mRegular, Typeface.BOLD)
        tv_corstate_name.setTypeface(mRegular, Typeface.BOLD)
        //  spinstatecor!!.setSelection(1)
        //  marrstatelist = ArrayList()
        // marrstatelist = rcbdbviewModel.getallstatelist()

        //marrstrstatelist.add("--Select--")
        // for (i in 0..marrstatelist!!.size - 1) {
        //   println("Loop: $n")

        //    marrstrstatelist.add(marrstatelist!!.get(i).name)
        // }


        typefacefontawesome =
            Typeface.createFromAsset(applicationContext.assets, "fonts/FontAwesome.ttf")

        if (Constant.objProfileResponse.issubmitforreview == 1) {
            llayoutsameaddress.setVisibility(View.GONE)


        } else {


            llayoutsameaddress.setVisibility(View.VISIBLE)
            tvchecksameadd.setTypeface(typefacefontawesome)

            //  tvchecksameadd.text = "\uF10C"//unchecked
            tvchecksameadd.text = "\uF058"//checked

            tvchecksameadd.setOnClickListener {

                if (tvchecksameadd.text == "\uF058") { // unchecked condition
                    tvchecksameadd.setText("\uF10C")


                    spinstatecor.setSelection(0)
                    edt_coraddress_name.setText("")
                    edt_corpincode_name.setText("")


                    edt_corpincode_name.text.toString()
                    edt_corpincode_name.text.toString()
                    //  spinstatecor.setSelection(statepstcor)


                } else if (tvchecksameadd.text == "\uF10C") {  // checked condition
                    tvchecksameadd.setText("\uF058")

                    edt_coraddress_name.setText(edt_address_name.text.toString())
                    edt_corpincode_name.setText(edt_pincode_name.text.toString())

                    /*  if(Constant.objProfileResponse.studentaddress.get(0).addressstate!="null") {
                          spinstatecor.setSelection(Constant.objProfileResponse.studentaddress.get(0).addressstate.toInt())
                          //spinstatecor.
                      }*/

                    spinstatecor!!.setSelection(statepst)

                }

            }
        }

        //f046


        if (strfathername != "null") {
            edt_father_name.setText(strfathername)
        }
        if (strmothername != "null") {
            edt_mother_name.setText(strmothername)
        }
        // edt_category_name.setText(strcategory)
        //   edt_nationality_name.setText(strnationality)
        //   edt_domicile_name.setText(strdomicile)
        if (permanentstraddressline != "null") {
            edt_address_name.setText(permanentstraddressline)

        }

        if (permanentstrpincode != "null") {
            edt_pincode_name.setText(permanentstrpincode)

        }
        if (correspondencestraddressline != "null") {

            edt_coraddress_name.setText(correspondencestraddressline)
        }

        if (correspondencestrpincode != "null") {

          edt_corpincode_name.setText(correspondencestrpincode)
        }
        //edt_state_name.setText(strstate)
        if (stremergencycontact != "null") {
            edt_emergency_contact_name.setText(stremergencycontact)

        }
        if (relation != "null") {
            edt_relation.setText(relation)
        }

        if (adhaarno != "null") {
            edt_adhaar.setText(adhaarno)
        }

        if (passportno != "null") {
            edt_passport.setText(passportno)
        }
        if (Constant.objProfileResponse.issubmitforreview == 1) {

            edt_father_name.isEnabled = false
            edt_mother_name.isEnabled = false
            edt_address_name.isEnabled = false
            edt_pincode_name.isEnabled = false
            edt_coraddress_name.isEnabled = false
            edt_corpincode_name.isEnabled = false
            spinstatecor.isEnabled = false
            edt_emergency_contact_name.isEnabled = false
            edt_relation.isEnabled = false
            edt_adhaar.isEnabled = false
            edt_passport.isEnabled = false
            spincategory!!.isEnabled = false
            spinnationality!!.isEnabled = false
            spindomicile!!.isEnabled = false
            spinstate!!.isEnabled = false
            spinbloodgrp!!.isEnabled = false

        }


        mHelpLayout = findViewById(R.id.abt_header_layout)

        mHelpLayout.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
        }



        btnacademdetail.setOnClickListener {


            Constant.objProfileResponse.guradiandetail.student_father_name =
                edt_father_name.text.toString()
            Constant.objProfileResponse.guradiandetail.student_mother_name =
                edt_mother_name.text.toString()

            Constant.objProfileResponse.studentrelation = edt_relation.text.toString()
            Constant.objProfileResponse.studentadharno = edt_adhaar.text.toString()
            Constant.objProfileResponse.studentpassportno = edt_passport.text.toString()

            Constant.objProfileResponse.studentemergencyno =
                edt_emergency_contact_name.text.toString()
            Constant.objProfileResponse.studentaddress.get(0).address_pincode =
                edt_pincode_name.text.toString()
            Constant.objProfileResponse.studentaddress.get(0).addressline =
                edt_address_name.text.toString()
            Constant.objProfileResponse.studentaddress.get(1).address_pincode =
                edt_corpincode_name.text.toString()
            Constant.objProfileResponse.studentaddress.get(1).addressline =
                edt_coraddress_name.text.toString()


//            Constant.objProfileResponse.studentaddress.get(1).addressline=  edt_coraddress_name.text.toString()
//            Constant.objProfileResponse.studentaddress.get(1).address_pincode=  edt_corpincode_name.text.toString()

            //   Constant.objProfileResponse.studentaddress.get(0).addressstate =

            //   Constant.objProfileResponse.guradiandetail.student_category =  spincategory!!.selectedItem.toString()
            val academiintent =
                Intent(this@GuardiandetailActivity, AcademicdetailActivity::class.java)
            //       academiintent.putExtra("arrexampassed", objProfileResponse.studentacademicrecord)
            startActivity(academiintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        }

        setcategory()
        spinstate!!.setSelection(0)

        spinstatecor!!.setSelection(0)

        marrstrstatelist.add("--Select State--")
        marrstrstatelistcorr.add("--Select State--")
        for (i in 0 until arrstate.size) {
            if (arrstate.get(i).country_id == 101) {            // for india
                marrdomicile.add(arrstate.get(i).name)
            }

            if (arrstate.get(i).id.toInt() == strstate) {
                Log.d("statename", arrstate.get(i).name)
                strstatename = arrstate.get(i).name
            }

            if (arrstate.get(i).id.toInt() == statecor) {
                Log.d("statename", arrstate.get(i).name)
                strstatenamecor = arrstate.get(i).name
            }

            marrstrstatelist.add(arrstate.get(i).name)

            marrstrstatelistcorr.add(arrstate.get(i).name)

        }


        var aastate = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrstrstatelist)
        aastate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var aastatecorr =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, marrstrstatelistcorr)
        aastate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        statepst = marrstrstatelist.indexOf(strstatename)
        statepstcor = marrstrstatelistcorr.indexOf(strstatenamecor)

        spinstate!!.setAdapter(aastate)
        spinstatecor!!.setAdapter(aastatecorr)

        spinstate!!.setSelection(statepst)
        spinstatecor!!.setSelection(statepstcor)
        if (Constant.objProfileResponse.issubmitforreview == 0) {
            spinstate!!.setOnItemSelectedListener(this)
            spinstatecor!!.setOnItemSelectedListener(this)
        }
        // if(strstate!="null") {
        //  spinstate!!.setSelection(strstate.toInt())
        //
        //  }
    }


    private var ddlistg: List<Dropdown> = ArrayList()
    lateinit var rcbdbviewModel: rcbviewModel

    private fun setcategory() {

        marrcategory = ArrayList()
        ddlistg = rcbdbviewModel.getalldropdownlist()!!
        marrcategory.add("--Select--")
        //  spingender

        for (i in 0..ddlistg.size - 1) {
            // for gender dropdown_dd_id =3
            if (ddlistg.get(i).dropdown_dd_id == 6) {
                marrcategory.add(ddlistg.get(i).dropdown_name)
            }
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrcategory)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spincategory!!.setAdapter(aa)
        if (Constant.objProfileResponse.issubmitforreview == 0) {
            spincategory!!.setOnItemSelectedListener(this)
        }

        if (strcategory != "null") {
            spincategory!!.setSelection(strcategory.toInt())
        }
        setnationality()
    }


    private fun setnationality() {

        marrnationality = ArrayList()

        marrnationality.add("--Select--")

        for (i in 0..ddlistg.size - 1) {
            // for gender dropdown_dd_id =3
            if (ddlistg.get(i).dropdown_dd_id == 5) {
                marrnationality.add(ddlistg.get(i).dropdown_name)
            } else if (ddlistg.get(i).dropdown_dd_id == 15) {
                marrbloodgrp.add(ddlistg.get(i).dropdown_name)
            }
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrnationality)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnationality!!.setAdapter(aa)
        if (Constant.objProfileResponse.issubmitforreview == 0) {
            spinnationality!!.setOnItemSelectedListener(this)
        }

        spinnationality!!.setSelection(strnationality.toInt())

        val adpterblodgrp = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrbloodgrp)
        adpterblodgrp.setDropDownViewResource(android.R.layout.simple_spinner_item)

        spinbloodgrp!!.adapter = adpterblodgrp
        spinbloodgrp!!.setOnItemSelectedListener(this)
        spinbloodgrp!!.setSelection(Constant.objProfileResponse.studentbloodgroup)

        spindomicile()
    }

    var statepst: Int = 0
    var statepstcor: Int = 0
    var statepdomicile: Int = 0

    var arrstate: List<State> = ArrayList()
    private fun spindomicile() {

        marrdomicile = ArrayList()
        //arrstate = ArrayList()
        arrstate = rcbdbviewModel.getallstatelist()!!

        marrdomicile.add("--Select--")


        /* for (i in 0..Constant.ddvalues.size-1) {
             // for gender dropdown_dd_id =3
             if (Constant.ddvalues.get(i).dropdown_dd_id==4) {
                 marrdomicile.add(Constant.ddvalues.get(i).dropdown_name)
             }
         }*/
        //  marrstrstatelistcorr.add("--Select State--")
        //

        // var nn =0
        /* for ( nn in 0 until marrstrstatelist.size-1) {
             if(nn==strstate.toInt()) {
                 Constant.statepst = nn +1
                 statepst = Constant.statepst
                 break
             }
         }*/


        //  marrstrstatelistcorr = marrstrstatelist

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrdomicile)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)



        spindomicile!!.setAdapter(aa)
        if (Constant.objProfileResponse.issubmitforreview == 0) {
            spindomicile!!.setOnItemSelectedListener(this)
        }



        if (strdomicile != "null") {
            spindomicile!!.setSelection(strdomicile.toInt())
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()

        overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
        // startActivity(Intent(this@GuardiandetailActivity, DashBoardActivity::class.java))

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        if (position > 0) {
            if (parent!!.id == R.id.spinstate) {
                //  Constant.objProfileResponse.studentaddress.get(0).addressstate = position
                statepst = position
                Constant.objProfileResponse.studentaddress.get(0).addressstate =
                    getselectedstateid(statepst)
            } else if (parent!!.id == R.id.spinstatecor) {
                //  Constant.objProfileResponse.studentaddress.get(1).addressstate = position
                statepstcor = position
                Constant.objProfileResponse.studentaddress.get(1).addressstate =
                    getselectedstateid(statepstcor)
            } else if (parent!!.id == R.id.spincategory) {
                Constant.objProfileResponse.guradiandetail.student_category = position.toString()
            } else if (parent!!.id == R.id.spinnationality) {
                Constant.objProfileResponse.guradiandetail.student_nationality = position.toString()
            } else if (parent!!.id == R.id.spindomicile) {
//                Constant.objProfileResponse.guradiandetail.student_domicile_state =
//                    position.toString()
                statepdomicile = position
                Constant.objProfileResponse.guradiandetail.student_domicile_state =
                    getselectedstateid(statepdomicile).toString()
            } else if (parent!!.id == R.id.spinbloodgrp) {
                Constant.objProfileResponse.studentbloodgroup = position
            }
        }

    }


    fun getselectedstateid(stateindex: Int): Int {
        var stateid: Int = 0
        for (i in 0 until arrstate.size) {
            if (i == stateindex) {

                stateid = arrstate.get(i).id.toInt() - 1
                break;
            }
        }
        return stateid
    }
}
