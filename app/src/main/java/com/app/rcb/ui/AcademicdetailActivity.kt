package com.app.rcb.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.app.rcb.R
import com.app.rcb.api.HttpPostMultipart
import com.app.rcb.util.Constant
import com.app.rcb.util.Constant.Companion.entranceproof
import com.app.rcb.util.Constant.Companion.objProfileResponse
import com.app.rcb.util.FileUtil
import com.app.rcb.util.LoadingScreen
import com.kaopiz.kprogresshud.KProgressHUD
import com.premierticketbookingapp.apicall.HttpTask
import com.shockwave.pdfium.PdfiumCore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_academic.*
import kotlinx.android.synthetic.main.layout_bank_detail.*
import kotlinx.android.synthetic.main.layout_guradian_detail.*
import kotlinx.android.synthetic.main.layout_personal_detail.*
import kotlinx.android.synthetic.main.row_academic_record.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class AcademicdetailActivity : AdapterView.OnItemSelectedListener, AppCompatActivity() {
    private lateinit var mMedium: Typeface
    private lateinit var mHelpLayout: RelativeLayout

    private var marrexampassed: ArrayList<String> = ArrayList()
    private var marrbankname: ArrayList<String> = ArrayList()

    private var marracademic: ArrayList<ViewGroup> = ArrayList()

    private lateinit var mRegular: Typeface
    private lateinit var msemibold: Typeface

    private val PICK_FILE_REQUEST = 100

    private lateinit var mFileUtil: FileUtil

    private lateinit var uri_path: Uri

    private lateinit var request_file: RequestBody

    private lateinit var multipartBody: MultipartBody.Part

    private lateinit var original_file: File

    private var displayName: String = ""

    private var imageUri: Uri? = null
    private lateinit var out: FileOutputStream
    private var fileextension: String = ""
    private var isuploadentranceproof = false
    private var isuploadacademicdocument = false

    private var splashsaveacademice: KProgressHUD? = null


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_academic)
        mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
