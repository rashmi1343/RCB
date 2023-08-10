package com.app.rcb.ui


import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.app.rcb.R
import com.app.rcb.api.HttpPostMultipart
import com.app.rcb.db.entity.*
import com.app.rcb.util.Constant
import com.app.rcb.util.Constant.Companion.objProfileResponse
import com.app.rcb.util.FileUtil
import com.app.rcb.util.FileUtil.Companion.showError
import com.app.rcb.viewmodel.rcbviewModel
import com.premierticketbookingapp.apicall.HttpTask
import com.shockwave.pdfium.PdfiumCore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_academic.*
import kotlinx.android.synthetic.main.layout_personal_detail.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PersonalDetailActivity : AdapterView.OnItemSelectedListener, AppCompatActivity() {

    private lateinit var mHelpLayout: RelativeLayout
    private lateinit var strfirstname: String
    private lateinit var strmiddlename: String
    private lateinit var strlastname: String
    private lateinit var strphone: String
    private lateinit var strdob: String
    private lateinit var stremail: String
    private lateinit var specialization: String
    private var marrcenter: ArrayList<String> = ArrayList()

    private var maincenterlist: List<Center> = ArrayList()
    private var programlist: List<Program> = ArrayList()
    private var academicyearlist: List<Ay> = ArrayList()
    private var branchlist: List<Branch> = ArrayList()
    private var seasonlist: List<Season> = ArrayList()
    private var ddlist: List<Dropdown> = ArrayList()
    private var speciallist: List<Specialization> = ArrayList()

    private var marrprogram: ArrayList<String> = ArrayList()


    private var marracademicyear: ArrayList<String> = ArrayList()

    private var marrbranch: ArrayList<String> = ArrayList()

    private var marrseason: ArrayList<String> = ArrayList()

    private var marrseasonduration: ArrayList<String> = ArrayList()

    private var marrgender: ArrayList<String> = ArrayList()

    private var arrmaritalstatus: ArrayList<String> = ArrayList()

    private var arrspecialization: ArrayList<String> = ArrayList()

    private lateinit var mRegular: Typeface
    private lateinit var mbold: Typeface

    private val PICK_FILE_REQUEST = 100

    private lateinit var mFileUtil: FileUtil


    private var displayName: String = ""

    private var fileextension: String = ""
    private var filetype: String = ""

    private var encImage: String = ""

    private var isuploadcharcertificate = false
    private var isuploadsignature = false
    private var isuploadphoto = false

    private var submitforreview: Int = 0

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_personal_detail)

        mHelpLayout = findViewById(R.id.abt_header_layout)

        try {
            mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
            mbold = Typeface.createFromAsset(assets, "montserrat/Montserrat-Bold.otf")

            rcbdbviewModel = ViewModelProviders.of(this).get(rcbviewModel::class.java)
            strfirstname = intent.getStringExtra("firstname").toString()
            strmiddlename = intent.getStringExtra("middlename").toString()
            strlastname = intent.getStringExtra("lastname").toString()
            strdob = intent.getStringExtra("dob").toString()
            strphone = intent.getStringExtra("phone").toString()
            stremail = intent.getStringExtra("email").toString()
            specialization = intent.getStringExtra("specialization").toString()





            edt_student_first_nme.setText(strfirstname)
            edt_student_middle_nme.setText(strmiddlename)
            edt_student_last_nme.setText(strlastname)

            edt_phone.setText(strphone)
            edt_dob.setText(strdob)
            edt_email.setText(stremail)

            edt_student_first_nme.setTypeface(mRegular)
            edt_student_middle_nme.setTypeface(mRegular)
            edt_student_last_nme.setTypeface(mRegular)

            edt_phone.setTypeface(mRegular)
            edt_dob.setTypeface(mRegular)
            edt_email.setTypeface(mRegular)

            tv_student_first_nme.setTypeface(mRegular, Typeface.BOLD)
            tv_studentsignature.setTypeface(mRegular, Typeface.BOLD)

            tv_student_middle_nme.setTypeface(mRegular, Typeface.BOLD)
            tv_charactercertificate.setTypeface(mRegular, Typeface.BOLD)
            tv_student_last_nme.setTypeface(mRegular, Typeface.BOLD)
            tv_photograph.setTypeface(mRegular, Typeface.BOLD)
            tv_email.setTypeface(mRegular, Typeface.BOLD)
            tv_phone.setTypeface(mRegular, Typeface.BOLD)
            tv_dob.setTypeface(mRegular, Typeface.BOLD)

            spinacademicyear!!.setSelection(0)
            btnuploadphoto.setTypeface(mRegular)
            btnuploadphoto.setOnClickListener {
                mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
                isuploadcharcertificate = false
                isuploadsignature = false
                isuploadphoto = true;
                val new_dialog = Dialog(this@PersonalDetailActivity)
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
                    //    fromgallery()
                    //  fordocument()
                    new_dialog.dismiss()

                }
                pick_location.visibility = View.GONE
                val param = WindowManager.LayoutParams()
                param.width = WindowManager.LayoutParams.MATCH_PARENT
                param.height = 750
                new_dialog.window!!.attributes = param
                new_dialog.window!!.attributes.windowAnimations = R.style.Animation
                new_dialog.show()

            }
            btnviewphoto.setTypeface(mRegular)
            btnviewphoto.setOnClickListener {
                //Log.d("view_Certifcate","view_Certificate")
                var studentphotofileextension = Constant.objProfileResponse.studentphoto.substring(
                    Constant.objProfileResponse.studentphoto.lastIndexOf(".")
                );

                if (studentphotofileextension.contains("png") || studentphotofileextension.contains(
                        "jpg"
                    )
                ) {
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(PersonalDetailActivity@ this)
                    val inflater: LayoutInflater =
                        PersonalDetailActivity@ this!!.getLayoutInflater()
                    val dialogView: View = inflater.inflate(R.layout.activity_img_dialog, null)
                    val imgview: ImageView = dialogView.findViewById(R.id.cc_image)
                    if (Constant.objProfileResponse.studentphoto.length > 0) {
                        /* Picasso
                             .get()
                             .load(Constant.ccertificateurl + Constant.objProfileResponse.studentcharactercertificate)
                             .resize(500, 500)
                             .noFade()
                             .into(imgview);*/
                        Picasso
                            .get()
                            .load(Constant.photourl + Constant.objProfileResponse.studentphoto)
                            .resize(200, 200)
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
                } else if (studentphotofileextension.contains("pdf")) {
                    /* val url = "https://docs.google.com/file/d/"+Constant.objProfileResponse.studentcharactercertificate         val i = Intent(Intent.ACTION_VIEW)
                     i.data = Uri.parse(url)
                     startActivity(i)*/
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://docs.google.com/viewer?url=" + Constant.photourl + Constant.objProfileResponse.studentphoto)
                    )
                    startActivity(browserIntent)
                }

            }


            btnviewchcertificate.setTypeface(mRegular)
            btnviewchcertificate.setOnClickListener {
                var studentccfileextension =
                    Constant.objProfileResponse.studentcharactercertificate.substring(
                        Constant.objProfileResponse.studentcharactercertificate.lastIndexOf(".")
                    );
                //Log.d("view_Certifcate","view_Certificate")
                if (studentccfileextension.contains("png") || studentccfileextension.contains("jpg")) {
                    val builder: AlertDialog.Builder =
                        AlertDialog.Builder(PersonalDetailActivity@ this)
                    val inflater: LayoutInflater =
                        PersonalDetailActivity@ this!!.getLayoutInflater()
                    val dialogView: View = inflater.inflate(R.layout.activity_img_dialog, null)
                    val imgview: ImageView = dialogView.findViewById(R.id.cc_image)
                    if (Constant.objProfileResponse.studentcharactercertificate.length > 0) {
                        Picasso
                            .get()
                            .load(Constant.ccertificateurl + Constant.objProfileResponse.studentcharactercertificate)
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

                } else if (studentccfileextension.contains("pdf")) {
                    /* val url = "https://docs.google.com/file/d/"+Constant.objProfileResponse.studentcharactercertificate    val i = Intent(Intent.ACTION_VIEW)
                     i.data = Uri.parse(url)
                     startActivity(i)*/
                    val mainIntent =
                        Intent(this@PersonalDetailActivity, ViewPdfActivity::class.java)
                    mainIntent.putExtra(
                        "docname",
                        Constant.ccertificateurl + Constant.objProfileResponse.studentcharactercertificate
                    )

                    startActivity(mainIntent)


                }
            }


            btnuploadchcertificate.setTypeface(mRegular)
            btnuploadchcertificate.setOnClickListener {

                mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
                isuploadcharcertificate = true
                isuploadsignature = false
                val new_dialog = Dialog(this@PersonalDetailActivity)
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
                    //    fromgallery()
                    //  fordocument()

                    new_dialog.dismiss()

                }
                val param = WindowManager.LayoutParams()
                param.width = WindowManager.LayoutParams.MATCH_PARENT
                param.height = 750

                new_dialog.window!!.attributes = param
                new_dialog.window!!.attributes.windowAnimations = R.style.Animation
                new_dialog.show()

            }

            btnviewsignature.setTypeface(mRegular)
            btnviewsignature.setOnClickListener {

                val builder: AlertDialog.Builder = AlertDialog.Builder(PersonalDetailActivity@ this)
                val inflater: LayoutInflater = PersonalDetailActivity@ this!!.getLayoutInflater()
                val dialogView: View = inflater.inflate(R.layout.activity_img_dialog, null)

                val imgview: ImageView = dialogView.findViewById(R.id.cc_image)


                if (Constant.objProfileResponse.studentsignature.length > 0) {
                    Picasso
                        .get()
                        .load(Constant.sigurl + Constant.objProfileResponse.studentsignature)
                        .resize(500, 500)
                        .noFade()
                        .into(imgview);
                }


                builder.setView(dialogView)
                    .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which -> })
                    .setNegativeButton(
                        "Cancel",
                        DialogInterface.OnClickListener { dialog, which -> })
                    .create().show()

            }

            btnuploadsignature.setTypeface(mRegular)
            btnuploadsignature.setOnClickListener {

                mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
                isuploadcharcertificate = false
                isuploadsignature = true
                val new_dialog = Dialog(this@PersonalDetailActivity)
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
                    //  fromgallery()
                    //fordocument()
                    // selectPDF()
                }
                val param = WindowManager.LayoutParams()
                param.width = WindowManager.LayoutParams.MATCH_PARENT
                param.height = 750

                new_dialog.window!!.attributes = param
                new_dialog.window!!.attributes.windowAnimations = R.style.Animation
                new_dialog.show()

            }


            mHelpLayout.setOnClickListener {
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
            }

            setinstitute()


            btngurdiandetail.setTypeface(mRegular)
            btngurdiandetail.setOnClickListener {


                objProfileResponse.firstname = edt_student_first_nme.text.toString()
                objProfileResponse.middlename = edt_student_middle_nme.text.toString()
                objProfileResponse.lastname = edt_student_last_nme.text.toString()
                objProfileResponse.studentdob = edt_dob.text.toString()
                objProfileResponse.studentemail = edt_email.text.toString()
                objProfileResponse.studentphone = edt_phone.text.toString()


                val guardianintent =
                    Intent(this@PersonalDetailActivity, GuardiandetailActivity::class.java)
                guardianintent.putExtra(
                    "fathername",
                    objProfileResponse.guradiandetail.student_father_name
                )
                guardianintent.putExtra(
                    "mothername",
                    objProfileResponse.guradiandetail.student_mother_name
                )
                guardianintent.putExtra(
                    "category",
                    objProfileResponse.guradiandetail.student_category
                )
                guardianintent.putExtra(
                    "nationality",
                    objProfileResponse.guradiandetail.student_nationality
                )
                guardianintent.putExtra(
                    "domicile",
                    objProfileResponse.guradiandetail.student_domicile_state
                )
                guardianintent.putExtra(
                    "permanentaddressline",
                    objProfileResponse.studentaddress.get(0).addressline
                )
                guardianintent.putExtra(
                    "permanentaddresslinepincode",
                    objProfileResponse.studentaddress.get(0).address_pincode
                )
                guardianintent.putExtra(
                    "correspondenceaddressline",
                    objProfileResponse.studentaddress.get(1).addressline
                )
                guardianintent.putExtra(
                    "correspondencepincode",
                    objProfileResponse.studentaddress.get(1).address_pincode
                )
                guardianintent.putExtra(
                    "state",
                    objProfileResponse.studentaddress.get(0).addressstate
                )
                guardianintent.putExtra(
                    "statecor",
                    objProfileResponse.studentaddress.get(1).addressstate
                )
                guardianintent.putExtra("emergencycontact", objProfileResponse.studentemergencyno)
                guardianintent.putExtra("relation", objProfileResponse.studentrelation)
                guardianintent.putExtra("adhaarno", objProfileResponse.studentadharno)
                guardianintent.putExtra("passportno", objProfileResponse.studentpassportno)
                startActivity(guardianintent)
                overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
            }
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }
    }


    var documentPick = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        try {

            mFileUtil = FileUtil()

            var file = File(uri!!.path.toString())
            // store in db
            var file_name = file.name;

            Constant.original_file = File(mFileUtil.getPath(uri!!, this@PersonalDetailActivity))
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            displayName = file.getName()
            path.mkdirs()
            Log.d("RealPath", Constant.original_file.toString())

            fileextension = FileUtil.getMimeType(PersonalDetailActivity@ this, uri!!)
            val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            if (isuploadcharcertificate) {
                filetype = "charactercertificate"
                file_name = "cc" + df.format(Date()).toString() + "." + fileextension
                pdf_img_cc.setImageURI(uri!!)
                Constant.student_char_certificate = file_name
                img_layout_relative_cc.visibility = View.VISIBLE
            } else if (isuploadsignature) {
                filetype = "studentsignature"
                file_name = "sign" + df.format(Date()).toString() + "." + fileextension
                pdf_img_sig.setImageURI(uri!!)
                Constant.student_signature = file_name
                img_layout_relative_sig.visibility = View.VISIBLE
            }


            if (uri!!.path.toString().startsWith("content://")) {
                var cursor: Cursor? = null;

                try {
                    cursor = getContentResolver().query(uri!!, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        encImage = FileUtil.getStringFile(
                            PersonalDetailActivity@ this,
                            Constant.original_file
                        )

                    }
                } finally {
                    cursor!!.close();
                }
            } else if (uri!!.path.toString().startsWith("file://")) {

                displayName = file.getName()
                encImage =
                    FileUtil.getStringFile(PersonalDetailActivity@ this, Constant.original_file)
            }

            UploadDoc(
                this@PersonalDetailActivity,
                "uploadimagenew",
                file_name,
                encImage,
                Constant.accesstoken,
                fileextension,
                filetype
            ).execute(Constant.url + "student")


        } catch (e: IOException) {
            showError(this, "Failed to read picture data!")
            e.printStackTrace()
        }
    }


    var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // Initialize result data
        try {

            mFileUtil = FileUtil()

            var file = File(uri!!.toString())
            // store in db
            var file_name = file.name;


            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            //displayName = file.getName()
            //path.mkdirs()
            // Log.d("RealPath", Constant.original_file.toString())

            fileextension = FileUtil.getMimeType(PersonalDetailActivity@ this, uri!!)
            val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            if (isuploadcharcertificate) {
                filetype = "charactercertificate"
                file_name = "cc" + df.format(Date()).toString() + "." + fileextension
                pdf_img_cc.setImageURI(uri!!)


                Constant.student_char_certificate = file_name
                img_layout_relative_cc.visibility = View.VISIBLE
                //  img_layout_relative_sig.visibility = View.GONE


            } else if (isuploadsignature) {
                filetype = "studentsignature"
                file_name = "sign" + df.format(Date()).toString() + "." + fileextension
                pdf_img_sig.setImageURI(uri!!)
                Constant.student_signature = file_name
                img_layout_relative_sig.visibility = View.VISIBLE
                //  img_layout_relative_cc.visibility = View.GONE


            }
            encImage = FileUtil.getStringFile(PersonalDetailActivity@ this, file)

            /* if (sPath.toString().startsWith("content://")) {
               var cursor: Cursor? = null;

               try {
                   cursor = getContentResolver().query(sUri, null, null, null, null);
                   if (cursor != null && cursor.moveToFirst()) {
                       displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                       // doc_name.setText(displayName)

                       //if (original_file.toString().contains("pdf")) {

                       //  generateImageFromPdf(uri_path)
                       //  }
                       //  val pdffile: File = File(getPath(uri_path))
                       encImage= FileUtil.getStringFile(PersonalDetailActivity@this,Constant.original_file)

                   }
               } finally {
                   cursor!!.close();
               }
           } else if (sPath.toString().startsWith("file://"))
           {

               displayName = file.getName()
               //  mDocumentName = file.getName()
               //  doc_name.setText(displayName)
               //generateImageFromPdf(uri_path)
               // val pdffile: File = File(getPath(uri_path))
               encImage=  FileUtil.getStringFile(PersonalDetailActivity@this,Constant.original_file)

           }*/

            UploadDoc(
                this@PersonalDetailActivity,
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
    }

    var mSelectedFileContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        try {
            Log.d("forimage", "forimagecalled")
            mFileUtil = FileUtil()

            var file = File(uri!!.path.toString())
            // store in db
            var file_name = file.name;

            Constant.original_file = File(mFileUtil.getPath(uri!!, this@PersonalDetailActivity))
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            displayName = file.getName()
            path.mkdirs()
            Log.d("RealPath", Constant.original_file.toString())

            fileextension = FileUtil.getMimeType(PersonalDetailActivity@ this, uri!!)
            val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            if (isuploadcharcertificate) {
                filetype = "charactercertificate"
                file_name = "cc" + df.format(Date()).toString() + "." + fileextension
                if (fileextension == "jpg" || fileextension == "png") {
                    pdf_img_cc.setImageURI(uri!!)
                    img_layout_relative_cc.visibility = View.VISIBLE
                }
                Constant.student_char_certificate = file_name

            } else if (isuploadsignature) {
                filetype = "studentsignature"
                file_name = "sign" + df.format(Date()).toString() + "." + fileextension
                if (fileextension == "jpg" || fileextension == "png") {
                    pdf_img_sig.setImageURI(uri!!)
                    img_layout_relative_sig.visibility = View.VISIBLE
                }
                Constant.student_signature = file_name

            } else if (isuploadphoto) {
                filetype = "profileimage"
                file_name = "pi_" + Constant.objProfileResponse.studentid + df.format(Date())
                    .toString() + "." + fileextension
                if (fileextension == "jpg" || fileextension == "png") {
                    // pdf_img_sig.setImageURI(uri!!)
                    //  val f = File(uri)
                    Picasso.get().load(uri).into(imgphoto);
                    img_layout_relative_sig.visibility = View.VISIBLE
                }
                Constant.student_photo = file_name
            }


            if (uri!!.path.toString().startsWith("content://")) {
                var cursor: Cursor? = null;

                try {
                    cursor = getContentResolver().query(uri!!, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        encImage = FileUtil.getStringFile(
                            PersonalDetailActivity@ this,
                            Constant.original_file
                        )

                    }
                } finally {
                    cursor!!.close();
                }
            } else if (uri!!.path.toString().startsWith("file://")) {

                displayName = file.getName()
                encImage =
                    FileUtil.getStringFile(PersonalDetailActivity@ this, Constant.original_file)
            } else {
                displayName = file.getName()
                encImage =
                    FileUtil.getStringFile(PersonalDetailActivity@ this, Constant.original_file)
            }

            UploadDoc(
                this@PersonalDetailActivity,
                "uploadimagenew",
                file_name,
                encImage,
                Constant.accesstoken,
                fileextension,
                filetype
            ).execute(Constant.url + "student")


        } catch (e: IOException) {
            showError(this, "Failed to read picture data!")
            e.printStackTrace()
        }

    }

    fun fromgallery() {
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, 1)
        //New Method
        mSelectedFileContent.launch("*/*")
        mSelectedFileContent.launch("image/*")
    }

    // var resultLauncher: ActivityResultLauncher<Intent>? = null
    fun selectPDF() {
        // Initialize intent
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // set type
        //      intent.type = "application/pdf"
        // Launch intent
        if ((ActivityCompat.checkSelfPermission(
                this@PersonalDetailActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            // When permission is not granted
            // Result permission
            ActivityCompat.requestPermissions(
                this@PersonalDetailActivity, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                1
            )
        } else {
            // When permission is granted
            // Create method
            //   selectPDF();
            resultLauncher!!.launch("*/*")
        }
    }


    fun fordocument() {

        documentPick.launch(
            arrayOf(
                "application/pdf",
                "application/msword",
                "application/ms-doc",
                "application/doc",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "text/plain"
            )
        )
    }

    lateinit var rcbdbviewModel: rcbviewModel
    private fun setinstitute() {

        try {
            marrcenter = ArrayList()
            maincenterlist = ArrayList()

            maincenterlist = rcbdbviewModel.getallcenterlist()!!
            //Constant.arrstudentcenter = ArrayList()
            //Constant.arrstudentcenter = rcbdbviewModel.getallcenterlist()!!
         //   marrcenter.add("--Select--")
            for (i in 0..maincenterlist!!.size - 1) {
                //   println("Loop: $n")

                marrcenter.add(maincenterlist!!.get(i).center_name)
            }


            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrcenter)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_institution_name.setTypeface(mRegular, Typeface.BOLD)

            spincentername!!.setAdapter(aa)
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spincentername!!.setOnItemSelectedListener(this)
            }

            val pstinstitute =
                findInstitute(maincenterlist , Constant.objProfileResponse.stdinstituteid.toInt())
            spincentername!!.setSelection(pstinstitute)

        //    spincentername!!.setSelection(Constant.objProfileResponse.stdinstituteid.toInt())

            setprogram()
        } catch (e: Exception) {
            Log.d("exception_institute", e.toString())
            //    setprogram()
        }
    }


    private fun setprogram() {

        try {
            marrprogram = ArrayList()
            programlist = ArrayList()
            programlist = rcbdbviewModel.getallprogramlist()!!
            //  marrprogram.add("--Select--")
            for (i in 0..programlist.size - 1) {
                //   println("Loop: $n")

                marrprogram.add(
                    programlist.get(i).pm_program_title + " (" + programlist.get(
                        i
                    ).pm_code + " )"
                )

            }


            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrprogram)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_prog_name.setTypeface(mRegular, Typeface.BOLD)
            spinprgname!!.setAdapter(aa)
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spinprgname!!.setOnItemSelectedListener(this)
            }
            val pst =
                findIndexProgrammeName(programlist, Constant.objProfileResponse.stdprgname.toInt())


            //    spinprgname!!.setSelection(Constant.objProfileResponse.stdprgname.toInt())
            spinprgname!!.setSelection(pst)

            setacademicyear()
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }

    }


    private fun setacademicyear() {

        try {
            marracademicyear = ArrayList()
            academicyearlist = ArrayList()

            academicyearlist = rcbdbviewModel.getallaylist()!!
            //  marracademicyear.add("--Select--")

            for (i in 0..academicyearlist.size - 1) {

                marracademicyear.add(academicyearlist.get(i).ay_name)
            }

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marracademicyear)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_acadamic_year.setTypeface(mRegular, Typeface.BOLD)
            spinacademicyear!!.setAdapter(aa)

            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spinacademicyear!!.setOnItemSelectedListener(this)
            }

            //  val pst = findIndex(Constant.arracademicyear,Constant.objProfileResponse.stdassyear.toInt())
            val pstacademic =
                findIndexAcademic(academicyearlist, Constant.objProfileResponse.stdassyear.toInt())
            spinacademicyear!!.setSelection(pstacademic)


            setbranch()
        } catch (e: Exception) {
            Log.d("Exception_ay", e.toString())
        }
    }


    var position = -1

    //fun findIndex(arr: ArrayList<academicyear>, item: Int): Int {
    fun findIndex(arr: ArrayList<String>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until arr.size) {
                if (i == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }
    fun findInstitute(arr:List<Center>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until arr.size) {
                if (arr.get(i).center_id.toInt() == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }

    fun findIndexSpecialization(arr: List<Specialization>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until arr.size) {
                if (arr.get(i).sm_id == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }

    fun findIndexBranch(arr: List<Branch>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until arr.size) {
                if (arr.get(i).bm_id == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }

    fun findIndexAcademic(arr: List<Ay>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until arr.size) {
                if (arr.get(i).ay_id == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }

    fun findIndexProgrammeName(arr: List<Program>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until arr.size) {
                if (arr.get(i).pm_id == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }


    private fun setbranch() {

        try {
            marrbranch = ArrayList()
            branchlist = ArrayList()
            branchlist = rcbdbviewModel.getallbranchlist()!!
          //  marrbranch.add("--Select--")

            //

            for (i in 0..branchlist.size - 1) {

                marrbranch.add(branchlist.get(i).bm_branchtitle)
            }

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrbranch)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_branch.setTypeface(mRegular, Typeface.BOLD)
            spinbranch!!.setAdapter(aa)
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spinbranch!!.setOnItemSelectedListener(this)
            }
            val branchpst =
                findIndexBranch(branchlist, Constant.objProfileResponse.stdbranch.toInt())
            spinbranch!!.setSelection(branchpst)
            //  spinbranch!!.setSelection(Constant.objProfileResponse.stdbranch.toInt())
            setsepcialization()
            // setseason()
        } catch (e: Exception) {
            Log.d("Exception_Season", e.toString())
        }
        //setgender()
    }


    private fun setsepcialization() {
        try {
            arrspecialization = ArrayList()
            speciallist = ArrayList()
            speciallist = rcbdbviewModel.getallspecializationlist()!!
            Log.d("speciallist", speciallist.size.toString())
            //    arrspecialization.add("--Select--")
            for (i in 0..speciallist.size - 1) {

                arrspecialization.add(speciallist.get(i).sm_title)
            }
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrspecialization)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_specialization.setTypeface(mRegular, Typeface.BOLD)
            spinspecialization!!.adapter = aa
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spinspecialization!!.setOnItemSelectedListener(this)
            }

            val spstpst =
                findIndexSpecialization(
                    speciallist,
                    Constant.objProfileResponse.studentspecialization
                )
            spinspecialization!!.setSelection(spstpst)
            setseason()
        } catch (e: Exception) {
            Log.d("Excep_sepec", e.toString())

        }
    }

    var season_duration_val: Int = 0
    var seasonpst: Int = 0

    @SuppressLint("SuspiciousIndentation")
    private fun setseason() {
        try {
            marrseason = ArrayList()
            marrseasonduration = ArrayList()
            //seasonlist = ArrayList()
            seasonlist = rcbdbviewModel.getallseasonlist()!!
            Log.d("season_list", seasonlist.size.toString())
            //    marrseason.add("--Select--")
            //    marrseasonduration.add("--Select--")
            for (i in 0..seasonlist.size - 1) {

                marrseason.add(seasonlist.get(i).season_name)
                marrseasonduration.add(seasonlist.get(i).season_duration)

                if (Constant.objProfileResponse.studentSeasons == seasonlist.get(i).season_id) {
                    //seasonpst = i +1
                    season_duration_val = i

                }

            }
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrseason)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_season.setTypeface(mRegular, Typeface.BOLD)
            tv_program_duration.setTypeface(mRegular, Typeface.BOLD)
            spinseason!!.setAdapter(aa)
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spinseason!!.setOnItemSelectedListener(this)
            }

            val pst = findIndexseason(seasonlist, Constant.objProfileResponse.studentSeasons)

            //   spinseason!!.setSelection(season_duration_val)
            spinseason!!.setSelection(pst)

            val sessiondurationadapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, marrseasonduration)
            sessiondurationadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinprgduration!!.adapter = sessiondurationadapter
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spinprgduration!!.setOnItemSelectedListener(this)
            }

            spinprgduration!!.setSelection(season_duration_val)


            setgender()
        } catch (e: Exception) {
            Log.d("Exception_season", e.toString())
        }
    }

    fun findIndexseason(arr: List<Season>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until seasonlist.size) {
                if (arr.get(i).season_id == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }
    fun findIndexGender(arr: List<Dropdown>, item: Int): Int {
        //return arr.indexOf(item)
        try {
            for (i in 0 until ddlist.size) {
                if (arr.get(i).dropdown_valueid == item) {
                    position = i
                    break;  // uncomment to get the first instance
                }
            }

        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }
        return position
    }

    private fun setgender() {

        try {
            marrgender = ArrayList()
            arrmaritalstatus = ArrayList()
       //     marrgender.add("--Select--")
            arrmaritalstatus.add("--Select--")
            ddlist = ArrayList()
            ddlist = rcbdbviewModel.getalldropdownlist()!!
            //  spingender

            for (i in 0..ddlist.size - 1) {
                // for gender dropdown_dd_id =3
                if (ddlist.get(i).dropdown_dd_id == 3) {
                    marrgender.add(ddlist.get(i).dropdown_name)
                } else if (ddlist.get(i).dropdown_dd_id == 14) {
                    arrmaritalstatus.add(ddlist.get(i).dropdown_name)
                }

            }

            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, marrgender)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            tv_gender.setTypeface(mRegular, Typeface.BOLD)
            spingender!!.setAdapter(aa)
            if (Constant.objProfileResponse.issubmitforreview == 0) {
                spingender!!.setOnItemSelectedListener(this)
            }

            val pst = findIndexGender(ddlist, Constant.objProfileResponse.studentgenderid.toInt())

            //   spinseason!!.setSelection(season_duration_val)
            spingender!!.setSelection(pst)
            //spingender!!.setSelection(Constant.objProfileResponse.studentgenderid.toInt())


            val martialadapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, arrmaritalstatus)
            martialadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            if (submitforreview == Constant.objProfileResponse.issubmitforreview) {
                tv_maritial_status.visibility = View.VISIBLE
                spinmaritalstatus.visibility = View.VISIBLE
                tv_maritial_status.setTypeface(mRegular, Typeface.BOLD)
                spinmaritalstatus!!.setAdapter(martialadapter)

                spinmaritalstatus!!.setOnItemSelectedListener(this)

                spinmaritalstatus!!.setSelection(Constant.objProfileResponse.studentgenderid.toInt())
            } else {
                tv_maritial_status.visibility = View.GONE
                spinmaritalstatus.visibility = View.GONE

            }



            edt_email.setTypeface(mRegular)
            edt_email.setText(Constant.objProfileResponse.studentusername)

            edt_dob.setTypeface(mRegular)
            edt_dob.transformIntoDatePicker(this, "MM/dd/yyyy")

            Picasso
                .get()
                .load(Constant.photourl + Constant.objProfileResponse.studentphoto)
                .resize(200, 200)
                .noFade()
                .into(imgphoto);

            /*Picasso
                .get()
                .load(Constant.sigurl + Constant.objProfileResponse.studentsignature)
                .resize(200, 200)
                .noFade()
                .into(imgstudentsignature);*/

            Picasso
                .get()
                .load(Constant.ccertificateurl + Constant.objProfileResponse.studentcharactercertificate)
                .resize(200, 200)
                .noFade()
                .into(imgcharactercertificate);


            //if (Constant.objProfileResponse.studentcharactercertificate.length>0) {
            if (!Constant.objProfileResponse.studentcharactercertificate.isNullOrEmpty()) {
                Constant.student_char_certificate =
                    Constant.objProfileResponse.studentcharactercertificate
            }

            // if (Constant.objProfileResponse.studentsignature.length>0) {
            if (!Constant.objProfileResponse.studentsignature.isNullOrEmpty()) {
                Constant.student_signature = Constant.objProfileResponse.studentsignature
            }




            if (Constant.objProfileResponse.issubmitforreview == 1) {

                edt_email.isEnabled = false
                edt_dob.isEnabled = false
                spingender.isEnabled = false
                spinmaritalstatus.isEnabled = false
                spinbranch.isEnabled = false
                spinacademicyear.isEnabled = false
                spinprgname.isEnabled = false
                spincentername.isEnabled = false
                spinseason.isEnabled = false
                spinprgduration.isEnabled = false
                spinspecialization.isEnabled = false
                edt_student_first_nme.isEnabled = false
                edt_student_middle_nme.isEnabled = false
                edt_student_last_nme.isEnabled = false
                edt_phone.isEnabled = false
                edt_dob.isEnabled = false
                btnuploadchcertificate.visibility=View.GONE
                btnuploadsignature.visibility=View.GONE
                // btnuploadchcertificate.isEnabled = false
                // btnuploadcertificate.isEnabled = false
                //  btnuploadsignature.isEnabled = false


            }else{
                btnuploadchcertificate.visibility=View.VISIBLE
                btnuploadsignature.visibility=View.VISIBLE

            }
        } catch (e: Exception) {
            Log.d("exception_gender", e.toString())
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()

        // finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
    }


    private lateinit var uri_path: Uri
    private var imageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                try {
                    Log.d("forpdf", "forpdf")
                    mFileUtil = FileUtil()
                    uri_path = Uri.parse(data!!.data.toString())

                    val path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS
                    )
                    var file = File(uri_path!!.path.toString())
                    var file_name = file.name
                    Constant.original_file =
                        File(mFileUtil.getPath(uri_path, this@PersonalDetailActivity))
                    path.mkdirs()
                    Log.d("RealPath", Constant.original_file.toString())

                    //   fileextension = FileUtil.getMimeType(PersonalDetailActivity@this, uri_path)

                    fileextension = FileUtil.getMimeType(PersonalDetailActivity@ this, uri_path)
                    val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
                    //  var file_name = "cc" +file.name
                    //   var file_name= "cc" + df.format(Date()).toString() + "." + fileextension
                    //  var file_name="";
                    if (isuploadcharcertificate) {
                        filetype = "charactercertificate"
                        file_name = "cc" + df.format(Date()).toString() + "." + fileextension
                        pdf_img_cc.setImageURI(imageUri)


                        Constant.student_char_certificate = file_name
                        img_layout_relative_cc.visibility = View.VISIBLE
                        //  img_layout_relative_sig.visibility = View.GONE


                    } else if (isuploadsignature) {
                        filetype = "studentsignature"
                        file_name = "sign" + df.format(Date()).toString() + "." + fileextension
                        pdf_img_sig.setImageURI(imageUri)
                        Constant.student_signature = file_name
                        img_layout_relative_sig.visibility = View.VISIBLE
                        //  img_layout_relative_cc.visibility = View.GONE


                    }


                    if (uri_path.toString().startsWith("content://")) {
                        var cursor: Cursor? = null;

                        try {
                            cursor = getContentResolver().query(uri_path, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName =
                                    cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                // doc_name.setText(displayName)

                                //if (original_file.toString().contains("pdf")) {

                                //  generateImageFromPdf(uri_path)
                                //  }
                                //  val pdffile: File = File(getPath(uri_path))
                                encImage = FileUtil.getStringFile(
                                    PersonalDetailActivity@ this,
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
                        //generateImageFromPdf(uri_path)
                        // val pdffile: File = File(getPath(uri_path))
                        encImage = FileUtil.getStringFile(
                            PersonalDetailActivity@ this,
                            Constant.original_file
                        )

                    }

                    UploadDoc(
                        this@PersonalDetailActivity,
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
            }


        } else if (requestCode == 1) {

            //imageUri = data?.data
            imageUri = Uri.parse(data!!.data.toString())

            mFileUtil = FileUtil()

            var file = File(imageUri!!.path.toString())

            //fileextension = getMimeType(PersonalDetailActivity@this, imageUri!!)
            fileextension = FileUtil.getMimeType(PersonalDetailActivity@ this, imageUri!!)
            // store in db
            val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            //  var file_name = "cc" +file.name
            //   var file_name= "cc" + df.format(Date()).toString() + "." + fileextension
            var file_name = "";
            if (isuploadcharcertificate) {
                filetype = "charactercertificate"
                file_name = "cc" + df.format(Date()).toString() + "." + fileextension
                pdf_img_cc.setImageURI(imageUri)


                Constant.student_char_certificate = file_name
                img_layout_relative_cc.visibility = View.VISIBLE
                //  img_layout_relative_sig.visibility = View.GONE


            } else if (isuploadsignature) {
                filetype = "studentsignature"
                file_name = "sign" + df.format(Date()).toString() + "." + fileextension
                pdf_img_sig.setImageURI(imageUri)
                Constant.student_signature = file_name
                img_layout_relative_sig.visibility = View.VISIBLE
                //  img_layout_relative_cc.visibility = View.GONE


            }


            Constant.original_file =
                File(mFileUtil.getPath(imageUri!!, this@PersonalDetailActivity))
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

            displayName = file.getName()
            //  mDocumentName = file.getName()
            //mDocumentName = "test.jpg"
            // doc_name.setText(displayName)


            path.mkdirs()
            Log.d("RealPath", Constant.original_file.toString())


            tvdeleteicon_cc.setOnClickListener {

                // Log.d("delete_click", "delete_click")

                val jsondeletefile = JSONObject()


                jsondeletefile.put("methodname", "delete")
                jsondeletefile.put("filename", file_name)

                if (isuploadcharcertificate) {
                    jsondeletefile.put("type", "charactercertificate")
                } else if (isuploadsignature) {
                    jsondeletefile.put("type", "studentsignature")
                }
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
                            pdf_img_cc.setImageDrawable(null)
                            tvdeleteicon_cc.visibility = View.GONE
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
            // uploadDocument("certificate")
            // uploadMultiFile()

            tvdeleteicon_sig.setOnClickListener {

                // Log.d("delete_click", "delete_click")

                val jsondeletefile = JSONObject()


                jsondeletefile.put("methodname", "delete")
                jsondeletefile.put("filename", file_name)

                if (isuploadcharcertificate) {
                    jsondeletefile.put("type", "charactercertificate")
                } else if (isuploadsignature) {
                    jsondeletefile.put("type", "studentsignature")
                }

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
                            pdf_img_sig.setImageDrawable(null)
                            tvdeleteicon_sig.visibility = View.GONE
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

            // uploadfilenew(file_name)
            //  encImage= getStringFile(Constant.original_file)

            encImage = FileUtil.getStringFile(PersonalDetailActivity@ this, Constant.original_file)
            UploadDoc(
                this@PersonalDetailActivity,
                "uploadimagenew",
                file_name,
                encImage,
                Constant.accesstoken,
                fileextension,
                filetype
            ).execute(Constant.url + "student")

        }
    }


    fun generateImageFromPdf(pdfUri: Uri) {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this@PersonalDetailActivity)

        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")


            var file_name = "";
            if (isuploadcharcertificate) {
                file_name = "cc" + df.format(Date()).toString() + "." + fileextension
            } else if (isuploadsignature) {
                file_name = "sign" + df.format(Date()).toString() + "." + fileextension
            }

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
            uploadfilenew(file_name)
        } catch (e: Exception) {
            //todo with exception
            e.printStackTrace()
        }
    }


    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }

    /*  fun getMimeType(context: Context, uri: Uri): String {
          var extension: String

          //Check uri format to avoid null
          extension = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
              //If scheme is a content
              val mime = MimeTypeMap.getSingleton()
              mime.getExtensionFromMimeType(context.contentResolver.getType(uri)).toString()
          } else {
              //If scheme is a File
              //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
              MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
          }
          return extension
      }*/


    // Converting File to Base64.encode String type using Method
    /*  fun getStringFile(f: File): String {
          var inputStream: InputStream? = null
         // var encodedFile = ""
          var lastVal: String = ""
          try {
              inputStream = FileInputStream(f.absolutePath)
              val buffer = ByteArray(10240) //specify the size to allow
              var bytesRead: Int = 0
              val output = ByteArrayOutputStream()
              val output64 = Base64OutputStream(output, Base64.DEFAULT)
              while (inputStream.read(buffer).also({ bytesRead = it }) != -1) {
                  output64.write(buffer, 0, bytesRead)
              }
              output64.close()
              lastVal = output.toString()
          } catch (e1: FileNotFoundException) {
              e1.printStackTrace()
          } catch (e: IOException) {
              e.printStackTrace()
          }
         Log.d("file_base64", lastVal)
          return lastVal
      }*/


    private fun uploadfilenew(filename: String) {

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
            if (isuploadcharcertificate) {
                multipart.addFormField("type", "charactercertificate")
            } else if (isuploadsignature) {
                multipart.addFormField("type", "studentsignature")
            }
            multipart.addFormField("methodname", "upload")
            // Add file
            //  var file = File(imageUri!!.path.toString())
            multipart.addFormField("filename", filename)
            multipart.addFilePart("file", Constant.original_file)
            // Print result
            val response: String = multipart.finish()
            println(response)
            Log.d("response", response.toString())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("exception", e.toString())
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (parent!!.id == R.id.spincentername) {
            objProfileResponse.stdinstituteid = position.toString()
        } else if (parent!!.id == R.id.spinprgname) {
            objProfileResponse.stdprgname = position.toString()
        } else if (parent!!.id == R.id.spinacademicyear) {
            Constant.objProfileResponse.stdassyear = position.toString()
        } else if (parent!!.id == R.id.spinbranch) {
            objProfileResponse.stdbranch = position.toString()
        } else if (parent!!.id == R.id.spingender) {
            objProfileResponse.studentgenderid = position.toString()
        }

    }


    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }


    internal class UploadDoc(
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
            progressuploading.setTitle("Vahaan")
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
                Log.d("Vicky", "Data to php = $jsonObject.toString()")
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
                Log.d("Vicky", "Response from php = $result")
                //Response = new JSONObject(result);
                connection.disconnect()
                return result.toString()
            } catch (e: java.lang.Exception) {
                Log.d("Vicky", "Error Encountered")
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
                    // finish()
                    /*  val intent:Intent = Intent(asyctx,OnGoingActivity::class.java)
                      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                      asyctx.startActivity(intent)
                      //overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                      val param = WindowManager.LayoutParams()
                      param.width = WindowManager.LayoutParams.MATCH_PARENT
                      param.height = 520

                      new_dialog.window!!.attributes = param
                      new_dialog.window!!.attributes.windowAnimations = R.style.Animation
                      new_dialog.show()*/
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


    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }


}



