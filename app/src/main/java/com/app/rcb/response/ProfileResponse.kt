package com.app.rcb.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/*
@Parcelize
data class newprofileResponse(val mtoken: String, val status:String, val message:String,
 val studentid:String, val firstname:String, val middlename:String, val lastname: String, val studentregno:String,

) : Parcelable
*/

data class ProfileResponse(

    /*  @SerializedName("token")
      public var mtoken :String,*/

    @SerializedName("status")
    public var status: Int,

    @SerializedName("message")
    public var msg: String,

    @SerializedName("studentid")
    public var studentid: String,

    @SerializedName("firstname")
    public var firstname: String,

    @SerializedName("middlename")
    public var middlename: String,

    @SerializedName("lastname")
    public var lastname: String,

    @SerializedName("studentregno")
    public var studentregno: String,

    @SerializedName("studentusername")
    public var studentusername: String,

    @SerializedName("studentstatus")
    public var studentstatus: String,

    @SerializedName("studentdob")
    public var studentdob: String,

    @SerializedName("studentphone")
    public var studentphone: String,

    @SerializedName("studentinstituteid")
    public var stdinstituteid: String,


    @SerializedName("studentprogramname")
    public var stdprgname: String,


    @SerializedName("studentassessmentyear")
    public var stdassyear: String,

    @SerializedName("studentbranch")
    public var stdbranch: String,


    @SerializedName("studentnationallevelentrancequalify")
    public var studentnationallevelentrancequalify: studentnationallevelentrancequalify,


    @SerializedName("GuardianDetail")
    public var guradiandetail: guradiandetail,

    @SerializedName("StudentAddress")
    public var studentaddress: ArrayList<studentaddress>,

    @SerializedName("studentacademicrecord")
    public var studentacademicrecord: ArrayList<arrstudentacademicrecord>,

    @SerializedName("studentbankdetails")
    public var studentbankdetails: ArrayList<studentbankdetail>,

    @SerializedName("studentadharno")
    public var studentadharno: String,

    @SerializedName("studentpassportno")
    public var studentpassportno: String,

    @SerializedName("studentemergencyno")
    public var studentemergencyno: String,

    @SerializedName("studentrelation")
    public var studentrelation: String,

    @SerializedName("studentcharactercertificate")
    public var studentcharactercertificate: String,

    @SerializedName("studentsignature")
    public var studentsignature: String,

    @SerializedName("studentphoto")
    public var studentphoto: String,

    @SerializedName("studentgenderid")
    public var studentgenderid: String,

    @SerializedName("studentemail")
    public var studentemail: String,

    @SerializedName("studentbloodgroup")
    public var studentbloodgroup: Int,

    @SerializedName("studentmaritalstatus")
    public var studentmaritalstatus: Int,

    @SerializedName("student_session_id")
    public var studentsessionid: Int,
    @SerializedName("student_seasons")
    var studentSeasons: Int,
    @SerializedName("IsSubmitforReview")
    public var issubmitforreview: Int,

    @SerializedName("studentregistrationdate")
    public var studentregistrationdate: String,

    @SerializedName("studentspecialization")
    public var studentspecialization: Int,

    @SerializedName("studentrollno")
    public var studentrollno: String
)


data class studentnationallevelentrancequalify(

    @SerializedName("student_entrance_qualifying_year")
    public var student_entrance_qualifying_year: String,

    @SerializedName("student_entrance_examination")
    public var student_entrance_examination: String,

    @SerializedName("student_entrance_proof")
    public var student_entrance_proof: String


)

data class guradiandetail(

    @SerializedName("student_father_name")
    public var student_father_name: String,

    @SerializedName("student_mother_name")
    public var student_mother_name: String,

    @SerializedName("student_category")
    public var student_category: String,

    @SerializedName("student_nationality")
    public var student_nationality: String,

    @SerializedName("student_domicile_state")
    public var student_domicile_state: String,

    @SerializedName("student_domicile_country")
    public var student_domicile_country: String

)

data class studentaddress(
    @SerializedName("address_id")
    public var address_id: String,

    @SerializedName("address_student_id")
    public var address_Student_id: String,

    @SerializedName("address_type")
    public var addresstype: String,

    @SerializedName("address_line")
    public var addressline: String,
    @SerializedName("address_line2")
    var addressLine2: String?,
    @SerializedName("address_country")
    var addressCountry: String?,
    @SerializedName("address_state")
    public var addressstate: Int,

    @SerializedName("address_pincode")
    public var address_pincode: String,

    @SerializedName("address_status")
    public var address_status: String,

    @SerializedName("address_added_by")
    public var address_added_by: String?,

    @SerializedName("address_created_at")
    public var address_created_at: String,

    @SerializedName("address_updated_at")
    public var address_updated_at: String
)


data class arrstudentacademicrecord(
    @SerializedName("sa_id")
    public var sa_id: String,

    @SerializedName("sa_student_id")
    public var sa_student_id: String,
    @SerializedName("sa_qualification_id")
    var saQualificationId: Int?,
    @SerializedName("sa_month")
    var saMonth: String?,
    @SerializedName("sa_name")
    public var sa_name: String,

    @SerializedName("sa_university")
    public var sa_university: String,

    @SerializedName("sa_institute")
    public var sa_institute: String,

    @SerializedName("sa_subject")
    public var sa_subject: String,

    @SerializedName("sa_year")
    public var sa_year: String,
    @SerializedName("sa_evaluation_type"  )
    var saEvaluationType  : Int?,
    @SerializedName("sa_marks")
    public var sa_marks: String,

    @SerializedName("sa_cgpa")
    public var sa_cgpa: String,

    @SerializedName("sa_document")
    public var sa_document: String,

    @SerializedName("sa_status")
    public var sa_status: String,

    @SerializedName("sa_added_by")
    public var sa_added_by: String?,

    @SerializedName("sa_updated_by")
    public var sa_updated_by: String?,

    @SerializedName("sa_created_at")
    public var sa_created_at: String,

    @SerializedName("sa_updated_at")
    public var sa_updated_at: String
)

data class studentbankdetail(
    @SerializedName("sb_id")
    public var sb_id: String,

    @SerializedName("sb_student_id")
    public var sb_student_id: String,

    @SerializedName("sb_bank_name")
    public var sb_bank_name: String,

    @SerializedName("sb_acc_name")
    public var sb_acc_name: String,

    @SerializedName("sb_acc_no")
    public var sb_acc_no: String,

    @SerializedName("sb_ifsc")
    public var sb_ifsc: String,

    @SerializedName("sb_status")
    public var sb_status: String,

    @SerializedName("sb_added_by")
    public var sb_added_by: String?,

    @SerializedName("sb_updated_by")
    public var sb_updated_by: String?,

    @SerializedName("sb_created_at")
    public var sb_created_at: String,

    @SerializedName("sb_updated_at")
    public var sb_updated_at: String,

    @SerializedName("sb_passbook")
    public var sb_passbook: String


)