//        tv_nationallevel.setTypeface(mRegular, Typeface.BOLD)
        tv_entrance_exam.setTypeface(mRegular, Typeface.BOLD)
        tv_qualifyingyear.setTypeface(mRegular, Typeface.BOLD)
        tv_attach_proof.setTypeface(mRegular, Typeface.BOLD)
        // Allow strict mode
        // Allow strict mode

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        mHelpLayout = findViewById(R.id.abt_header_layout)
        if (Constant.objProfileResponse.issubmitforreview == 1) {

            edt_entrance_exam.isEnabled = false
            edt_qualifyingyear.isEnabled = false


        }

        //  marrexampassed = ArrayList()

        marrbankname = ArrayList()
        marracademic = ArrayList()

        // Log.d("etrance_file", objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof)

        if (!objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.isNullOrEmpty()) {
            // if (objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.length>0) {

            Constant.student_entranceproof =
                objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof

            if (!objProfileResponse.studentnationallevelentrancequalify.student_entrance_examination.isNullOrEmpty()) {
                Constant.student_entranceexam =
                    objProfileResponse.studentnationallevelentrancequalify.student_entrance_examination

            }
            // edt_entrance_exam.setText(Constant.student_entranceproof)

            Picasso
                .get()
                .load(entranceproof + objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof)
                .resize(200, 200)
                .noFade()
                .into(img_attach_proof);

        }
        createacademicdatalayout()


        //  setexampassedspinner()
        mHelpLayout.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
        }




        btnviewcertificate.setOnClickListener {

            val builder: AlertDialog.Builder = AlertDialog.Builder(PersonalDetailActivity@ this)
            val inflater: LayoutInflater = PersonalDetailActivity@ this!!.getLayoutInflater()
            val dialogView: View = inflater.inflate(R.layout.activity_img_dialog, null)
            var studentnationallevelentrancequalify =
                Constant.objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.substring(
                    Constant.objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.lastIndexOf(
                        "."
                    )
                );
            val imgview: ImageView = dialogView.findViewById(R.id.cc_image)

            if (studentnationallevelentrancequalify.contains("png") || studentnationallevelentrancequalify.contains(
                    "jpg"
                )
            ) {
                val builder: AlertDialog.Builder =
                    AlertDialog.Builder(AcademicdetailActivity@ this)
                val inflater: LayoutInflater =
                    AcademicdetailActivity@ this!!.getLayoutInflater()
                val dialogView: View = inflater.inflate(R.layout.activity_img_dialog, null)
                val imgview: ImageView = dialogView.findViewById(R.id.cc_image)
                if (Constant.objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.length > 0) {
                    Picasso
                        .get()
                        .load(Constant.entranceproof + Constant.objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof)
                        .resize(500, 500)
                        .noFade()
                        .into(imgview);
                }


                builder.setView(dialogView)
                    .setPositiveButton(
                        "ok",
                        DialogInterface.OnClickListener { dialog, which -> })
                    .setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, which -> })
                    .create().show()

            } else if (studentnationallevelentrancequalify.contains("pdf")) {
                /* val url = "https://docs.google.com/file/d/"+Constant.objProfileResponse.studentcharactercertificate    val i = Intent(Intent.ACTION_VIEW)
                 i.data = Uri.parse(url)
                 startActivity(i)*/
                val mainIntent =
                    Intent(this@AcademicdetailActivity, ViewPdfActivity::class.java)
                mainIntent.putExtra(
                    "docname",
                    Constant.entranceproof + Constant.objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof
                )

                startActivity(mainIntent)


            }else{
                Toast.makeText(this,"No Certificate Available!!",Toast.LENGTH_SHORT).show()
            }
        }


        btnuploadcertificate.setOnClickListener {

            mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
            isuploadentranceproof = true;
            isuploadacademicdocument = false;
            val new_dialog = Dialog(this@AcademicdetailActivity)
            new_dialog.setContentView(R.layout.custom_upload_dialog)
            new_dialog.setCancelable(false)
            val dismiss_layout: RelativeLayout = new_dialog.findViewById(R.id.dismiss_layout)
            val dismiss_text: TextView = new_dialog.findViewById(R.id.dismiss_txt)
            dismiss_layout.visibility = View.VISIBLE
            dismiss_text.setTypeface(mRegular)
            dismiss_layout.setOnClickListener {
                new_dialog.dismiss()
            }
            val current_location: RelativeLayout = new_dialog.findViewById(R.id.current_layout)
            val pick_text: TextView = new_dialog.findViewById(R.id.pickup_layout_text)
            pick_text.setText("IMAGE")
            pick_text.setTypeface(mRegular)
            current_location.setOnClickListener {

                // takePicture(this@UploadImageActivity)
                fileextension = "png";
                fromgallery()
                new_dialog.dismiss()
            }
            val pick_location: RelativeLayout = new_dialog.findViewById(R.id.pickp_layout)
            val drop_text: TextView = new_dialog.findViewById(R.id.drop_off_layout_text)
            drop_text.setTypeface(mRegular)
            drop_text.setText("DOCUMENT")
            pick_location.setOnClickListener {


                fileextension = "pdf";
                val newIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                newIntent.setType("application/pdf")
                startActivityForResult(newIntent, PICK_FILE_REQUEST)

                new_dialog.dismiss()

            }
            val param = WindowManager.LayoutParams()
            param.width = WindowManager.LayoutParams.MATCH_PARENT
            param.height = 750

            new_dialog.window!!.attributes = param
            new_dialog.window!!.attributes.windowAnimations = R.style.Animation
            new_dialog.show()


        }




        btnsave.setOnClickListener {

            if (!LoadingScreen.isNetworkAvailable(AcademicdetailActivity@ this)) {
                Toast.makeText(
                    AcademicdetailActivity@ this,
                    "Internet Connectivity Issue",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                splashsaveacademice = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading..")
                    .setDetailsLabel("RCB")
                    .show();

                if (edt_entrance_exam.text.length > 0) {
                    Constant.student_entranceexam = edt_entrance_exam.text.toString()
                }
                val jsonupdateprofiledetail = JSONObject()
                jsonupdateprofiledetail.put("methodname", "updatepersonaldetail")
                jsonupdateprofiledetail.put("student_id", objProfileResponse.studentid)
                jsonupdateprofiledetail.put("dob", objProfileResponse.studentdob)
                jsonupdateprofiledetail.put("gender", objProfileResponse.studentgenderid)
                jsonupdateprofiledetail.put("mobileno", objProfileResponse.studentphone)
                jsonupdateprofiledetail.put("institution_id", objProfileResponse.stdinstituteid)
                jsonupdateprofiledetail.put("program_id", objProfileResponse.stdprgname)
                jsonupdateprofiledetail.put("academic_year_id", objProfileResponse.stdassyear)
                jsonupdateprofiledetail.put("student_batch_id", objProfileResponse.stdbranch)
                jsonupdateprofiledetail.put(
                    "student_session_id",
                    objProfileResponse.studentsessionid
                )
                jsonupdateprofiledetail.put("student_seasons", objProfileResponse.studentSeasons)
                jsonupdateprofiledetail.put(
                    "student_maritial_status",
                    objProfileResponse.studentmaritalstatus
                )
                jsonupdateprofiledetail.put("student_email", objProfileResponse.studentemail)
                jsonupdateprofiledetail.put("firstname", objProfileResponse.firstname)
                jsonupdateprofiledetail.put("middlename", objProfileResponse.middlename)
                jsonupdateprofiledetail.put("lastname", objProfileResponse.lastname)
                jsonupdateprofiledetail.put(
                    "student_specialization",
                    objProfileResponse.studentspecialization
                )

                jsonupdateprofiledetail.put(
                    "student_character_certificate",
                    Constant.student_char_certificate
                )
                jsonupdateprofiledetail.put("student_signature", Constant.student_signature)
                jsonupdateprofiledetail.put(
                    "student_entrance_qualifying_year",
                    edt_qualifyingyear.text.toString()
                )
                jsonupdateprofiledetail.put(
                    "student_entrance_examination",
                    Constant.student_entranceexam.toString()
                )
                //  jsonupdateprofiledetail.put("student_entrance_proof",   Constant.entranceproof.toString())
                jsonupdateprofiledetail.put(
                    "student_entrance_proof",
                    Constant.student_entranceproof.toString()
                )
                jsonupdateprofiledetail.put("student_photo", Constant.student_photo.toString())

                Log.d("jsongetprofiledetail", jsonupdateprofiledetail.toString())


                HttpTask {
                    ""
                    if (it == null) {
                        Log.d("connection error", "Some thing Went Wrong")

                        return@HttpTask
                    }
                    try {


                        var updatejsonobj = JSONObject(it.toString())
                        Log.d("respond", updatejsonobj.toString())

                        var status = updatejsonobj.getString("status");
                        var message = updatejsonobj.getString("message");
//                var otp = eventjsonobj.getString("otp");
                        //   var studentid = eventjsonobj.getString("studentid");

                        if (status == "1") {
                            //  Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()


                            /* val toast =   Toast.makeText(loginrcbActivity@ this,
                                    Html.fromHtml("<font color='#e3f2fd' ><b>" + message + "</b></font>"), Toast.LENGTH_LONG).show()
                                val view: View = toast.getView()*/

                            /*   val toast: Toast = Toast.makeText(
                                   loginrcbActivity@ this,
                                   Html.fromHtml("<font color='#008000' ><b>" + message + "</b></font>"),      Toast.LENGTH_LONG
                               )
                               toast.view!!.setBackgroundColor(Color.parseColor("#F6AE2D"))
                               toast.show()*/

                            //To change the Background of Toast

                            //To change the Background of Toast
                            // view.setBackgroundColor(Color.TRANSPARENT)
                            // toast(message)
                            //  splashsaveacademice!!.dismiss()
                            // finish()
                            updategurdiandetail()
                        } else {
                            /* val toast: Toast = Toast.makeText(
                                 loginrcbActivity@ this,
                                 Html.fromHtml("<font color='#008000' ><b>" + message + "</b></font>"),      Toast.LENGTH_LONG
                             )
                             //   toast.view!!.setBackgroundColor(Color.parseColor("#F6AE2D"))
                             toast.view!!.setBackgroundColor(Color.TRANSPARENT)
                             toast.show()*/
                            toast(message)
                            splashsaveacademice!!.dismiss()

                        }


                    } catch (e: JSONException) {
                        println("Exception caught");
                        Log.d("exception", e.toString())
                        splashsaveacademice!!.dismiss()
                        /*    val toast: Toast = Toast.makeText(
                                loginrcbActivity@ this,
                                Html.fromHtml("<font color='#FF0000' ><b>" + "Something went wrong, please try again" + "</b></font>"),      Toast.LENGTH_LONG
                            )
                            toast.view!!.setBackgroundColor(Color.TRANSPARENT)
                            toast.show()*/

                        toast("Something went wrong, please try again")
                    }


                }.execute(
                    "POST",
                    Constant.url + "student",
                    jsonupdateprofiledetail.toString().trim()
                )


            }


        }


        btnsubmit.setOnClickListener {

            if (!LoadingScreen.isNetworkAvailable(AcademicdetailActivity@ this)) {
                Toast.makeText(
                    AcademicdetailActivity@ this,
                    "Internet Connectivity Issue",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                submitforreview()
            }

        }

    }


    @SuppressLint("LongLogTag")
    private fun updategurdiandetail() {


        val jsonupdateguradianedetail = JSONObject()
        jsonupdateguradianedetail.put("methodname", "updateguardiandetail")
        jsonupdateguradianedetail.put(
            "caddress_state",
            Constant.objProfileResponse.studentaddress.get(1).addressstate
        )
        jsonupdateguradianedetail.put(
            "student_domicile_country",
            Constant.objProfileResponse.guradiandetail.student_domicile_country
        )
        jsonupdateguradianedetail.put(
            "student_emergency_contact",
            Constant.objProfileResponse.studentemergencyno
        )
        jsonupdateguradianedetail.put("student_id", Constant.objProfileResponse.studentid)
        jsonupdateguradianedetail.put(
            "student_category",
            Constant.objProfileResponse.guradiandetail.student_category
        )
        jsonupdateguradianedetail.put(
            "student_mother_name",
            Constant.objProfileResponse.guradiandetail.student_mother_name
        )
        jsonupdateguradianedetail.put(
            "paddress_line",
            Constant.objProfileResponse.studentaddress.get(0).addressline
        )
        jsonupdateguradianedetail.put(
            "student_domicile_state",
            Constant.objProfileResponse.guradiandetail.student_domicile_state
        )
        jsonupdateguradianedetail.put(
            "paddress_state",
            Constant.objProfileResponse.studentaddress.get(0).addressstate
        )
        jsonupdateguradianedetail.put(
            "student_adhar_number",
            Constant.objProfileResponse.studentadharno
        )
        jsonupdateguradianedetail.put(
            "paddress_id",
            Constant.objProfileResponse.studentaddress.get(0).address_id
        )
        jsonupdateguradianedetail.put(
            "caddress_id",
            Constant.objProfileResponse.studentaddress.get(1).address_id
        )
        jsonupdateguradianedetail.put(
            "student_father_name",
            Constant.objProfileResponse.guradiandetail.student_father_name
        )
        jsonupdateguradianedetail.put(
            "student_blood_group",
            Constant.objProfileResponse.studentbloodgroup
        )
        jsonupdateguradianedetail.put(
            "student_relation",
            Constant.objProfileResponse.studentrelation
        )
        jsonupdateguradianedetail.put(
            "paddress_pincode",
            Constant.objProfileResponse.studentaddress.get(0).address_pincode
        )
        jsonupdateguradianedetail.put(
            "caddress_pincode",
            Constant.objProfileResponse.studentaddress.get(0).address_pincode
        )
        jsonupdateguradianedetail.put(
            "student_nationality",
            Constant.objProfileResponse.guradiandetail.student_nationality
        )
        jsonupdateguradianedetail.put(
            "caddress_line",
            Constant.objProfileResponse.studentaddress.get(1).addressline
        )
        jsonupdateguradianedetail.put(
            "student_passport_no",
            Constant.objProfileResponse.studentpassportno
        )
        jsonupdateguradianedetail.put(
            "caddress_pincode",
            Constant.objProfileResponse.studentaddress.get(1).address_pincode
        )
        Log.d("jsonupdateguradianedetail", jsonupdateguradianedetail.toString())

        HttpTask {
            ""
            if (it == null) {
                Log.d("connection error", "Some thing Went Wrong")

                return@HttpTask
            }
            try {


                var updatejsonobj = JSONObject(it.toString())
                Log.d("respond", updatejsonobj.toString())

                var status = updatejsonobj.getString("status");
                var message = updatejsonobj.getString("message");
//                var otp = eventjsonobj.getString("otp");
                var studentid = updatejsonobj.getString("studentid");

                if (status == "1") {
                    toast(message)
                    splashsaveacademice!!.dismiss()
                    //  finish()
                    //  startActivity(Intent(this@AcademicdetailActivity, DashBoardActivity::class.java))
                    val mainintent =
                        Intent(this@AcademicdetailActivity, DashBoardActivity::class.java)
                    mainintent.putExtra("studentid", studentid)
                    startActivity(mainintent)
                } else {
                    /* val toast: Toast = Toast.makeText(
                         loginrcbActivity@ this,
                         Html.fromHtml("<font color='#008000' ><b>" + message + "</b></font>"),      Toast.LENGTH_LONG
                     )
                     //   toast.view!!.setBackgroundColor(Color.parseColor("#F6AE2D"))
                     toast.view!!.setBackgroundColor(Color.TRANSPARENT)
                     toast.show()*/
                    toast(message)
                    splashsaveacademice!!.dismiss()

                }


            } catch (e: JSONException) {
                println("Exception caught");
                Log.d("exception", e.toString())
                splashsaveacademice!!.dismiss()
                /*    val toast: Toast = Toast.makeText(
                        loginrcbActivity@ this,
                        Html.fromHtml("<font color='#FF0000' ><b>" + "Something went wrong, please try again" + "</b></font>"),      Toast.LENGTH_LONG
                    )
                    toast.view!!.setBackgroundColor(Color.TRANSPARENT)
                    toast.show()*/

                toast("Something went wrong, please try again")
            }


        }.execute(
            "POST",
            Constant.url + "student",
            jsonupdateguradianedetail.toString().trim()
        )


    }


    fun toast(message: String?) {
        val toast = Toast(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.custom_toast, null)
        val textView = view.findViewById(R.id.custom_toast_text) as TextView
        textView.text = message
        toast.view = view
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    private var filetype: String = ""
    private var encImage: String = ""
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                try {
                    mFileUtil = FileUtil()
                    uri_path = Uri.parse(data!!.data.toString())

                    val path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS
                    )
                    var file = File(uri_path!!.path.toString())
                    var file_name = file.name
                    Constant.original_file = File(
                        mFileUtil.getPath(
                            uri_path,
                            this@AcademicdetailActivity
                        )
                    )
                    path.mkdirs()
                    Log.d("RealPath", Constant.original_file.toString())

                    fileextension = FileUtil.getMimeType(PersonalDetailActivity@ this, uri_path)
                    val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")

                    if (isuploadacademicdocument) {
                        filetype = "academicdocument"
                        file_name = "acad" + df.format(Date()).toString() + "." + fileextension
                        Constant.student_academic = file_name
                    } else if (isuploadentranceproof) {
                        filetype = "entranceproof"
                        file_name = "ent" + df.format(Date()).toString() + "." + fileextension
                        Constant.student_entranceproof = file_name
                    }

                    request_file =
                        RequestBody.create(
                            "application/pdf".toMediaTypeOrNull(),
                            Constant.original_file
                        )


                    /* path.mkdir()
                     file.createNewFile()*/
                    multipartBody = MultipartBody.Part.createFormData(
                        "filenames",
                        file_name,
                        request_file
                    )
                    Log.d("Multipart", multipartBody.body.contentLength().toString())


                    if (uri_path.toString().startsWith("content://")) {
                        var cursor: Cursor? = null;

                        try {
                            cursor = getContentResolver().query(uri_path, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName =
                                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                // doc_name.setText(displayName)

                                //if (original_file.toString().contains("pdf")) {

                                // generateImageFromPdf(uri_path)
                                //  }
                                encImage = FileUtil.getStringFile(
                                    AcademicdetailActivity@ this,
                                    Constant.original_file
                                )

                            }
                        } finally {
                            cursor!!.close();
                        }
                    } else if (uri_path.toString().startsWith("file://")) {

                        displayName = file.getName()
                        //  mDocumentName = file.getName()
                        //  doc_name.setText(displayName)
                        // generateImageFromPdf(uri_path)
                        encImage = FileUtil.getStringFile(
                            AcademicdetailActivity@ this,
                            Constant.original_file
                        )

                    }

                    UploadDocacademic(
                        this@AcademicdetailActivity,
                        "uploadimagenew",
                        file_name,
                        encImage,
                        Constant.accesstoken,
                        fileextension,
                        filetype
                    ).execute(Constant.url + "student")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == 1) {

                imageUri = data?.data

                pdf_img.setImageURI(imageUri)
                mFileUtil = FileUtil()

                var file = File(imageUri!!.path.toString())
                var file_name = file.name
                val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")

                if (isuploadacademicdocument) {
                    filetype = "academicdocument"
                    file_name = "acad" + df.format(Date()).toString() + "." + fileextension
                    Constant.student_academic = file_name
                } else if (isuploadentranceproof) {
                    filetype = "entranceproof"
                    file_name = "ent" + df.format(Date()).toString() + "." + fileextension
                    Constant.student_entranceproof = file_name
                }





                Constant.original_file = File(
                    mFileUtil.getPath(
                        imageUri!!,
                        this@AcademicdetailActivity
                    )
                )
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                )

                displayName = file.getName()

                img_layout_relative.visibility = View.VISIBLE

                path.mkdirs()
                Log.d("RealPath", Constant.original_file.toString())

                request_file =
                    RequestBody.create("Image/jpeg".toMediaTypeOrNull(), Constant.original_file)


                /* path.mkdir()
                 file.createNewFile()*/
                multipartBody = MultipartBody.Part.createFormData(
                    "filenames",
                    file_name,
                    request_file
                )
                Log.d("Multipart", multipartBody.body.contentLength().toString())


                tvdeleteicon_acad.setOnClickListener {

                    val jsondeletefile = JSONObject()


                    jsondeletefile.put("methodname", "delete")
                    jsondeletefile.put("filename", file_name)
                    jsondeletefile.put("type", "academicdocument")

                    Log.d("jsonevent", jsondeletefile.toString())


                    HttpTask {
                        ""
                        if (it == null) {
                            Log.d("connection error", "Some thing Went Wrong")

                            return@HttpTask
                        }
                        try {


                            var eventjsonobj = JSONObject(it.toString())
                            Log.d("respond", eventjsonobj.toString())

                            var status = eventjsonobj.getString("status");
                            var message = eventjsonobj.getString("message");
//                var otp = eventjsonobj.getString("otp");
                            //   var studentid = eventjsonobj.getString("studentid");

                            if (status == "1") {
                                pdf_img.setImageDrawable(null)
                                tvdeleteicon_acad.visibility = View.GONE
                                Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                    .show()
                            }


                        } catch (e: JSONException) {
                            println("Exception caught");
                        }


                    }.execute("POST", Constant.url + "student", jsondeletefile.toString().trim())

                }

                // uploadfilenew()

                encImage =
                    FileUtil.getStringFile(AcademicdetailActivity@ this, Constant.original_file)
                UploadDocacademic(
                    this@AcademicdetailActivity,
                    "uploadimagenew",
                    file_name,
                    encImage,
                    Constant.accesstoken,
                    fileextension,
                    filetype
                ).execute(Constant.url + "student")
            }
        }
    }


    fun generateImageFromPdf(pdfUri: Uri) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this@AcademicdetailActivity)

        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            val fd = getContentResolver().openFileDescriptor(pdfUri, "r")
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
            pdf_img.setImageBitmap(bmp)
            /* document_recycler.visibility = View.VISIBLE
             document_recycler.layoutManager = LinearLayoutManager(this@NewDocumentActivity)
             document_recycler.adapter = DocLayoutAdapter(this@NewDocumentActivity,mDocumentList)*/
            // saveImage(bmp)
            pdfiumCore.closeDocument(pdfDocument) // important!
            uploadfilenew()
            //    someTask(AcademicdetailActivity@ this).execute()
        } catch (e: Exception) {
            //todo with exception
            e.printStackTrace()
        }
    }


    private fun saveImage(bmp: Bitmap) {

        try {
            val folder = File(uri_path.path)

            if (!folder.exists())
                folder.mkdirs()
            val file = File(folder, "PDF.png")
            out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            //  uploadDocument("certificate")
            //   uploadMultiFile()
            uploadfilenew()
        } catch (e: Exception) {
            //todo with exception
            e.printStackTrace()
        } finally {
            try {
                if (out != null)
                    out.close()
            } catch (e: Exception) {
                //todo with exception
                e.printStackTrace()
            }
        }
    }


    fun fromgallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 1)
    }


    private fun setexampassedspinner() {

        // student Exam Selection
        for (i in 0..objProfileResponse.studentacademicrecord.size - 1) {
            //   println("Loop: $n")

            marrexampassed.add(objProfileResponse.studentacademicrecord.get(i).sa_name)
        }


        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrexampassed)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //   spinexampassed!!.setAdapter(aa)

        //   spinexampassed!!.setOnItemSelectedListener(this)


        //Student Entrance Exam Selection

        // for (i in 0.. objProfileResponse.studentnationallevelentrancequalify)


        //student bank Name Selection
        for (i in 0..objProfileResponse.studentbankdetails.size - 1) {

            marrbankname.add(objProfileResponse.studentbankdetails.get(i).sb_bank_name)
        }

        val arrbankname = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrbankname)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //   spinbankname!!.setAdapter(arrbankname)

        //    spinbankname!!.setOnItemSelectedListener(this)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


        /*  if (parent!!.id == R.id.spinexampassed) {
              // first spinner selected
              edt_university.setText(objProfileResponse.studentacademicrecord.get(position).sa_university)
              edt_passingyear.setText(objProfileResponse.studentacademicrecord.get(position).sa_year)
              edt_percentagemark.setText(objProfileResponse.studentacademicrecord.get(position).sa_marks)
              edt_uploaddoc.setText(objProfileResponse.studentacademicrecord.get(position).sa_document)

          }*/
        /*   if (parent!!.id == R.id.spinbankname) {
          // second spinner selected
       //   edt_accountname.setText(objProfileResponse.studentbankdetails.get(position).sb_acc_name)
       //   edt_accountno.setText(objProfileResponse.studentbankdetails.get(position).sb_acc_no)
       //   edt_ifsc.setText(objProfileResponse.studentbankdetails.get(position).sb_ifsc)

      }*/


    }


    private fun createacademicdatalayout() {

        var tvqualificationval: TextView
        var tvinstituteval: TextView
        var boardval: TextView
        var tvsubjectval: TextView
        var tvpassingyearval: TextView
        var tvmarkspercentageval: TextView
        var tvcgpaval: TextView

        var tvviewdocicon: TextView
        var tvivewuploadicon: TextView
        var tvdeletevalthree: TextView

        lateinit var typefacefontawesome: Typeface


        val llayoutsusectionview = findViewById<LinearLayout>(R.id.llacademicdata)

        val layoutInflater: LayoutInflater = LayoutInflater.from(ScheduledDemoActivity@ this)


        for (i in 0..objProfileResponse.studentacademicrecord.size - 1) {

            val vwacademic: ViewGroup =
                layoutInflater.inflate(R.layout.row_academic_record, null, false) as ViewGroup

            tvqualificationval = vwacademic.findViewById(R.id.tvqualificationval) as TextView

            tvinstituteval = vwacademic.findViewById(R.id.tvinstituteval) as TextView

            boardval = vwacademic.findViewById(R.id.boardval) as TextView

            tvsubjectval = vwacademic.findViewById(R.id.tvsubjectval) as TextView

            tvpassingyearval = vwacademic.findViewById(R.id.tvpassingyearval) as TextView

            tvmarkspercentageval = vwacademic.findViewById(R.id.tvmarkspercentageval) as TextView

            tvcgpaval = vwacademic.findViewById(R.id.tvcgpaval) as TextView

            tvviewdocicon = vwacademic.findViewById(R.id.tvdeletevalone) as TextView

            tvivewuploadicon = vwacademic.findViewById(R.id.tvdeletevaltwo) as TextView

            tvdeletevalthree = vwacademic.findViewById(R.id.tvdeletevalthree) as TextView

            tvqualificationval.setText(objProfileResponse.studentacademicrecord.get(i).sa_name)

            tvinstituteval.setText(objProfileResponse.studentacademicrecord.get(i).sa_institute)

            boardval.setText(objProfileResponse.studentacademicrecord.get(i).sa_university)

            tvsubjectval.setText(objProfileResponse.studentacademicrecord.get(i).sa_subject)

            tvpassingyearval.setText(objProfileResponse.studentacademicrecord.get(i).saMonth+ '/' +objProfileResponse.studentacademicrecord.get(i).sa_year)

            tvmarkspercentageval.setText(objProfileResponse.studentacademicrecord.get(i).sa_marks)

            tvcgpaval.setText(objProfileResponse.studentacademicrecord.get(i).sa_cgpa)


            marracademic.add(vwacademic)

            typefacefontawesome = Typeface.createFromAsset(
                applicationContext.assets,
                "fonts/FontAwesome.ttf"
            )

            tvviewdocicon.setText("\uf044")
            tvviewdocicon.setTypeface(typefacefontawesome)


        //    tvivewuploadicon.setText("\uf06e")
            tvivewuploadicon.setTypeface(typefacefontawesome)


            tvdeletevalthree.setText("\uf1f8")
            tvdeletevalthree.setTypeface(typefacefontawesome)

            llayoutsusectionview.addView(vwacademic)


            tvivewuploadicon.setOnClickListener {

                var docname = objProfileResponse.studentacademicrecord.get(i).sa_document


                if (!docname.isNullOrEmpty()) {
                    var ext = docname.split(".")

                    if (ext[1] == "pdf") {
                        Log.d("pdf", ext[1])

                        //   val pdf = "https://rcb.broadwayinfotech.net.au/beta/storage/uploads/users/academic_documents/" + docname
                        val pdf = Constant.academicurl + docname
                        val viewpdfintent =
                            Intent(this@AcademicdetailActivity, ViewPdfActivity::class.java)
                        viewpdfintent.putExtra("docname", pdf)
                        startActivity(viewpdfintent)
                        overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                    } else if (ext[1] == "png") {
                        Log.d("image", ext[1])
                    }
                } else {
                    Toast.makeText(
                        AcademicdetailActivity@ this,
                        "File not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            tvviewdocicon.setOnClickListener {

                showeditacademicdialog(
                    objProfileResponse.studentacademicrecord.get(i).sa_name,
                    objProfileResponse.studentacademicrecord.get(i).sa_institute,
                    objProfileResponse.studentacademicrecord.get(i).sa_university,
                    objProfileResponse.studentacademicrecord.get(i).sa_subject,
                    objProfileResponse.studentacademicrecord.get(i).saMonth+ '/' + objProfileResponse.studentacademicrecord.get(i).sa_year,
                    objProfileResponse.studentacademicrecord.get(i).sa_marks,
                    objProfileResponse.studentacademicrecord.get(i).sa_cgpa,
                    objProfileResponse.studentacademicrecord.get(i).sa_document,
                    i, 1
                )

            }


        }


        edt_entrance_exam.setText(objProfileResponse.studentnationallevelentrancequalify.student_entrance_examination)
        // edt_entrance_exam.setText(objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof)
        edt_qualifyingyear.setText(objProfileResponse.studentnationallevelentrancequalify.student_entrance_qualifying_year)
        // edt_attach_proof.setText(objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof)


        /*  btnaddacademic.setText("\uf067")
          btnaddacademic.setTypeface(typefacefontawesome)
          btnaddacademic.setOnClickListener {

              showeditacademicdialog(
                      "",
                      "",
                      "",
                      "",
                      "",
                      "",
                      "",
                      "",
                      0,
                      1
              )
          }*/
    }


    private lateinit var editdialog: Dialog


    private fun showeditacademicdialog(
        qualification: String,
        institute: String,
        board: String,
        subject: String,
        passingyear: String,
        percentage: String,
        cgpa: String,
        docname: String,
        pst: Int,
        mode: Int
    ) {

        var edtqualname: EditText
        var edtinstitute: EditText
        var edtboard: EditText
        var edtsubject: EditText
        var edtpassingyear: EditText
        var edtpercentage: EditText
        var edtcgpa: EditText
        var btnsaveacademic: Button
        var btnuploadacademic: Button
        var btncancelacademicdetail: Button
        var tvviewicon: TextView
        var tvdeleteicon: TextView
        var imgacademic: ImageView

        var llalreadyuploadedacademic: LinearLayout
        lateinit var typefacefontawesome: Typeface

        editdialog = Dialog(this@AcademicdetailActivity)
        editdialog.setContentView(R.layout.layout_edit_academic_record)

        edtqualname = editdialog.findViewById<EditText>(R.id.edtqualification)
        edtinstitute = editdialog.findViewById<EditText>(R.id.edtinstitute)
        edtboard = editdialog.findViewById<EditText>(R.id.edtboard)
        edtsubject = editdialog.findViewById<EditText>(R.id.edtsubject)
        edtpassingyear = editdialog.findViewById<EditText>(R.id.edtpassingyear)
        edtpercentage = editdialog.findViewById<EditText>(R.id.edtpercentage)
        edtcgpa = editdialog.findViewById<EditText>(R.id.edtcgpa)
        llalreadyuploadedacademic =
            editdialog.findViewById<LinearLayout>(R.id.llalreadyuploadedacademic)
        typefacefontawesome = Typeface.createFromAsset(
            applicationContext.assets,
            "fonts/FontAwesome.ttf"
        )

        tvviewicon = editdialog.findViewById<EditText>(R.id.tvviewicon)

        tvdeleteicon = editdialog.findViewById<EditText>(R.id.tvdeleteicon)

        imgacademic = editdialog.findViewById<ImageView>(R.id.imgacademic)
        // imgacademic.setTypeface(typefacefontawesome)
        //  imgacademic.setText("\uf1c1")

        /* Picasso
             .get()
             .load("https://rcb.broadwayinfotech.net.au/beta/storage/uploads/users/academic_documents/" +docname)
             .resize(200, 200)
             .noFade()
             .into(imgacademic);*/

        if (docname != null) {
            Picasso
                .get()
                .load(Constant.academicurl + docname)
                .resize(200, 200)
                .noFade()
                .into(imgacademic);
        }



       // tvviewicon.setText("\uf06e")
        tvviewicon.setTypeface(typefacefontawesome)

        tvviewicon.setOnClickListener {

            if (docname != "") {
                var ext = docname.split(".")

                if (ext[1] == "pdf") {
                    Log.d("pdf", ext[1])

                    val pdf = Constant.academicurl + docname
                    val viewpdfintent =
                        Intent(this@AcademicdetailActivity, ViewPdfActivity::class.java)
                    viewpdfintent.putExtra("docname", pdf)
                    startActivity(viewpdfintent)
                    overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                } else if (ext[1] == "png") {
                    // Log.d("image", ext[1])
                    val viewimgintent =
                        Intent(this@AcademicdetailActivity, ViewImgActivity::class.java)
                    viewimgintent.putExtra("imgname", Constant.academicurl + docname)
                    startActivity(viewimgintent)
                    overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                }
            } else {
                Toast.makeText(
                    AcademicdetailActivity@ this,
                    "File not available",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        tvdeleteicon.setText("\uf1f8")
        tvdeleteicon.setTypeface(typefacefontawesome)

        tvdeleteicon.setOnClickListener {

            if (docname.length > 0) {

                val jsondeletefile = JSONObject()


                jsondeletefile.put("methodname", "delete")
                jsondeletefile.put("filename", docname)
                jsondeletefile.put("type", "academicdocument")

                Log.d("jsonevent", jsondeletefile.toString())
                HttpTask {
                    ""
                    if (it == null) {
                        Log.d("connection error", "Some thing Went Wrong")

                        return@HttpTask
                    }
                    try {


                        var eventjsonobj = JSONObject(it.toString())
                        Log.d("respond", eventjsonobj.toString())

                        var status = eventjsonobj.getString("status");
                        var message = eventjsonobj.getString("message");
//                var otp = eventjsonobj.getString("otp");
                        //   var studentid = eventjsonobj.getString("studentid");

                        if (status == "1") {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                .show()

                        }


                    } catch (e: JSONException) {
                        println("Exception caught");
                    }


                }.execute("POST", Constant.url + "student", jsondeletefile.toString().trim())
            } else {
                Toast.makeText(
                    AcademicdetailActivity@ this,
                    "File not available",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        edtqualname.setText(qualification)
        edtinstitute.setText(institute)
        edtboard.setText(board)
        edtsubject.setText(subject)
        edtpassingyear.setText(passingyear)
        edtpercentage.setText(percentage)
        edtcgpa.setText(cgpa)
        btnuploadacademic = editdialog.findViewById<Button>(R.id.btnuploadaacademic)

        if (Constant.objProfileResponse.issubmitforreview == 1) {

            edtqualname.isEnabled = false
            edtinstitute.isEnabled = false
            edtboard.isEnabled = false
            edtsubject.isEnabled = false
            edtpassingyear.isEnabled = false
            edtpercentage.isEnabled = false
            edtcgpa.isEnabled = false
            btnuploadacademic.visibility=View.GONE
            tvviewdocicon.visibility=View.GONE
            btnsave.visibility=View.GONE
            btnsubmit.visibility=View.GONE
        }else{
            btnuploadacademic.visibility=View.VISIBLE
            tvviewdocicon.visibility=View.VISIBLE
            btnsave.visibility=View.VISIBLE
            btnsubmit.visibility=View.VISIBLE
        }


        btnuploadacademic.setOnClickListener {

            mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
            isuploadacademicdocument = true;
            isuploadentranceproof = false;
            //  isuploadacademicdocument = false;


            val new_dialog = Dialog(this@AcademicdetailActivity)
            new_dialog.setContentView(R.layout.custom_upload_dialog)
            new_dialog.setCancelable(false)
            val dismiss_layout: RelativeLayout = new_dialog.findViewById(R.id.dismiss_layout)
            val dismiss_text: TextView = new_dialog.findViewById(R.id.dismiss_txt)
            dismiss_layout.visibility = View.VISIBLE
            dismiss_text.setTypeface(mRegular)
            dismiss_layout.setOnClickListener {
                new_dialog.dismiss()
            }
            val current_location: RelativeLayout = new_dialog.findViewById(R.id.current_layout)
            val pick_text: TextView = new_dialog.findViewById(R.id.pickup_layout_text)
            pick_text.setText("IMAGE")
            pick_text.setTypeface(mRegular)
            current_location.setOnClickListener {

                // takePicture(this@UploadImageActivity)
                fileextension = "png";
                fromgallery()
                new_dialog.dismiss()
            }
            val pick_location: RelativeLayout = new_dialog.findViewById(R.id.pickp_layout)
            val drop_text: TextView = new_dialog.findViewById(R.id.drop_off_layout_text)
            drop_text.setTypeface(mRegular)
            drop_text.setText("DOCUMENT")
            pick_location.setOnClickListener {


                fileextension = "pdf";
                val newIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                newIntent.setType("application/pdf")
                startActivityForResult(newIntent, PICK_FILE_REQUEST)

                new_dialog.dismiss()

            }
            val param = WindowManager.LayoutParams()
            param.width = WindowManager.LayoutParams.MATCH_PARENT
            param.height = 750

            new_dialog.window!!.attributes = param
            new_dialog.window!!.attributes.windowAnimations = R.style.Animation
            new_dialog.show()

        }


        btnsaveacademic = editdialog.findViewById<Button>(R.id.btnsaveacademic)

        btnsaveacademic.setOnClickListener {
            //call api to save bank detail
            update_academic(
                edtqualname.text.toString(), edtinstitute.text.toString(),
                edtboard.text.toString(),
                edtsubject.text.toString(),
                edtpassingyear.text.toString(),
                edtpercentage.text.toString(),
                edtcgpa.text.toString(), pst, editdialog, mode
            )

        }

        btncancelacademicdetail = editdialog.findViewById<Button>(R.id.btncancelacademicdetail)

        btncancelacademicdetail.setOnClickListener {

            editdialog.dismiss()
        }

        editdialog.show()

    }


    private fun update_academic(
        strqualname: String,
        strinstitute: String,
        strboard: String,
        strsubject: String,
        strpassyear: String,
        strpercentage: String,
        strcgpa: String,
        pst: Int,
        editdialog: Dialog,
        mode: Int
    ) {

        var tvqualificationval: TextView
        var tvinstituteval: TextView
        var boardval: TextView
        var tvsubjectval: TextView
        var tvpassingyearval: TextView
        var tvmarkspercentageval: TextView
        var tvcgpaval: TextView


        for (bk in 0..marracademic.size - 1) {

            if (bk == pst) {
                tvqualificationval =
                    marracademic.get(bk).findViewById(R.id.tvqualificationval) as TextView

                tvinstituteval = marracademic.get(bk).findViewById(R.id.tvinstituteval) as TextView

                boardval = marracademic.get(bk).findViewById(R.id.boardval) as TextView

                tvsubjectval = marracademic.get(bk).findViewById(R.id.tvsubjectval) as TextView

                tvpassingyearval =
                    marracademic.get(bk).findViewById(R.id.tvpassingyearval) as TextView

                tvmarkspercentageval =
                    marracademic.get(bk).findViewById(R.id.tvmarkspercentageval) as TextView

                tvcgpaval = marracademic.get(bk).findViewById(R.id.tvcgpaval) as TextView

                tvqualificationval.setText(strqualname)

                tvinstituteval.setText(strinstitute)

                boardval.setText(strboard)

                tvsubjectval.setText(strsubject)

                tvpassingyearval.setText(strpassyear)

                tvmarkspercentageval.setText(strpercentage)

                tvcgpaval.setText(strcgpa)
                objProfileResponse.studentacademicrecord.get(bk).sa_name = strqualname
                objProfileResponse.studentacademicrecord.get(bk).sa_institute = strinstitute
                objProfileResponse.studentacademicrecord.get(bk).sa_university = strboard
                objProfileResponse.studentacademicrecord.get(bk).sa_cgpa = strcgpa
                objProfileResponse.studentacademicrecord.get(bk).sa_marks = strpercentage
                objProfileResponse.studentacademicrecord.get(bk).sa_year = strpassyear
                objProfileResponse.studentacademicrecord.get(bk).sa_subject = strsubject


                if (Constant.student_academic.length > 0) {
                    objProfileResponse.studentacademicrecord.get(bk).sa_document =
                        Constant.student_academic
                }
                academicdetailapi(
                    objProfileResponse.studentacademicrecord.get(bk).sa_id,
                    strqualname,
                    strboard,
                    strinstitute,
                    strsubject,
                    strpassyear,
                    strpercentage,
                    strcgpa,
                    mode

                )
                editdialog.dismiss()
                break
            }

        }

    }


    private fun academicdetailapi(
        said: String, saname: String, sauniversity: String, sainstitute: String, sasubject: String,
        sayear: String, samark: String, sa_cgpa: String, mode: Int
    ) {

        if (!LoadingScreen.isNetworkAvailable(AcademicdetailActivity@ this)) {
            Toast.makeText(
                AcademicdetailActivity@ this,
                "Internet Connectivity Issue",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (mode == 1) {
                val jsonupdateacademicdetail = JSONObject()
                jsonupdateacademicdetail.put("methodname", "updateacademicdetail")
                jsonupdateacademicdetail.put("student_id", objProfileResponse.studentid)
                jsonupdateacademicdetail.put("sa_id", said)
                jsonupdateacademicdetail.put("sa_name", saname)
                jsonupdateacademicdetail.put("sa_university", sauniversity)
                jsonupdateacademicdetail.put("sa_institute", sainstitute)
                jsonupdateacademicdetail.put("sa_subject", sasubject)
                jsonupdateacademicdetail.put("sa_year", sayear)
                jsonupdateacademicdetail.put("sa_marks", samark)
                jsonupdateacademicdetail.put("sa_cgpa", sa_cgpa)
                jsonupdateacademicdetail.put("sa_document", Constant.student_academic)


                Log.d("jsonupdatebankdetail", jsonupdateacademicdetail.toString())


                HttpTask {
                    ""
                    if (it == null) {
                        Log.d("connection error", "Some thing Went Wrong")

                        return@HttpTask
                    }
                    try {


                        var updatejsonobj = JSONObject(it.toString())
                        Log.d("respond", updatejsonobj.toString())

                        var status = updatejsonobj.getString("status");
                        var message = updatejsonobj.getString("message");

                        if (status == "1") {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                .show()

                        } else {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                .show()

                        }


                    } catch (e: JSONException) {
                        println("Exception caught");
                    }


                }.execute(
                    "POST",
                    Constant.url + "student",
                    jsonupdateacademicdetail.toString().trim()
                )
            } else if (mode == 2) {
                val jsoncreateacademicdetail = JSONObject()
                jsoncreateacademicdetail.put("methodname", "createacademicdetail")
                jsoncreateacademicdetail.put("student_id", objProfileResponse.studentid)
                // jsonupdateacademicdetail.put("sa_id", said)
                jsoncreateacademicdetail.put("sa_name", saname)
                jsoncreateacademicdetail.put("sa_university", sauniversity)
                jsoncreateacademicdetail.put("sa_institute", sainstitute)
                jsoncreateacademicdetail.put("sa_subject", sasubject)
                jsoncreateacademicdetail.put("sa_year", sayear)
                jsoncreateacademicdetail.put("sa_marks", samark)
                jsoncreateacademicdetail.put("sa_cgpa", sa_cgpa)
                jsoncreateacademicdetail.put("sa_status", 1)
                jsoncreateacademicdetail.put("sa_document", Constant.student_academic)



                Log.d("jsoncreateacademic", jsoncreateacademicdetail.toString())


                HttpTask {
                    ""
                    if (it == null) {
                        Log.d("connection error", "Some thing Went Wrong")

                        return@HttpTask
                    }
                    try {


                        var updatejsonobj = JSONObject(it.toString())
                        Log.d("respond", updatejsonobj.toString())

                        var status = updatejsonobj.getString("status");
                        var message = updatejsonobj.getString("message");

                        if (status == "1") {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                .show()

                        } else {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG)
                                .show()

                        }


                    } catch (e: JSONException) {
                        println("Exception caught");
                    }


                }.execute(
                    "POST",
                    Constant.url + "student",
                    jsoncreateacademicdetail.toString().trim()
                )
            }
        }

    }


    private fun submitforreview() {
        if (!LoadingScreen.isNetworkAvailable(AcademicdetailActivity@ this)) {
            Toast.makeText(
                AcademicdetailActivity@ this,
                "Internet Connectivity Issue",
                Toast.LENGTH_LONG
            ).show()
        } else {

            val jsonupdateacademicdetail = JSONObject()
            jsonupdateacademicdetail.put("methodname", "submitforreview")
            jsonupdateacademicdetail.put("student_id", objProfileResponse.studentid)
            jsonupdateacademicdetail.put("issubmitted", 1)



            Log.d("jsonupdatebankdetail", jsonupdateacademicdetail.toString())


            HttpTask {
                ""
                if (it == null) {
                    Log.d("connection error", "Some thing Went Wrong")

                    return@HttpTask
                }
                try {


                    var updatejsonobj = JSONObject(it.toString())
                    Log.d("respond", updatejsonobj.toString())

                    var status = updatejsonobj.getString("status");
                    var message = updatejsonobj.getString("message");

                    if (status == "1") {
                        Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()

                    }


                } catch (e: JSONException) {
                    println("Exception caught");
                }


            }.execute("POST", Constant.url + "student", jsonupdateacademicdetail.toString().trim())
        }
    }


    private fun uploadfilenew() {

        try {
            // Set header
            //  val headers: Map<String, String> = HashMap()
            var headers: MutableMap<String?, String?> = mutableMapOf()

            headers.put(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36"
            )
            headers.put("Authorization", "Bearer " + Constant.accesstoken)

            val multipart = HttpPostMultipart(
                "https://rcb.broadwayinfotech.net.au/beta/api/student",
                "utf-8",
                headers
            )
            // Add form field
            // multipart.addFormField("type", "certificate")
            if (isuploadentranceproof) {
                multipart.addFormField("type", "entranceproof")
                multipart.addFormField("filename", Constant.student_entranceproof)
            } else if (isuploadacademicdocument) {
                multipart.addFormField("type", "academicdocument")
                multipart.addFormField("filename", Constant.student_academic)
            }

            multipart.addFormField("methodname", "upload")
            // Add file
            //  var file = File(imageUri!!.path.toString())
            multipart.addFilePart("file", Constant.original_file)
            // Print result
            val response: String = multipart.finish()
            println(response)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }


    internal class UploadDocacademic(
        ctx: Context,
        mname: String,
        imagename: String,
        encode_string: String?,
        maintoken: String?,
        fileext: String,
        doctype: String
    ) :
        AsyncTask<String, Void, String>() {

        val methodname: String = mname
        val Encode_String = encode_string
        val Main_token = maintoken
        val asyctx: Context = ctx
        private lateinit var progressuploading: ProgressDialog

        val fileextension = fileext
        val filename = imagename
        val dtype = doctype
        override fun onPreExecute() {
            progressuploading = ProgressDialog(asyctx)
            progressuploading.setTitle("RCB")
            progressuploading.setMessage("Uploading, please wait")
            progressuploading.show()

        }

        override fun doInBackground(vararg p0: String): String? {
            try {
                Log.d("Vicky", "encodedImage = $Encode_String")
                val random_num = kotlin.random.Random.nextInt((200 - 20) + 1) + 20

                var jsonObject = JSONObject()

                //isuploadcharcertificate = false
                //isuploadsignature = true
                //if(isuploadcharcertificate)
                jsonObject.put("filename", filename)
                //    jsonObject.put("fileextension",fileextension)
                jsonObject.put("methodname", methodname)
                jsonObject.put("doctype", dtype)
                if (fileextension.contains("jpg")) {
                    jsonObject.put("strbase64dta", "data:image/jpg;base64," + Encode_String)
                } else if (fileextension.contains("png")) {
                    jsonObject.put("strbase64dta", "data:image/png;base64," + Encode_String)
                } else if (fileextension.contains("pdf")) {
                    jsonObject.put("strbase64dta", "data:application/pdf;base64," + Encode_String)
                }


                val url = URL(Constant.url + "student")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoOutput(true)
                connection.setDoInput(true)
                connection.setRequestMethod("POST")
                connection.setFixedLengthStreamingMode(jsonObject.toString().toByteArray().size)
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + Main_token.toString().trim()
                )

                val out: OutputStream = BufferedOutputStream(connection.getOutputStream())
                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
                writer.write(jsonObject.toString())
                Log.d("RCB_data", "Data to laravel = $jsonObject.toString()")
                writer.flush()
                writer.close()
                out.close()
                connection.connect()
                val `in`: InputStream = BufferedInputStream(connection.getInputStream())
                val reader = BufferedReader(
                    InputStreamReader(
                        `in`, "UTF-8"
                    )
                )
                val sb = StringBuilder()
                var line: String? = null
                while (reader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }
                `in`.close()
                val result: String? = sb.toString()
                Log.d("RCB", "Response from php = $result")
                //Response = new JSONObject(result);
                connection.disconnect()
                return result.toString()
            } catch (e: java.lang.Exception) {
                Log.d("Exception_rcb", e.toString())
                e.printStackTrace()
            }
            return null

        }

        override fun onPostExecute(result: String?) {

            Log.d("imgresponse", result.toString())

            if (result.toString().contains("1", false)) {
                progressuploading.dismiss()
                val new_dialog = Dialog(asyctx)
                new_dialog.setContentView(R.layout.success_dialog)
                val success_text: TextView = new_dialog.findViewById(R.id.suceess_text)
                val mmedium: Typeface =
                    Typeface.createFromAsset(asyctx.assets, "montserrat/Montserrat-Medium.otf")
                val mRegular: Typeface =
                    Typeface.createFromAsset(asyctx.assets, "montserrat/Montserrat-Regular.otf")
                success_text.setTypeface(mRegular)
                if ((fileextension.contains("jpg")) || fileextension.contains("png")) {
                    success_text.text = "Image Uploaded successfully"
                } else if (fileextension.contains("pdf")) {
                    success_text.text = "Document Uploaded successfully"
                }
                val ok_bttn: AppCompatButton = new_dialog.findViewById(R.id.success_button)
                ok_bttn.setTypeface(mRegular)
                new_dialog.setCancelable(false)

                ok_bttn.setOnClickListener {
                    new_dialog.dismiss()

                }
                val param = WindowManager.LayoutParams()
                param.width = WindowManager.LayoutParams.MATCH_PARENT
                param.height = 520

                new_dialog.window!!.attributes = param
                new_dialog.window!!.attributes.windowAnimations = R.style.Animation
                new_dialog.show()
            } else {
                progressuploading.dismiss()
                Toast.makeText(asyctx, "Something went wrong", Toast.LENGTH_SHORT).show()
            }


        }
    }


}


