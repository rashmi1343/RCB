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
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.app.rcb.R
import com.app.rcb.api.HttpPostMultipart
import com.app.rcb.util.Constant
import com.app.rcb.util.Constant.Companion.objProfileResponse
import com.app.rcb.util.FileUtil
import com.app.rcb.util.LoadingScreen
import com.premierticketbookingapp.apicall.HttpTask
import com.shockwave.pdfium.PdfiumCore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_academic.*
import kotlinx.android.synthetic.main.layout_bank_detail.*
import kotlinx.android.synthetic.main.layout_bank_row.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BankdetailActivity : AppCompatActivity() {

    private lateinit var mHelpLayout: RelativeLayout
    private lateinit var editdialog: Dialog
    private lateinit var mRegular: Typeface
    private var fileextension:String = ""
    private val PICK_FILE_REQUEST = 100
    private lateinit var mFileUtil:FileUtil

    private lateinit var uri_path:Uri

    private lateinit var request_file:RequestBody

    private lateinit var multipartBody:MultipartBody.Part

    private lateinit var  original_file:File

    private  var displayName:String = ""

    private var imageUri: Uri? = null
    private lateinit var out: FileOutputStream




    private var arrviewbank: ArrayList<ViewGroup> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_bank_detail)




        mHelpLayout = findViewById(R.id.abt_header_layout)
        mHelpLayout.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
        }

        showbankdetail()
    }

    private var filetype:String = ""
    private  var encImage:String = ""
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK)
        {
            if (requestCode==PICK_FILE_REQUEST)
            {
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
                            this@BankdetailActivity
                        )
                    )
                    path.mkdirs()
                    Log.d("RealPath", Constant.original_file.toString())
                    fileextension = FileUtil.getMimeType(BankdetailActivity@this, uri_path)
                    filetype = "passbook"
                    val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
                    file_name = "pb" + df.format(Date()).toString() + "." + fileextension
                    Constant.student_passbook = file_name

                    request_file =
                        RequestBody.create(
                            "application/pdf".toMediaTypeOrNull(),
                            Constant.original_file
                        )


                    /* path.mkdir()
                     file.createNewFile()*/
                    multipartBody =  MultipartBody.Part.createFormData(
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
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                // doc_name.setText(displayName)

                                //if (original_file.toString().contains("pdf")) {

                               // generateImageFromPdf(uri_path)
                                //  }
                                encImage = FileUtil.getStringFile(BankdetailActivity@this,Constant.original_file)

                            }
                        } finally {
                            cursor!!.close();
                        }
                    } else if (uri_path.toString().startsWith("file://"))
                    {

                        displayName = file.getName()
                        //  mDocumentName = file.getName()
                        //  doc_name.setText(displayName)
                       // generateImageFromPdf(uri_path)
                        encImage=  FileUtil.getStringFile(BankdetailActivity@this,Constant.original_file)

                    }

                    UploadDocbank(
                        this@BankdetailActivity,
                        "uploadimagenew",
                        file_name,
                        encImage,
                        Constant.accesstoken,
                        fileextension,
                        filetype
                    ).execute(Constant.url + "student")

                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            else if (requestCode==1) {

                imageUri = data?.data

               // pdf_img.setImageURI(imageUri)
                mFileUtil = FileUtil()

                var file = File(imageUri!!.path.toString())
                fileextension = FileUtil.getMimeType(BankdetailActivity@this, imageUri!!)
                var file_name = file.name
                val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")


                filetype = "passbook"
                    file_name = "pb" + df.format(Date()).toString() + "." + fileextension
                    Constant.student_passbook = file_name






                Constant.original_file = File(
                    mFileUtil.getPath(
                        imageUri!!,
                        this@BankdetailActivity
                    )
                )
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                )

                displayName = file.getName()

