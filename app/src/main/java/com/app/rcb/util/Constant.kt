package com.app.rcb.util

import com.app.rcb.db.entity.*
import com.app.rcb.response.*
import java.io.File


class Constant {

    companion object {

        // old beta server broadway
      //  const val baseurl = "https://rcb.broadwayinfotech.net.au/"
        const val baseurl = "https://acad.rcb.ac.in/"

        const val url = baseurl + "beta/api/"
     //   const val stdurl = "http://192.168.29.251:80/api/"
       // var BASE_URL: String = "http://192.168.29.251/api/student/"
        //const val photourl = "https://rcb.broadwayinfotech.net.au/beta/storage/uploads/users/photographs/"
        const val photourl = baseurl + "beta/storage/uploads/users/photographs/"
     //   const val sigurl = "https://rcb.broadwayinfotech.net.au/beta/storage/uploads/users/student_signatures/"
       const val sigurl = baseurl + "beta/storage/uploads/users/student_signatures/"
        const val ccertificateurl = baseurl + "beta/storage/uploads/users/character_certificates/"
        const val academicurl = baseurl + "beta/storage/uploads/users/academic_documents/"
        //const val bankpburl = baseurl + "beta/storage/uploads/users/passbook/"
        const val bankpburl = baseurl + "beta/storage/"
        const val bankurl = baseurl + "beta/storage/"
        const val entranceproof = baseurl + "beta/storage/uploads/users/entrance_proofs/"

        const val assignmenturl = baseurl + "beta/assets/images/faculty/doc/"

       lateinit var objProfileResponse: ProfileResponse
       lateinit var objHolidayResponse: HolidaysResponse

         var accesstoken:String =" "
        lateinit var  original_file: File

         var student_char_certificate:String = " "
         var student_signature:String = " "
         var student_entranceproof:String =" "
        var student_entranceexam:String = " "
         var student_academic:String =" "
         var student_passbook:String =" "
         var student_photo:String = " "

        var statepst:Int=0

        lateinit var arracdemiccalendar: List<academiccalendar>

        lateinit var arrasignment:ArrayList<assignment>

        lateinit var  arrfee:ArrayList<studentfee>

        lateinit var arrdownloadassignment : ArrayList<downloadassignment>
        lateinit var holiday : ArrayList<Holiday>

         var  strprgmid:ArrayList<Int> = ArrayList()
      //  lateinit var arrmaritalstatus:ArrayList<String>
      val PREFS_NAME = "LoginData"


    }
}