//                img_layout_relative.visibility = View.VISIBLE

                path.mkdirs()
                Log.d("RealPath", Constant.original_file.toString())

                request_file = RequestBody.create(
                    "Image/jpeg".toMediaTypeOrNull(),
                    Constant.original_file
                )


                /* path.mkdir()
                 file.createNewFile()*/
                multipartBody =  MultipartBody.Part.createFormData(
                    "filenames",
                    file_name,
                    request_file
                )
                Log.d("Multipart", multipartBody.body.contentLength().toString())


               /* tvdeleteicon_acad.setOnClickListener {

                    val jsondeletefile = JSONObject()


                    jsondeletefile.put("methodname", "delete")
                    jsondeletefile.put("filename", file_name)
                    jsondeletefile.put("type", "passbook")

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
                           //     pdf_img.setImageDrawable(null)
                          //      tvdeleteicon_acad.visibility = View.GONE
                                Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()
                            }
                            else {
                                Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()
                            }


                        } catch (e: JSONException) {
                            println("Exception caught");
                        }


                    }.execute("POST", Constant.url + "student", jsondeletefile.toString().trim())

                }
*/
               // uploadfilenew()
                encImage = FileUtil.getStringFile(BankdetailActivity@this,Constant.original_file)
                UploadDocbank(
                    this@BankdetailActivity,
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


    fun generateImageFromPdf(pdfUri: Uri)
    {
        val pageNumber = 0
        val pdfiumCore = PdfiumCore(this@BankdetailActivity)

        try {
            val fd = getContentResolver().openFileDescriptor(pdfUri, "r")
            val pdfDocument = pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height)
         //   pdf_img.setImageBitmap(bmp)
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


    private fun uploadfilenew() {

        try {
            // Set header
            //  val headers: Map<String, String> = HashMap()
            var headers:MutableMap<String?, String?> = mutableMapOf()

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

            multipart.addFormField("type", "passbook")
            multipart.addFormField("filename", Constant.student_passbook)
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


    private fun showbankdetail() {

        var banknameval: TextView
        var accountnoval: TextView
        var accountnameval: TextView
        var ifsccodeval: TextView
        var tveditoption: TextView
        var tvviewoption: TextView
        var tvdeleteoption: TextView


        lateinit var typefacefontawesome: Typeface


        typefacefontawesome = Typeface.createFromAsset(
            applicationContext.assets,
            "fonts/FontAwesome.ttf"
        )
        val lllayoutbankdata = findViewById<LinearLayout>(R.id.llbankdetaildata)

        val layoutInflater: LayoutInflater = LayoutInflater.from(ScheduledDemoActivity@ this)

        arrviewbank = ArrayList()

        for (i in 0..Constant.objProfileResponse.studentbankdetails.size - 1) {

            val vwbankdetail: ViewGroup =
                    layoutInflater.inflate(R.layout.layout_bank_row, null, false) as ViewGroup

            var banpassbook:String = Constant.objProfileResponse.studentbankdetails.get(i).sb_passbook?:return

            banknameval = vwbankdetail.findViewById(R.id.tvbanknameval)

            accountnameval = vwbankdetail.findViewById(R.id.tvbankacountnameval)

            accountnoval = vwbankdetail.findViewById(R.id.tvbankacountnoval)

            ifsccodeval = vwbankdetail.findViewById(R.id.tvbankifsccodeval)

            tvviewoption = vwbankdetail.findViewById(R.id.tvviewoption)

            tveditoption = vwbankdetail.findViewById(R.id.tveditoption)

            tvdeleteoption = vwbankdetail.findViewById(R.id.tvdeleteoption)

            banknameval.setText(objProfileResponse.studentbankdetails.get(i).sb_bank_name)

            accountnoval.setText(objProfileResponse.studentbankdetails.get(i).sb_acc_no)

            accountnameval.setText(objProfileResponse.studentbankdetails.get(i).sb_acc_name)

            ifsccodeval.setText(objProfileResponse.studentbankdetails.get(i).sb_ifsc)

            tveditoption.setTypeface(typefacefontawesome)

          //  tveditoption.setText("\uf044")

            tvviewoption.setTypeface(typefacefontawesome)
          //  tvviewoption.setText("\uf06e")

            tvviewoption.setOnClickListener {

                try {
                    if (banpassbook != "null") {
                        val bankpassbookparts = banpassbook.split(".")
                        var viewextension: String = bankpassbookparts[1]

                        if (viewextension == "png" || viewextension == "jpg" || viewextension == "jpeg") {
                            val builder: AlertDialog.Builder =
                                AlertDialog.Builder(this)
                            val inflater: LayoutInflater =
                                this.getLayoutInflater()
                            val dialogView: View =
                                inflater.inflate(R.layout.activity_img_dialog, null)
                            val imgview: ImageView = dialogView.findViewById(R.id.cc_image)

                            if (banpassbook.length > 0) {
                                Picasso
                                    .get()
                                    .load(Constant.bankpburl + banpassbook)
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
                                    DialogInterface.OnClickListener { dialog, which -> }).create()
                                .show()


                        } else if (viewextension == "pdf") {
                            val mainIntent =
                                Intent(this@BankdetailActivity, ViewPdfActivity::class.java)
                            mainIntent.putExtra("docname", Constant.bankpburl + banpassbook)
                            startActivity(mainIntent)
                          //  finish()
                            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
                        }else {
                            Toast.makeText(
                                BankdetailActivity@ this,
                                "File not available",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            BankdetailActivity@ this,
                            "File not available",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }catch(e:Exception) {
                    Log.d("exception", "file not available")

                    Toast.makeText(
                        BankdetailActivity@ this,
                        "File not available",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            tvdeleteoption.setTypeface(typefacefontawesome)
            tvdeleteoption.setText("\uf1f8")
            tvdeleteoption.visibility = View.GONE

            arrviewbank.add(vwbankdetail)

            tveditoption.setOnClickListener {


                showeditdialog(
                    objProfileResponse.studentbankdetails.get(i).sb_bank_name,
                    objProfileResponse.studentbankdetails.get(i).sb_acc_no,
                    objProfileResponse.studentbankdetails.get(i).sb_acc_name,
                    objProfileResponse.studentbankdetails.get(i).sb_ifsc, i,
                    2,
                    objProfileResponse.studentbankdetails.get(i).sb_passbook
                )


            }

            lllayoutbankdata.addView(vwbankdetail)
        }
        tvplusiconbank.setText("\uf067")
        tvplusiconbank.setTypeface(typefacefontawesome)
        tvplusiconbank.visibility = View.GONE

        tvplusiconbank.setOnClickListener {

            showeditdialog(
                "",
                "",
                "",
                "", 0,
                1,
                ""
            )

        }


    }


    private fun showeditdialog(
        sbBankName: String,
        sbAccNo: String,
        sbAccName: String,
        sbIfsc: String,
        position: Int,
        mode: Int,
        sbpassbook: String
    ) {

        var edtbankname: EditText
        var edtbankaccountname: EditText
        var edtbankaccountno: EditText
        var edtbankifsccode: EditText
        var btnsavebankdetail: Button
        var btncancelbankdetail: Button
        var btnuploadbankdetail:Button

        var tvviewiconbank:TextView
        var tvdeleteiconbank:TextView
        var imgpassbook:ImageView

        lateinit var typefacefontawesome:Typeface



        editdialog = Dialog(this@BankdetailActivity)
        editdialog.setContentView(R.layout.layout_edit_bank_detail)

        edtbankname = editdialog.findViewById<EditText>(R.id.edtbankname)
        edtbankaccountname = editdialog.findViewById<EditText>(R.id.edtbankaccountname)
        edtbankaccountno = editdialog.findViewById<EditText>(R.id.edtbankaccountno)
        edtbankifsccode = editdialog.findViewById<EditText>(R.id.edtbankifsccode)

        edtbankname.setText(sbBankName)

        edtbankaccountname.setText(sbAccName)

        edtbankifsccode.setText(sbIfsc)

        edtbankaccountno.setText(sbAccNo)

        if(Constant.objProfileResponse.issubmitforreview==1) {
            edtbankname.isEnabled = false
            edtbankaccountname.isEnabled = false
            edtbankifsccode.isEnabled = false
            edtbankaccountno.isEnabled = false
            tveditoption.visibility=View.GONE
        }else{
            tveditoption.visibility=View.VISIBLE
        }

        editdialog.show()

        typefacefontawesome = Typeface.createFromAsset(
            applicationContext.assets,
            "fonts/FontAwesome.ttf"
        )

        tvviewiconbank = editdialog.findViewById<TextView>(R.id.tvviewiconbank)

        tvdeleteiconbank = editdialog.findViewById<TextView>(R.id.tvdeleteiconbank)

        imgpassbook = editdialog.findViewById<ImageView>(R.id.imgpassbook)
       // imgpassbook.setTypeface(typefacefontawesome)
       // imgpassbook.setText("\uf1c1")

        if (sbpassbook!=null) {
            Picasso
                    .get()
                    .load(Constant.bankpburl + sbpassbook)
                    .resize(200, 200)
                    .noFade()
                    .into(imgpassbook);
        }



      //  tvviewiconbank.setText("\uf06e")
        tvviewiconbank.setText("View")
        tvviewiconbank.setTypeface(typefacefontawesome)

        tvviewiconbank.setOnClickListener {



             if (!sbpassbook.isNullOrEmpty()) {
                var ext = sbpassbook.split(".")

                if (ext[1] == "pdf") {
                    Log.d("pdf", ext[1])

                    val pdf =  Constant.bankpburl  + sbpassbook
                    val viewpdfintent = Intent(this@BankdetailActivity, ViewPdfActivity::class.java)
                    viewpdfintent.putExtra("docname", pdf)
                    startActivity(viewpdfintent)
                    overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                } else if (ext[1] == "png") {
                   // Log.d("image", ext[1])
                    val viewimgintent = Intent(this@BankdetailActivity, ViewImgActivity::class.java)
                    viewimgintent.putExtra("imgname", Constant.bankpburl + sbpassbook)
                    startActivity(viewimgintent)
                    overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
                }
            }
            else {
                Toast.makeText(
                    AcademicdetailActivity@ this,
                    "File not available",
                    Toast.LENGTH_LONG
                ).show()
              /*  Picasso
                        .get()
                        .load(Constant.bankpburl + sbpassbook)
                        .resize(200, 200)
                        .noFade()
                        .into(imgpassbook);*/
            }
        }

        tvdeleteiconbank.setText("\uf1f8")
        tvdeleteiconbank.setTypeface(typefacefontawesome)
      //  tvdeleteiconbank.visibility = View.GONE

        tvdeleteiconbank.setOnClickListener {

            if (sbpassbook.length>0) {

               // val string: String = "leo_Ana_John"


             //   val yourArray: List<String> = sbpassbook.split("/")

               // val filename:String = yourArray[3]
                val jsondeletefile = JSONObject()


                jsondeletefile.put("methodname", "delete")
              //  jsondeletefile.put("filename", filename)
                jsondeletefile.put("filename",sbpassbook)
                jsondeletefile.put("type", "passbook")

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
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()

                        }


                    } catch (e: JSONException) {
                        println("Exception caught");
                    }


                }.execute("POST", Constant.url + "student", jsondeletefile.toString().trim())
            }
            else {
                Toast.makeText(
                    AcademicdetailActivity@ this,
                    "File not available",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

        tvdeleteiconbank.visibility = View.GONE



        btnuploadbankdetail = editdialog.findViewById<Button>(R.id.btnuploadbankdetail)

        btnuploadbankdetail.setOnClickListener {

            mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
           // isuploadacademicdocument = true;
          //  isuploadentranceproof = false;
            //  isuploadacademicdocument = false;


            val new_dialog = Dialog(this@BankdetailActivity)
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

        btnsavebankdetail = editdialog.findViewById<Button>(R.id.btnsavebankdetail)

        btnsavebankdetail.setOnClickListener {
            //call api to save bank detail
            updatelist(
                edtbankname.text.toString(), edtbankaccountname.text.toString(),
                edtbankifsccode.text.toString(),
                edtbankaccountno.text.toString(),
                position,
                editdialog,
                mode
            )

        }

        btncancelbankdetail = editdialog.findViewById<Button>(R.id.btncancelbankdetail)

        btncancelbankdetail.setOnClickListener {

            editdialog.dismiss()
        }




    }


    fun fromgallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 1)
    }


    private fun updatelist(
        sbBankName: String,
        sbAccNo: String,
        sbAccName: String,
        sbIfsc: String,
        pos: Int,
        editdialog: Dialog,
        mode: Int
    ) {

        var banknameval: TextView
        var accountnoval: TextView
        var accountnameval: TextView
        var ifsccodeval: TextView
        //  var tveditoption : TextView

        if (arrviewbank.size==0) {
            updatebankdetailapi(
                "0",
                sbBankName, sbAccName, sbAccNo, sbIfsc, mode
            )
            editdialog.dismiss()
        }
        else {
            for (bk in 0..arrviewbank.size - 1) {

                if (bk == pos) {


                    banknameval = arrviewbank.get(bk).findViewById(R.id.tvbanknameval) as TextView

                    accountnameval =
                        arrviewbank.get(bk).findViewById(R.id.tvbankacountnameval) as TextView

                    accountnoval =
                        arrviewbank.get(bk).findViewById(R.id.tvbankacountnoval) as TextView

                    ifsccodeval =
                        arrviewbank.get(bk).findViewById(R.id.tvbankifsccodeval) as TextView


                    banknameval.setText(sbBankName)
                    accountnameval.setText(sbAccName)
                    accountnoval.setText(sbAccNo)
                    ifsccodeval.setText(sbIfsc)

                    objProfileResponse.studentbankdetails.get(bk).sb_bank_name = sbBankName
                    objProfileResponse.studentbankdetails.get(bk).sb_acc_name = sbAccName
                    objProfileResponse.studentbankdetails.get(bk).sb_ifsc = sbIfsc
                    objProfileResponse.studentbankdetails.get(bk).sb_acc_no = sbAccNo

                    updatebankdetailapi(
                        objProfileResponse.studentbankdetails.get(bk).sb_id,
                        sbBankName, sbAccName, sbAccNo, sbIfsc, mode
                    )
                    editdialog.dismiss()
                    break

                }


            }
        }

    }


    private fun updatebankdetailapi(
        sbid: String,
        sbankname: String,
        saccountname: String,
        saccountno: String,
        sifsc: String,
        mode: Int
    ) {

        if (!LoadingScreen.isNetworkAvailable(AcademicdetailActivity@ this)) {
            Toast.makeText(
                AcademicdetailActivity@ this,
                "Internet Connectivity Issue",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (mode == 2) {        //  update existing detail
                val jsonupdatebankdetail = JSONObject()
                jsonupdatebankdetail.put("methodname", "updatebankdetail")
                jsonupdatebankdetail.put("student_id", objProfileResponse.studentid)
                jsonupdatebankdetail.put("sb_id", sbid)
                jsonupdatebankdetail.put("sb_bank_name", sbankname)
                jsonupdatebankdetail.put("sb_acc_name", saccountname)
                jsonupdatebankdetail.put("sb_acc_no", saccountno)
                jsonupdatebankdetail.put("sb_ifsc", sifsc)
                jsonupdatebankdetail.put("sb_passbook", Constant.student_passbook)

                Log.d("jsonupdatebankdetail", jsonupdatebankdetail.toString())


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


                }.execute("POST", Constant.url + "student", jsonupdatebankdetail.toString().trim())
            }
            else if (mode==1) {     // create new bank Account record

                val jsoncreatebankdetail = JSONObject()
                jsoncreatebankdetail.put("methodname", "createbankdetail")
                jsoncreatebankdetail.put("student_id", objProfileResponse.studentid)
            //    jsonupdatebankdetail.put("sb_id", sbid)
                jsoncreatebankdetail.put("sb_bank_name", sbankname)
                jsoncreatebankdetail.put("sb_acc_name", saccountname)
                jsoncreatebankdetail.put("sb_acc_no", saccountno)
                jsoncreatebankdetail.put("sb_ifsc", sifsc)
                jsoncreatebankdetail.put("sb_passbook", Constant.student_passbook)


                Log.d("jsoncreateebankdetail", jsoncreatebankdetail.toString())


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


                }.execute("POST", Constant.url + "student", jsoncreatebankdetail.toString().trim())
            }
        }

    }



    internal class UploadDocbank(ctx: Context, mname:String, imagename:String, encode_string:String?, maintoken:String?, fileext:String, doctype:String) :
        AsyncTask<String, Void, String>() {

        val methodname:String = mname
        val Encode_String = encode_string
        val Main_token = maintoken
        val asyctx: Context = ctx
        private lateinit var progressuploading : ProgressDialog

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
                val random_num = kotlin.random.Random.nextInt((200-20)+1)+20

                var jsonObject = JSONObject()

                //isuploadcharcertificate = false
                //isuploadsignature = true
                //if(isuploadcharcertificate)
                jsonObject.put("filename", filename)
                //    jsonObject.put("fileextension",fileextension)
                jsonObject.put("methodname",methodname)
                jsonObject.put("doctype",dtype)
                if(fileextension.contains("jpg")) {
                    jsonObject.put("strbase64dta", "data:image/jpg;base64," + Encode_String)
                }
                else if (fileextension.contains("png")) {
                    jsonObject.put("strbase64dta", "data:image/png;base64," + Encode_String)
                }
                else if (fileextension.contains("pdf")) {
                    jsonObject.put("strbase64dta","data:application/pdf;base64,"+Encode_String)
                }


                val url = URL(Constant.url + "student")
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.setDoOutput(true)
                connection.setDoInput(true)
                connection.setRequestMethod("POST")
                connection.setFixedLengthStreamingMode(jsonObject.toString().toByteArray().size)
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("Authorization","Bearer " + Main_token.toString().trim())

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
                val result:String? = sb.toString()
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

            if (result.toString().contains("1",false))
            {
                progressuploading.dismiss()
                val new_dialog = Dialog(asyctx)
                new_dialog.setContentView(R.layout.success_dialog)
                val success_text: TextView = new_dialog.findViewById(R.id.suceess_text)
                val mmedium :Typeface = Typeface.createFromAsset(asyctx.assets,"montserrat/Montserrat-Medium.otf")
                val mRegular:Typeface = Typeface.createFromAsset(asyctx.assets,"montserrat/Montserrat-Regular.otf")
                success_text.setTypeface(mRegular)
                if ((fileextension.contains("jpg")) ||  fileextension.contains("png")) {
                    success_text.text = "Image Uploaded successfully"
                }
                else if (fileextension.contains("pdf")) {
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
            }
            else
            {
                progressuploading.dismiss()
                Toast.makeText(asyctx,"Something went wrong",Toast.LENGTH_SHORT).show()
            }


        }
    }


}