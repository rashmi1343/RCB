package com.app.rcb.ui

import Apirequest.ApiClient
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.*
import android.provider.OpenableColumns
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.AppBarConfiguration
import com.app.rcb.R
import com.app.rcb.api.ApiInterface
import com.app.rcb.api.HttpPostMultipart
import com.app.rcb.response.*
import com.app.rcb.util.Constant
import com.app.rcb.util.Constant.Companion.objProfileResponse
import com.app.rcb.util.FileUtil
import com.app.rcb.util.FileUtil.Companion.showError
import com.app.rcb.util.FileUtilnew
import com.app.rcb.util.LoadingScreen
import com.app.rcb.viewmodel.rcbviewModel
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.kaopiz.kprogresshud.KProgressHUD
import com.premierticketbookingapp.apicall.HttpTask
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.nav_header_my_new.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashBoardActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG: String = "DashBoardActivity"
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mMedium: Typeface
    private lateinit var mRegular: Typeface
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mSettingsText: TextView

    private lateinit var date_final: String
    private lateinit var mStartTime: String
    private lateinit var mEndTime: String

    private lateinit var mDeliveryText: TextView
    private lateinit var mPriceText: TextView
    private lateinit var mDateText: TextView
    private lateinit var mStartTimeText: TextView
    private lateinit var mEndTimeText: TextView
    private lateinit var mUserText: TextView
    private lateinit var mPickupText: TextView
    private lateinit var mDropoffText: TextView
    private lateinit var mNoongoingText: TextView
    private lateinit var mOngoHeading: ImageView
    private lateinit var mSmileImage: ImageView
    private lateinit var mClockImage: ImageView
    private lateinit var mLocationImage: ImageView
    private lateinit var mImageProfile: ImageView

    // private var progress: ProgressBar? = null

    var strstudentid: String? = null


    private lateinit var mPrefrences: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    private lateinit var mFileUtil: FileUtil

    private lateinit var request_file: RequestBody

    private lateinit var multipartBody: MultipartBody.Part


    private lateinit var imageView: ImageView

    private var displayName: String = ""

    private var imageUri: Uri? = null
    private var fileextension: String = ""

    private val IMG_WIDTH = 640
    private val IMG_HEIGHT = 480

    private var actualImage: File? = null
    private var compressedImage: File? = null

    private var prgdeletephoto: KProgressHUD? = null

    private var stateid: Int = 0
    private val SPLASH_DELAY: Long = 3000
    private var stateidcor:Int=0
    lateinit var rcbdbviewModel: rcbviewModel
    private var splashprg: KProgressHUD? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)


    var appfirsttime :Int =0
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = getWindow()

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_pad));

        setContentView(R.layout.activity_my_new)


        LoadingScreen.displayLoadingWithText(this, "Please wait...", false)

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        textStyle()


        rcbdbviewModel = ViewModelProviders.of(this).get(rcbviewModel::class.java)





        strstudentid = intent.getStringExtra("studentid")

        Log.d("studentid", strstudentid.toString())

        mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
        mEditor = mPrefrences.edit()


        getStudentProfileDetail("Bearer " + Constant.accesstoken, "getProfileDetail", strstudentid)

        home_layout.visibility = View.GONE
        open_job_layout.visibility = View.GONE
        active_job_layout.visibility = View.GONE
        past_job_layout.visibility = View.GONE
        passed_job_layout.visibility = View.GONE
        abt_layout.visibility = View.GONE
        support_job_layout.visibility = View.GONE
        ongoing_job_layout.visibility = View.GONE
        logout_layout.visibility = View.VISIBLE
        settings_layout_user.visibility = View.GONE
        main_layout.visibility = View.VISIBLE


        /*ongoing_layout_dashboard.setOnClickListener {
            val intent:Intent = Intent(this@DashBoardActivity,OnGoingActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        }*/
        //person_img.setColorFilter(ContextCompat.getColor(this,android.R.color.white))
        // profile_layout.setOnClickListener(this)
        open_layout.setOnClickListener(this)
        active_layout.setOnClickListener(this)
        past_layout.setOnClickListener(this)
        home_layout.setOnClickListener(this)
        open_job_layout.setOnClickListener(this)
        active_job_layout.setOnClickListener(this)
        past_job_layout.setOnClickListener(this)
        my_profile_layout.setOnClickListener(this)
        logout_layout.setOnClickListener(this)
        abt_layout.setOnClickListener(this)
        support_job_layout.setOnClickListener(this)
        ongoing_job_layout.setOnClickListener(this)
        bankdetail_layout.setOnClickListener(this)
        academiccal_layout.setOnClickListener(this)
        fee_layout.setOnClickListener(this)
        assignment_layout.setOnClickListener(this)
        holiday_layout.setOnClickListener(this)



        mImageProfile = findViewById(R.id.my_profile_icon)
        mImageProfile.setColorFilter(R.color.colorAccent)

        drawerLayout = findViewById(R.id.drawer_layout)
        val mHamburger: ImageView = findViewById(R.id.hamburger_img)
        val mSettings: RelativeLayout = findViewById(R.id.settings_layout)
        mSettingsText = findViewById(R.id.setting_txt)
        mSettingsText.setTypeface(mRegular)
        mSettings.visibility = View.GONE

        bankdetail_txt.setTypeface(mRegular)

        acaddetail_txt.setTypeface(mRegular)
        fee_txt.setTypeface(mRegular)
        assignment_txt.setTypeface(mRegular)
        holiday_txt.setTypeface(mRegular)

        mSettings.setOnClickListener {

            val popupMenu: PopupMenu = PopupMenu(this, mSettings)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                if (item.itemId == R.id.action_logout) {
                    val displayMetrics = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(displayMetrics)
                    //  val height = displayMetrics.heightPixels
                    //  val width = displayMetrics.widthPixels
                    val dialog: Dialog = Dialog(this@DashBoardActivity)
                    dialog.setContentView(R.layout.logout_dialogue)
                    val yesBttn: AppCompatButton = dialog.findViewById(R.id.yes_button)
                    yesBttn.setOnClickListener {
                        dialog.dismiss()
                    }
                    // dialog.window!!.setLayout(1000,170)
                    dialog.show()
                }
                true
            })
            popupMenu.show()
        }

        mHamburger.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(Gravity.START);
            else drawerLayout.closeDrawer(Gravity.END)
        }
        val navView: NavigationView = findViewById(R.id.nav_view)

        imageView = navView.findViewById(R.id.imageView)


        settings_layout_user.setOnClickListener {
            /*       startActivity(Intent(this@DashBoardActivity,UserSettingsActivity::class.java))
                   overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
                   drawerLayout.closeDrawer(Gravity.LEFT, false)*/
        }


        profile_image.setOnClickListener {

            // fromgallery()
            mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
            //  isuploadcharcertificate = false
            //  isuploadsignature = true

            val new_dialog = Dialog(this@DashBoardActivity)
            // dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            new_dialog.setContentView(R.layout.custom_upload_dialog)
            // val metrics = resources.displayMetrics
            // val screenWidth = (metrics.widthPixels * 0.80).toInt()
            // getWindow().setLayout(screenWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);


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
            pick_text.setText("Upload")
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
            drop_text.setText("Remove")
            pick_location.setOnClickListener {

                deletephoto()
                // call delete api
                new_dialog.dismiss()

            }
            val param = WindowManager.LayoutParams()
            param.width = WindowManager.LayoutParams.MATCH_PARENT
            param.height = 750

            new_dialog.window!!.attributes = param
            new_dialog.window!!.attributes.windowAnimations = R.style.Animation
            new_dialog.show()

        }


    }


    @SuppressLint("Range")
    var mGetContent = registerForActivityResult(
        GetContent()
    ) { uri ->
        try {

            mFileUtil = FileUtil()

            var file = File(uri!!.path.toString())
            // store in db
            val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
            var file_name = "";
            file_name = "pp" + df.format(Date()).toString() + "." + fileextension
            profile_image.setImageURI(uri)
            imageView.setImageURI(uri)

            Constant.student_photo = file_name
            Constant.original_file = File(mFileUtil.getPath(uri!!, this@DashBoardActivity))
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            )

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
            }else {
                displayName = file.getName()
                encImage=  FileUtil.getStringFile(PersonalDetailActivity@this,Constant.original_file)
            }

            //displayName = file.getName()
           // path.mkdirs()
           // Log.d("RealPath", Constant.original_file.toString())
          //  encImage = FileUtil.getStringFile(DashBoardActivity@ this, Constant.original_file)
            UploadImages(
                this@DashBoardActivity,
                "uploadimagenew",
                "test.png",
                encImage,
                Constant.accesstoken
            ).execute(Constant.url + "student")
        } catch (e: IOException) {
            showError(this, "Failed to read picture data!")
            e.printStackTrace()
        }

    }


    fun fromgallery() {
        //  val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        // startActivityForResult(gallery, 1)
        //  resultLauncher.launch(gallery)

        mGetContent.launch("image/*");
        // mGetContent.launch("*/*")

    }


    private fun deletephoto() {


        prgdeletephoto = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Processing..")
            .setDetailsLabel("RCB")
            .show();

        val jsondeletefile = JSONObject()


        jsondeletefile.put("methodname", "delete")
        jsondeletefile.put("filename", Constant.student_photo)
        jsondeletefile.put("type", "profileimage")




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

                    if(objProfileResponse.studentgenderid.toInt()==1){
                        imageView.setImageResource(R.drawable.person_user)
                        profile_image.setImageResource(R.drawable.person_user)
                    }else{
                        imageView.setImageResource(R.drawable.female_profile)
                        profile_image.setImageResource(R.drawable.female_profile)
                    }
                    //    pdf_img_cc.setImageDrawable(null)

                    prgdeletephoto!!.dismiss()
                    //  tvdeleteicon_cc.visibility = View.GONE
                    Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()


                } else {
                    prgdeletephoto!!.dismiss()

                    Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()

                }


            } catch (e: JSONException) {
                println("Exception caught");
                prgdeletephoto!!.dismiss()

            }


        }.execute("POST", Constant.url + "student", jsondeletefile.toString().trim())
    }


    /* private fun setBackground(context: Context)
     {
         var background_img =  pref.getString("background","noimage")
         Log.d("backimg",background_img)
        /* if (background_img!!.contentEquals("null"))
         {
             background_image.setImageResource(R.drawable.background)
             Log.d("back","null")
         }*/
         else
         {

             var complete_url = mImageUrl+mTypeUrl+background_img
             Glide.with(context).load(complete_url).into(background_image)
             Log.d("back",complete_url)
         }
     }*/

   /* override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }*/


    private fun initText() {
        mImageProfile = findViewById(R.id.my_profile_icon)
        mImageProfile.setColorFilter(R.color.colorAccent)
        mLocationImage = findViewById(R.id.location_img_gray_go)
        mLocationImage.setColorFilter(R.color.color_silver)
        mSmileImage = findViewById(R.id.smile_image_go)
        mSmileImage.setColorFilter(R.color.color_silver)
        mClockImage = findViewById(R.id.clock_image_go)
        mClockImage.setColorFilter(R.color.color_silver)
        mOngoHeading = findViewById(R.id.tree_image_ongoing)
        mNoongoingText = findViewById(R.id.no_ongoing_txt)
        getRegular(mNoongoingText)
        mDateText = findViewById(R.id.ongoing_date)
        getRegular(mDateText)
        mStartTimeText = findViewById(R.id.ongo_start_time)
        getRegular(mStartTimeText)
        mEndTimeText = findViewById(R.id.ongo_end_time)
        getRegular(mEndTimeText)
        mDeliveryText = findViewById(R.id.ongoing_text_trip)
        getMedium(mDeliveryText)
        mPriceText = findViewById(R.id.ongoing_price)
        getRegular(mPriceText)
        mUserText = findViewById(R.id.ongoing_user)
        getRegular(mUserText)
        mPickupText = findViewById(R.id.address_ongoing_text)
        getRegular(mPickupText)
        mDropoffText = findViewById(R.id.dest_address_ongoing)
        getRegular(mDropoffText)


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            /*  if (requestCode==PICK_FILE_REQUEST)
              {
                  try {
                      mFileUtil = FileUtil()
                      uri_path = Uri.parse(data!!.data.toString())

                      val path = Environment.getExternalStoragePublicDirectory(
                          Environment.DIRECTORY_DOCUMENTS)
                      var file = File(uri_path!!.path.toString())
                      var file_name = file.name
                      Constant.original_file = File(mFileUtil.getPath(uri_path, this@DashBoardActivity))
                      path.mkdirs()
                      Log.d("RealPath", Constant.original_file.toString())


                      request_file =
                          RequestBody.create("application/pdf".toMediaTypeOrNull(), Constant.original_file)


                      /* path.mkdir()
                       file.createNewFile()*/
                      multipartBody =  MultipartBody.Part.createFormData("filenames", file_name, request_file)
                      Log.d("Multipart", multipartBody.body.contentLength().toString())


                      if (uri_path.toString().startsWith("content://")) {
                          var cursor: Cursor? = null;

                          try {
                              cursor = getContentResolver().query(uri_path, null, null, null, null);
                              if (cursor != null && cursor.moveToFirst()) {
                                  displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                  // doc_name.setText(displayName)

                                  //if (original_file.toString().contains("pdf")) {

                                  generateImageFromPdf(uri_path)
                                  //  }


                              }
                          } finally {
                              cursor!!.close();
                          }
                      } else if (uri_path.toString().startsWith("file://"))
                      {

                          displayName = file.getName()
                          //  mDocumentName = file.getName()
                          //  doc_name.setText(displayName)
                          generateImageFromPdf(uri_path)


                      }



                  }
                  catch (e: Exception)
                  {
                      e.printStackTrace()
                  }
              }*/
            if (requestCode == 1) {

                imageUri = data?.data

                mFileUtil = FileUtil()

                var file = File(imageUri!!.path.toString())

                try {
                    actualImage = data!!.data?.let {
                        FileUtilnew.from(this, it)?.also {
                            //  actualImageView.setImageBitmap(loadBitmap(it))
                            //  actualSizeTextView.text = String.format("Size : %s", getReadableFileSize(it.length()))
                            // clearImage()
                        }
                    }
                } catch (e: IOException) {
                    showError(this, "Failed to read picture data!")
                    e.printStackTrace()
                }

                // store in db
                val df: DateFormat = SimpleDateFormat("yyyyMMddhhmmss")
                //  var file_name = "cc" +file.name
                //   var file_name= "cc" + df.format(Date()).toString() + "." + fileextension
                var file_name = "";
                file_name = "pp" + df.format(Date()).toString() + "." + fileextension
                // pdf_img_cc.setImageURI(imageUri)
                profile_image.setImageURI(imageUri)

                imageView.setImageURI(imageUri)

                Constant.student_photo = file_name
                // img_layout_relative_cc.visibility = View.VISIBLE
                //  img_layout_relative_sig.visibility = View.GONE


                Constant.original_file = File(mFileUtil.getPath(imageUri!!, this@DashBoardActivity))
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                )

                displayName = file.getName()
                //  mDocumentName = file.getName()
                //mDocumentName = "test.jpg"
                // doc_name.setText(displayName)


                path.mkdirs()
                Log.d("RealPath", Constant.original_file.toString())

                request_file =
                    RequestBody.create("Image/jpeg".toMediaTypeOrNull(), Constant.original_file)


                /* path.mkdir()
                 file.createNewFile()*/
                multipartBody =
                    MultipartBody.Part.createFormData("filenames", file_name, request_file)
                Log.d("Multipart", multipartBody.body.contentLength().toString())

                //compressImage()
                encImage = FileUtil.getStringFile(DashBoardActivity@ this, Constant.original_file)
                UploadImages(
                    this@DashBoardActivity,
                    "uploadimagenew",
                    "test.png",
                    encImage,
                    Constant.accesstoken
                ).execute(Constant.url + "student")


            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun encodeImage(bm: Bitmap) {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        encImage = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Encode", encImage)
        var reszieb64 = resizeBase64Image(encImage)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun compressImage() {

        compressedImage = Compressor(this).compressToFile(actualImage)
        // uploadfilenew("test")
        val bitmap = Compressor(this).compressToBitmap(actualImage)
        //uploadImage(bitmap)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val bm = BitmapFactory.decodeFile(compressedImage.toString())
            val stream = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            val byteFormat = stream.toByteArray()
            encImage = Base64.encodeToString(byteFormat, Base64.DEFAULT)
            //uploadImage(bm)
        }
        UploadImages(
            this@DashBoardActivity,
            "uploadimagenew",
            "test.png",
            encImage,
            Constant.accesstoken
        ).execute(Constant.url + "student")

    }


    /*private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }*/

    private fun resizeBase64Image(base64image: String): String? {
        val encodeByte = Base64.decode(base64image.toByteArray(), Base64.DEFAULT)
        val options = BitmapFactory.Options()
        //        options.inPurgeable = true;
        var image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size, options)
        if (image.height <= 400 && image.width <= 400) {
            return base64image
        }
        image = Bitmap.createScaledBitmap(image, IMG_WIDTH, IMG_HEIGHT, false)
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        System.gc()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }


    private fun saveToInternalStorage(bitmapImage: Bitmap): String {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("error_creating", e.toString())
            }
        }
        return directory.absolutePath
    }

    lateinit var file: File
    private fun loadImageFromStorage(path: String) {
        try {
            file = File(path, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(file))
            // val img = findViewById<View>(R.id.imgPicker) as ImageView
            profile_image.setImageBitmap(b)

            // uploadfilenew("testfile")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("error_reading", e.toString())

        }
    }

    private fun uploadfilenew(filename: String) {

        try {
            // Set header
            //  val headers: Map<String, String> = HashMap()
            Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

            var headers: MutableMap<String?, String?> = mutableMapOf()

            headers.put(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36"
            )
            //headers.put("Authorization", "Bearer " + Constant.accesstoken)
            headers.put("Content-Type", "image/jpg")

            //val multipart = HttpPostMultipart("https://rcb.broadwayinfotech.net.au/beta/api/student", "utf-8", headers)
            val multipart = HttpPostMultipart(
                "https://rcb.broadwayinfotech.net.au/beta/api/uploadfile",
                "utf-8",
                headers
            )
            // Add form field
            // multipart.addFormField("type", "profileimage".trim().toString())
            // multipart.addFormField("methodname", "upload")
            // Add file
            //  var file = File(imageUri!!.path.toString())
            multipart.addFormField("filename", filename)
            compressedImage?.let { multipart.addFilePart("file", it) }
            // Print result
            val response: String = multipart.finish()
            println(response)
            Log.d("response", response.toString())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.d("exception", e.toString())
        }

    }


    private lateinit var encImage: String


    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadImage(bitmap: Bitmap) {
        /* val byteArrayOutputStream = ByteArrayOutputStream()
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
         val b = byteArrayOutputStream.toByteArray()*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val bm = BitmapFactory.decodeFile(compressedImage.toString())
            val stream = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            val byteFormat = stream.toByteArray()
            encImage = Base64.encodeToString(byteFormat, Base64.DEFAULT)
        }
        //  encImage = Base64.encodeToString(b, Base64.DEFAULT)
        val imgname = java.lang.String.valueOf(Calendar.getInstance().timeInMillis)
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constant.url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api: ApiInterface = retrofit.create(ApiInterface::class.java)
        Log.d("helloo", "$encImage   >>$imgname")
        Log.d("imggggg", "   >>$imgname")
        val call: Call<String> =
            api.uploadimg("Bearer " + Constant.accesstoken, imgname, encImage, "uploadimagenew")
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.i("Responsestring", response.body().toString())
                //Toast.makeText()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.i("onSuccessimg", response.body().toString())
                        Toast.makeText(
                            this@DashBoardActivity,
                            "Image Uploaded Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.i(
                            "onEmptyResponse",
                            "Returned empty response"
                        ) //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }


            override fun onFailure(call: Call<String?>, t: Throwable) {


            }
        })
    }


    internal class UploadImages(
        ctx: Context,
        mname: String,
        imagename: String,
        encode_string: String,
        maintoken: String?
    ) :
        AsyncTask<String, Void, String>() {

        val methodname: String = mname
        val Encode_String = encode_string
        val Main_token = maintoken
        val asyctx: Context = ctx
        private lateinit var progressuploading: ProgressDialog

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
                //jsonObject.put("imageString", Encode_String)
                // jsonObject.put("imageName", "+917358513024")
                jsonObject.put("filename", Constant.student_photo)
                //    jsonObject.put("fileextension",fileextension)

                jsonObject.put("doctype", "profileimage")
                jsonObject.put("methodname", methodname)
                jsonObject.put("strbase64dta", "data:image/jpg;base64," + Encode_String)
                // jsonObject.put("strbase64dta",Encode_String)
                //   jsonObject.put("notes",Notes)
                //  jsonObject.put("booking_id",ID)
                //    jsonObject.put("locationType",Location)
                //   jsonObject.put("imagename",random_num.toString()+".jpg")
                //   jsonObject.put("document",Encode_String)

                //val data: String = jsonObject.toString()
                //  val yourURL = Constants.API_BASE_PATH + "saveImage"
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
                success_text.text = "Image Uploaded successfully"
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


    @SuppressLint("SetTextI18n")
    private fun getStudentProfileDetaillatest(token: String, methodname: String, stdid: String?) {

        val jsongetprofiledetail = JSONObject()


        jsongetprofiledetail.put("methodname", "getProfileDetail")
        jsongetprofiledetail.put("student_id", stdid)

        Log.d("jsongetprofiledetail", jsongetprofiledetail.toString())


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
                    var gson = Gson()
                    objProfileResponse =
                        gson.fromJson(eventjsonobj.toString(), ProfileResponse::class.java);
                    Log.d("profile_data", objProfileResponse.toString());

                    user_fname_txt.text =
                        objProfileResponse!!.firstname + " " + objProfileResponse!!.middlename + " " + objProfileResponse!!.lastname
                 //   user_reg_txt.text = objProfileResponse!!.studentregno
                    user_reg_txt.setText("Registration No : "+ objProfileResponse.studentregno)
                   // user_roll_txt.text= objProfileResponse!!.studentrollno
                    user_roll_txt.setText("Roll No : "+ objProfileResponse.studentrollno)
                    if (objProfileResponse!!.studentstatus == "1") {
                        user_status_txt.text = "Active"
                    }

                    user_headerf_text.text = objProfileResponse!!.firstname

                    user_headerl_text.text =
                        objProfileResponse!!.middlename + " " + objProfileResponse!!.lastname

                    textView_gmail.text = objProfileResponse!!.studentemail

                    if (objProfileResponse.studentphoto.length > 0) {

                        Constant.student_photo = objProfileResponse.studentphoto
if (objProfileResponse.studentgenderid.toInt()==1){
    Picasso
        .get()
        .load(Constant.photourl + Constant.objProfileResponse.studentphoto)
        .resize(200, 200)
        .noFade()
        .placeholder(R.drawable.person_user)
        .into(profile_image);


    Picasso
        .get()
        .load(Constant.photourl + objProfileResponse.studentphoto)
        .resize(200, 200)
        .placeholder(R.drawable.person_user)
        .noFade()
        .into(imageView);
}else{
    Picasso
        .get()
        .load(Constant.photourl + Constant.objProfileResponse.studentphoto)
        .resize(200, 200)
        .noFade()
        .placeholder(R.drawable.female_profile)
        .into(profile_image);


    Picasso
        .get()
        .load(Constant.photourl + objProfileResponse.studentphoto)
        .resize(200, 200)
        .placeholder(R.drawable.female_profile)
        .noFade()
        .into(imageView);
}

                    }



                    if (objProfileResponse.studentcharactercertificate.length > 0) {
                        Constant.student_char_certificate =
                            objProfileResponse.studentcharactercertificate
                    }

                    if (objProfileResponse.studentsignature.length > 0) {
                        Constant.student_signature = objProfileResponse.studentsignature
                    }

                    if (objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.length > 0) {

                        Constant.student_entranceproof =
                            objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof

                    }


                    // objProfileResponse = response.body()!!
                    // LoadingScreen.hideLoading()
                    //  progressdashboard.dismiss()

                } else {
                    Toast.makeText(loginrcbActivity@ this, message, Toast.LENGTH_LONG).show()
                    //  progressdashboard.dismiss()
                    // LoadingScreen.hideLoading()

                }


            } catch (e: JSONException) {
                println("Exception caught");
            }


        }.execute("POST", Constant.url + "student", jsongetprofiledetail.toString().trim())
    }

    private fun getStudentProfileDetail(token: String, methodname: String, stdid: String?) {

        val getstudentprofile =
            ApiClient.getClient.getstudentprofiledetail(token, methodname, stdid)

        getstudentprofile.enqueue(object : Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

                Log.d("Profile Error", t.toString())
                //   LoadingScreen.hideLoading()

            }

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {

                try {
                    if (response.isSuccessful) {

                        if (response.body()!!.status == 1) {


                            /*  Log.d("responsebody_Profile", response.body().toString())
                              Log.d("status", response.body()!!.msg)
                              Log.d("msg", response.body()!!.status)
                              Log.d(
                                  "stdname",
                                  response.body()!!.firstname + response.body()!!.middlename + response.body()!!.lastname
                              )
                              Log.d("stdstatus", response.body()!!.studentstatus)
                              Log.d("stdregno", response.body()!!.studentregno)


                              user_fname_txt.text =
                                  response.body()!!.firstname + " " + response.body()!!.middlename + " " + response.body()!!.lastname
                              user_reg_txt.text = response.body()!!.studentregno

                              if (response.body()!!.studentstatus == "1") {
                                  user_status_txt.text = "Active"
                              }


                              objProfileResponse = response.body()!!*/
                            // profile_image


                            //   LoadingScreen.hideLoading()
                            //  var profiljsonobj = JSONObject(response.body().toString())

                            //  var gson = Gson()
                            //  objProfileResponse = gson.fromJson(profiljsonobj.toString(), ProfileResponse::class.java);

                            Constant.objProfileResponse = response.body()!!
                            Log.d("profile_data", objProfileResponse.toString());

                            user_fname_txt.text =
                                objProfileResponse!!.firstname + " " + objProfileResponse!!.middlename + " " + objProfileResponse!!.lastname
                      //      user_reg_txt.text = objProfileResponse!!.studentregno
                            user_reg_txt.setText("Registration No : "+ objProfileResponse.studentregno)
                         //   user_roll_txt.text= objProfileResponse!!.studentrollno
                            user_roll_txt.setText("Roll No : "+ objProfileResponse.studentrollno)

                            if (objProfileResponse!!.studentstatus == "1") {
                                user_status_txt.text = "Active"
                            }

                            user_headerf_text.text = objProfileResponse!!.firstname

                            user_headerl_text.text =
                                objProfileResponse!!.middlename + " " + objProfileResponse!!.lastname

                            textView_gmail.text = objProfileResponse!!.studentemail

                            // if (objProfileResponse.studentphoto.length>0) {

                            if (!objProfileResponse.studentphoto.isNullOrEmpty()) {
                                Constant.student_photo = objProfileResponse.studentphoto


                                if (objProfileResponse.studentgenderid.toInt()==1){
                                    Picasso
                                        .get()
                                        .load(Constant.photourl + objProfileResponse.studentphoto)
                                        .resize(200, 200)
                                        .placeholder(R.drawable.person_user)
                                        .noFade()
                                        .into(profile_image);

                                    Picasso
                                        .get()
                                        .load(Constant.photourl + objProfileResponse.studentphoto)
                                        .resize(200, 200)
                                        .placeholder(R.drawable.person_user)
                                        .noFade()
                                        .into(imageView);

                                }else{
                                    Picasso
                                        .get()
                                        .load(Constant.photourl + objProfileResponse.studentphoto)
                                        .resize(200, 200)
                                        .placeholder(R.drawable.female_profile)
                                        .noFade()
                                        .into(profile_image);

                                    Picasso
                                        .get()
                                        .load(Constant.photourl + objProfileResponse.studentphoto)
                                        .resize(200, 200)
                                        .placeholder(R.drawable.female_profile)
                                        .noFade()
                                        .into(imageView);
                                }

                            }


                            //if (objProfileResponse.studentcharactercertificate.length>0) {
                            if (!objProfileResponse.studentcharactercertificate.isNullOrEmpty()) {
                                Constant.student_char_certificate =
                                    objProfileResponse.studentcharactercertificate
                            }

                            //if (objProfileResponse.studentsignature.length>0) {
                            if (!objProfileResponse.studentsignature.isNullOrEmpty()) {
                                Constant.student_signature = objProfileResponse.studentsignature
                            }

                            //if (objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.length>0) {
                            if (!objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof.isNullOrEmpty()) {
                                Constant.student_entranceproof =
                                    objProfileResponse.studentnationallevelentrancequalify.student_entrance_proof

                            }
                            if (objProfileResponse.studentaddress.get(0).addressstate > 0) {
                                stateid = objProfileResponse.studentaddress.get(0).addressstate
                            }
                            if (objProfileResponse.studentaddress.get(1).addressstate > 0) {
                                stateidcor = objProfileResponse.studentaddress.get(1).addressstate
                            }

                        }
                    //    appfirsttime  = FileUtil.appGetFirstTimeRun(this@DashBoardActivity)
                  //    if(appfirsttime==0) {


                            getstudentcenterlatest()

                    //    }else {
                        //  Constant.arrstate = rcbdbviewModel.getallstatelist()!!
                    //      Log.d("all_state_offline",rcbdbviewModel.getallstatelist()!!.size.toString())
                        //  Constant.arrstudentseason = rcbdbviewModel.getallseasonlist()!!
                     //     Log.d("all_season_offlne", rcbdbviewModel.getallseasonlist()!!.size.toString())
                       //   Constant.arrstudentprogram = rcbdbviewModel.getallprogramlist()!!
                  //        Log.d("all_program_offline",rcbdbviewModel.getallprogramlist()!!.size.toString())
                       //   Constant.ddvalues = rcbdbviewModel.getalldropdownlist()!!
                    //      Log.d("all_dropdown_offline", rcbdbviewModel.getalldropdownlist()!!.size.toString())
                      //    Constant.arrstudentcenter = rcbdbviewModel.getallcenterlist()!!
                    //      Log.d("all_center_offline", rcbdbviewModel.getallcenterlist()!!.size.toString())
                     //     Constant.arrstudentbranch = rcbdbviewModel.getallbranchlist()!!
                   //       Log.d("all_branch_offline",rcbdbviewModel.getallbranchlist()!!.size.toString())
                        //  Constant.arracademicyear =rcbdbviewModel.getallaylist()!!
                     //     Log.d("all_academiy_year",rcbdbviewModel.getallaylist()!!.size.toString())
                  //    }

                        /* else {
                             Toast.makeText(loginrcbActivity@ this, response.body()!!.msg, Toast.LENGTH_LONG).show()

                         }*/
                    }
                   // LoadingScreen.hideLoading()
                } catch (e: Exception) {
                    Log.d("Excepition", e.toString())
                }
            }

        });


    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant", "ServiceCast")
    override fun onClick(v: View?) {
        if (v == home_layout) {
            drawerLayout.closeDrawer(Gravity.LEFT, false);
        } else if (v == open_job_layout) {

        } else if (v == ongoing_job_layout) {

        } else if (v == active_job_layout) {

        } else if (v == past_job_layout) {

        } else if (v == my_profile_layout) {

            drawerLayout.closeDrawer(Gravity.LEFT, false);


        } else if (v == abt_layout) {

        } else if (v == logout_layout) {
            val dialogue = Dialog(this@DashBoardActivity)
            dialogue.setContentView(R.layout.logout_dialogue)
            val yes_bttn: AppCompatButton = dialogue.findViewById(R.id.yes_button)
            yes_bttn.setOnClickListener {
                //  val pref : SharedPreferences = getSharedPreferences("Token", Context.MODE_PRIVATE)
                //   val edit : SharedPreferences.Editor = pref.edit()
                mPrefrences = getSharedPreferences("LoginData", Context.MODE_PRIVATE)
                mEditor = mPrefrences.edit()
                mEditor.clear().commit()
                //  finish()
                dialogue.dismiss()
                startActivity(Intent(this@DashBoardActivity, loginrcbActivity::class.java))
                overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
                drawerLayout.closeDrawer(Gravity.LEFT, false)
            }
            val no_bttn: AppCompatButton = dialogue.findViewById(R.id.no_button)
            no_bttn.setOnClickListener {
                dialogue.dismiss()
                drawerLayout.closeDrawer(Gravity.LEFT, false)
            }
            dialogue.window!!.attributes.windowAnimations = R.style.Animation
            dialogue.show()

        } else if (v == support_job_layout) {
            /*  startActivity(Intent(this@DashBoardActivity,HelpActivity::class.java))
              overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
              drawerLayout.closeDrawer(Gravity.LEFT, false)*/
        } else if (v == open_layout) {
            //startActivity(Intent(this@DashBoardActivity,OpenDeliveryActivity::class.java))
            //  overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

            //   startActivity(Intent(this@DashBoardActivity,PersonalDetailActivity::class.java))
            val personalintent = Intent(this@DashBoardActivity, PersonalDetailActivity::class.java)
            personalintent.putExtra("firstname", objProfileResponse.firstname)
            personalintent.putExtra("middlename", objProfileResponse.middlename)
            personalintent.putExtra("lastname", objProfileResponse.lastname)
            personalintent.putExtra("dob", objProfileResponse.studentdob)
            personalintent.putExtra("phone", objProfileResponse.studentphone)
            personalintent.putExtra("email", objProfileResponse.studentusername)
            startActivity(personalintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
             {
                // displayNotification()

             }*/

            // Set the info for the views that show in the notification panel.


        } else if (v == active_layout) {
            //   startActivity(Intent(this@DashBoardActivity,GuardiandetailActivity::class.java))
            val guardianintent = Intent(this@DashBoardActivity, GuardiandetailActivity::class.java)
            guardianintent.putExtra(
                "fathername",
                objProfileResponse.guradiandetail.student_father_name
            )
            guardianintent.putExtra(
                "mothername",
                objProfileResponse.guradiandetail.student_mother_name
            )
            guardianintent.putExtra("category", objProfileResponse.guradiandetail.student_category)
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
            guardianintent.putExtra("state", stateid)
            guardianintent.putExtra("statecor", stateidcor)
            guardianintent.putExtra("emergencycontact", objProfileResponse.studentemergencyno)
            guardianintent.putExtra("relation", objProfileResponse.studentrelation)
            guardianintent.putExtra("adhaarno", objProfileResponse.studentadharno)
            guardianintent.putExtra("passportno", objProfileResponse.studentpassportno)
            Log.d("statepassed", stateid.toString())
            //guardianintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(guardianintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        } else if (v == past_layout) {
            // startActivity(Intent(this@DashBoardActivity,AcademicdetailActivity::class.java))
            val academiintent = Intent(this@DashBoardActivity, AcademicdetailActivity::class.java)
            //       academiintent.putExtra("arrexampassed", objProfileResponse.studentacademicrecord)
            startActivity(academiintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        } else if (v == bankdetail_layout) {
            val academiintent = Intent(this@DashBoardActivity, BankdetailActivity::class.java)
            startActivity(academiintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        } else if (v == academiccal_layout) {
            val academiintent = Intent(this@DashBoardActivity, AcdemiccalendarActivity::class.java)
            startActivity(academiintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        } else if (v == fee_layout) {
            val academiintent = Intent(this@DashBoardActivity, StudentfeeActivity::class.java)
            startActivity(academiintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        } else if (v == assignment_layout) {
            val academiintent = Intent(this@DashBoardActivity, AssignmentActivity::class.java)
            startActivity(academiintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)
        }else if (v == holiday_layout) {
            val holidayintent = Intent(this@DashBoardActivity, HolidaysActivity::class.java)
            startActivity(holidayintent)
            overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

}
    }

    @SuppressLint("WrongConstant")
    override fun onResume() {
        super.onResume()

    }


    private fun getstudentcenterlatest() {

        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val getstdcenter = ApiClient.getClient.getstudentcenter("Bearer "+Constant.accesstoken, "getstudentcenter")

        getstdcenter.enqueue(object:Callback<getstudentcenter> {
            override fun onResponse(
                call: Call<getstudentcenter>,
                response: Response<getstudentcenter>
            ) {
                try {
                    if (response.isSuccessful) {
                        //        Constant.arrstudentcenter = response.body()!!.centers
                        rcbdbviewModel.setcenterlist(response.body()!!.centers)
                    }
                    getstate()
                    //  getStudentProfileDetail("Bearer "+Constant.accesstoken, "getProfileDetail", strstudentid)
                 //   getStudentSeason("Bearer "+Constant.accesstoken,"getseason")
                }catch(e:Exception){
                    Log.d("exception",e.toString())
                }
            }

            override fun onFailure(call: Call<getstudentcenter>, t: Throwable) {

            }

        })
    }
    private fun getstate() {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!


        val callstate = ApiClient.getClient.getstates("Bearer "+Constant.accesstoken, "getstate")

        callstate.enqueue(object:Callback<strstateresponse> {
            override fun onResponse(call: Call<strstateresponse>, response: Response<strstateresponse>) {
                try {
                    if(response.isSuccessful) {
                        //    Constant.arrstate = response.body()!!.States as ArrayList<State>
                        rcbdbviewModel.setstatelist(response.body()!!.States)

                    }
                    //  Log.d("states",Constant.arrstate.toString())
                    getdropdownlatest()

                }catch(e:Exception) {
                    Log.d("Exception",e.toString())
                }
            }

            override fun onFailure(call: Call<strstateresponse>, t: Throwable) {
                Log.d("failure",t.message.toString())
            }

        })
    }



    private fun getdropdownlatest() {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val calldropdownapi = ApiClient.getClient.getdropdownvalue("Bearer "+Constant.accesstoken,"dropdownvalues")
        calldropdownapi.enqueue(object:Callback<getdropdownvalue> {
            override fun onResponse(call: Call<getdropdownvalue>, response: Response<getdropdownvalue>) {
                try {
                    if (response.isSuccessful) {

                        Log.d("responsebody",response.body().toString())

                        //        Constant.ddvalues= response.body()!!.Data
                        rcbdbviewModel.setdropdownlist(response.body()!!.Data)
                        // getstudentbrnach()
                    }
                    else {
                        Toast.makeText(this@DashBoardActivity, "No Record Found", Toast.LENGTH_LONG).show()

                    }

                getstudentbrnachlatest(Constant.objProfileResponse.stdinstituteid,Constant.objProfileResponse.stdprgname)

                }catch(e:Exception) {
                    Log.d("exception",e.toString())
                }
            }

            override fun onFailure(call: Call<getdropdownvalue>, t: Throwable) {

            }

        })

    }


    private fun getstudentbrnachlatest(centerid:String,programid:String)
    {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val getstudentbranch = ApiClient.getClient.getstudentbranch("Bearer "+Constant.accesstoken,"studentbranch",centerid,programid)

        getstudentbranch.enqueue(object:Callback<getstudentbranch>{
            override fun onResponse(
                call: Call<getstudentbranch>,
                response: Response<getstudentbranch>
            ) {
                try {
                    if (response.isSuccessful) {

                        Log.d("responsebody",response.body().toString())

                        //        Constant.arrstudentbranch= response.body()!!.Branch

                        rcbdbviewModel.setbranchlist(response.body()!!.Branch)
                    }
                    else {
                        Toast.makeText(this@DashBoardActivity, "No Record Found", Toast.LENGTH_LONG).show()

                    }

                 getacdemicyearlatest()

                }catch(e:Exception) {
                    Log.d("exception",e.toString())
                }

            }

            override fun onFailure(call: Call<getstudentbranch>, t: Throwable) {


            }

        }

        )

    }



    private fun getacdemicyearlatest() {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val getacdemicyear = ApiClient.getClient.getacadamicyear("Bearer " +Constant.accesstoken, "getacademicyear")

        getacdemicyear.enqueue(object:Callback<getacademicyear>
        {
            override fun onResponse(
                call: Call<getacademicyear>,
                response: Response<getacademicyear>
            ) {

                try {
                    if (response.isSuccessful) {

                        Log.d("responsebody",response.body().toString())

                        //        Constant.arracademicyear= response.body()!!.AcademicYear

                        rcbdbviewModel.setaylist(response.body()!!.AcademicYear)
                    }
                    else {
                        Toast.makeText(this@DashBoardActivity, "No Record Found", Toast.LENGTH_LONG).show()

                    }

                    // getacdemicyear()
                    // getstudentprogram()
                  getstudentprogramlatest(Constant.objProfileResponse.stdinstituteid)

                }catch(e:Exception) {
                    Log.d("exception",e.toString())
                }


            }

            override fun onFailure(call: Call<getacademicyear>, t: Throwable) {

            }

        })

    }

    private fun getstudentprogramlatest(centerid:String) {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val getstdprg = ApiClient.getClient.getstudentprogram("Bearer "+Constant.accesstoken , "getstudentprogram",centerid)
        getstdprg.enqueue(object:Callback<getstudentprogram> {
            override fun onResponse(
                call: Call<getstudentprogram>,
                response: Response<getstudentprogram>
            ) {
                try {
                    if(response.isSuccessful) {
                        //        Constant.arrstudentprogram = response.body()!!.Programs
                        rcbdbviewModel.setprogramlist(response.body()!!.Programs)
                        Constant.strprgmid = ArrayList()
                        for (program in response.body()!!.Programs) {
                            Log.d("program.pm_id", program.pm_id.toString())
                            Constant.strprgmid.add(program.pm_id)


                        }

                            Log.d("programlist", Constant.strprgmid.size.toString())


                    }

                }catch(e:Exception){
                    Log.d("Exception",e.toString())
                }
                //   getstudentcenterlatest()
                getStudentSeason("Bearer "+Constant.accesstoken,"getseason")
            }

            override fun onFailure(call: Call<getstudentprogram>, t: Throwable) {

            }

        })

    }




    private fun getStudentSeason(token:String, methodname:String) {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val studentsession = ApiClient.getClient.getseason(token,methodname)

        studentsession.enqueue(object:Callback<SeasonResponse> {
            override fun onResponse(
                call: Call<SeasonResponse>,
                response: Response<SeasonResponse>
            ) {
                try {
                    if (response.isSuccessful) {
                        //        Constant.arrstudentseason = response.body()!!.Season
                        rcbdbviewModel.setseasonlist(response.body()!!.Season)
                        /*  splashprg!!.dismiss()
                          Handler().postDelayed({
                              val mainIntent = Intent(this@SplashActivity, loginrcbActivity::class.java)
                              startActivity(mainIntent)
                              finish()
                              overridePendingTransition(R.anim.slidein_right, R.anim.slide_out_left)

                          }, SPLASH_DELAY)*/


                    }
                    //    Log.d("studentseason", Constant.arrstudentseason.size.toString())
                    getstudentspecialization("Bearer "+Constant.accesstoken,"getspecialization",
                        objProfileResponse.stdprgname,objProfileResponse.stdinstituteid,Constant.objProfileResponse.stdbranch)
                }catch(e:Exception){
                    Log.d("exception",e.toString())
                    //   splashprg!!.dismiss()
                }
            }

            override fun onFailure(call: Call<SeasonResponse>, t: Throwable) {

                Log.d("failure",t.message.toString())
                //   splashprg!!.dismiss()
            }

        })


    }


    private fun getstudentspecialization(token:String, methodname:String,programid:String,centerid:String,branchid:String) {
        Constant.accesstoken = mPrefrences.getString("accesstoken", "")!!

        val studentsp = ApiClient.getClient.getsp(token,methodname,programid,centerid,branchid)

        studentsp.enqueue(object:Callback<getspecialization> {
            override fun onResponse(
                call: Call<getspecialization>,
                response: Response<getspecialization>
            ) {
                try {
                    if (response.isSuccessful) {
                        rcbdbviewModel.setspecialization(response.body()!!.Specialization)

                        LoadingScreen.hideLoading()


                    }
                }catch(e:Exception) {
                    Log.d("exception",e.toString())
                }
            }

            override fun onFailure(call: Call<getspecialization>, t: Throwable) {
                Log.d("failure",t.message.toString())
                LoadingScreen.hideLoading()

            }

        })



    }


    override fun onStop() {
        super.onStop()
      /*  if (LoadingScreen != null) {
             LoadingScreen.hideLoading()
         }*/
    }


    override fun onDestroy() {

        super.onDestroy()

        /* if (LoadingScreen != null) {
             LoadingScreen.hideLoading()
         }*/
    }

    private fun getStartAmPm(start_time: Int) {
        var open_time = start_time
        //var close_time = end_time
        if (start_time >= 12) {
            if (start_time.equals(12)) {
                mStartTime = open_time.toString() + "P.M"
                mStartTimeText.text = mStartTime
            } else {
                open_time = (open_time) - (12)
                mStartTime = open_time.toString() + "P.M"
                mStartTimeText.text = mStartTime

            }

        } else {
            mStartTime = open_time.toString() + "A.M"
            mStartTimeText.text = mStartTime
        }
    }

    private fun getEndAmPm(end_time: Int) {

        var close_time = end_time
        if (end_time >= 12) {
            if (end_time.equals(12)) {
                mEndTime = close_time.toString() + "P.M"
                mEndTimeText.text = mEndTime

            } else {
                close_time = (close_time) - (12)
                mEndTime = close_time.toString() + "P.M"
                mEndTimeText.text = mEndTime

            }

        } else {
            mEndTime = close_time.toString() + "A.M"
            mEndTimeText.text = mEndTime
        }
    }

    private fun convertDate(current_time: Long): String {
        val netDate = Date(current_time * 1000)
        Log.d("newdate", netDate.toString())


        try {
            val date: Date = Date(netDate.toString())
            val sdf: DateFormat = SimpleDateFormat("dd MMM yyy")
            sdf.timeZone = TimeZone.getTimeZone("Australia/Sydney")
            date_final = sdf.format(date)
            mDateText.text = date_final
            Log.d("Date Final", date_final.toString())


        } catch (e: Exception) {
            Log.d("DateError", e.message.toString())
        }
        return date_final
    }


    /* private  fun getDeviceId()
     {
       //  Constants.devid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
       //  Constants.devmodel = android.os.Build.MODEL


       //  Log.d("deviceId",Constants.devid+"Model " +Constants.devmodel )
         FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
             this
         ) { instanceIdResult: InstanceIdResult ->
             val newToken = instanceIdResult.token
          //   Constants.devkey = newToken
             Log.d("newToken", newToken)
             getPreferences(Context.MODE_PRIVATE).edit()
                 .putString("fb", newToken).apply()
         }
     }*/


    private fun textStyle() {

        open_job_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        active_job_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        past_job_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        passed_job_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        ongoing_job_icon.setColorFilter(ContextCompat.getColor(this, android.R.color.black))
        //  person_img.setColorFilter(ContextCompat.getColor(this,android.R.color.white))
        // mHeadingText = findViewById(R.id.dash_heading_txt) as TextView
        getMedium(ongoing_text_header)
        // getMedium(mHeadingText)

        getMedium(user_fname_txt)
        //getMedium(user_lname_txt)
        getRegular(user_reg_txt)
        getRegular(open_txt)
        // getRegular(deliveries_txt)
        getRegular(active_txt)
        //  getRegular(delivery_txt)
        getRegular(past_txt)
        getRegular(passed_job_txt)
        getRegular(ongoing_job_txt)
        getRegular(support_job_txt)
        // getRegular(deliverie_txt)
        // getRegular(my_txt)
        //  getRegular(profile_txt)
        getMedium(user_headerf_text)
        getMedium(user_headerl_text)
        getRegular(textView_gmail)
        getRegular(home_text)
        getRegular(open_job_txt)
        getRegular(active_job_txt)
        getRegular(past_job_txt)
        getRegular(my_profile_txt)
        getRegular(logout_txt)
        //getRegular(push_notification)
    }

    public fun getMedium(text: TextView) {

        mMedium = Typeface.createFromAsset(assets, "montserrat/Montserrat-Medium.otf")
        text.setTypeface(mMedium)
    }

    public fun getRegular(text: TextView) {
        mRegular = Typeface.createFromAsset(assets, "montserrat/Montserrat-Regular.otf")
        text.setTypeface(mRegular)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slideout_right)
    }


    public fun callaccessApi() {

        try {
            val ja = JSONObject()

            val callaccesapi =
                ApiClient.getClient.getaccesstoken("vivek.chandra@broadwayinfotech.com", "123456")
            callaccesapi.enqueue(object : Callback<RequestAccess> {

                override fun onFailure(call: Call<RequestAccess>, t: Throwable) {

                    Log.d("LoginError", t.toString())
                    Toast.makeText(
                        this@DashBoardActivity,
                        "Something Went Wrong",
                        Toast.LENGTH_LONG
                    ).show()

                }

                override fun onResponse(
                    call: Call<RequestAccess>,
                    response: Response<RequestAccess>
                ) {

                    try {
                        if (response.isSuccessful) {

                            Log.d("responsebody", response.body().toString())
                            Log.d("status", response.body()!!.status);
                            Log.d("message", response.body()!!.message);
                            Log.d("token", response.body()!!.access_token);

                            Constant.accesstoken = response.body()!!.access_token


                        }
                    } catch (e: Exception) {
                        Log.d("Exception", e.toString())
                    }
                }

            });
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
        }

    }


    fun sendFileToServer(filename: String, targetUrl: String): String? {
        var filename = filename
        var response = "error"
        Log.e("Image filename", filename)
        Log.e("url", targetUrl)
        var connection: HttpURLConnection? = null
        var outputStream: DataOutputStream? = null
        // DataInputStream inputStream = null;
        val pathToOurFile = filename
        val lineEnd = "\r\n"
        val twoHyphens = "--"
        val boundary = "*****"
        val df: DateFormat = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss")
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        val buffer: ByteArray
        val maxBufferSize = 1 * 1024
        try {
            val fileInputStream = FileInputStream(
                File(
                    pathToOurFile
                )
            )
            val url = URL(targetUrl)
            connection = url.openConnection() as HttpURLConnection

            // Allow Inputs & Outputs
            connection.doInput = true
            connection!!.doOutput = true
            connection.useCaches = false
            connection.setChunkedStreamingMode(1024)
            // Enable POST method
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data;boundary=$boundary"
            )
            outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(twoHyphens + boundary + lineEnd)
            var connstr: String? = null
            connstr = ("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                    + pathToOurFile + "\"" + lineEnd)
            Log.i("Connstr", connstr)
            outputStream.writeBytes(connstr)
            outputStream.writeBytes(lineEnd)
            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = ByteArray(bufferSize)

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            Log.e("Image length", bytesAvailable.toString() + "")
            try {
                while (bytesRead > 0) {
                    try {
                        outputStream.write(buffer, 0, bufferSize)
                    } catch (e: OutOfMemoryError) {
                        e.printStackTrace()
                        response = "outofmemoryerror"
                        return response
                    }
                    bytesAvailable = fileInputStream.available()
                    bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                response = "error"
                return response
            }
            outputStream.writeBytes(lineEnd)
            outputStream.writeBytes(
                twoHyphens + boundary + twoHyphens
                        + lineEnd
            )

            // Responses from the server (code and message)
            val serverResponseCode = connection.responseCode
            val serverResponseMessage = connection.responseMessage
            Log.i("Server Response Code ", "" + serverResponseCode)
            Log.i("Server Response Message", serverResponseMessage)
            if (serverResponseCode == 200) {
                response = "true"
            }
            var CDate: String? = null
            val serverTime = Date(connection.date)
            try {
                CDate = df.format(serverTime)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e("Date Exception", e.message + " Parse Exception")
            }
            Log.i("Server Response Time", CDate + "")
            filename = (CDate
                    + filename.substring(
                filename.lastIndexOf("."),
                filename.length
            ))
            Log.i("File Name in Server : ", filename)
            fileInputStream.close()
            outputStream.flush()
            outputStream.close()
            outputStream = null
        } catch (ex: java.lang.Exception) {
            // Exception handling
            response = "error"
            Log.e("Send file Exception", ex.message + "")
            ex.printStackTrace()
        }
        return response
    }


    private fun convertStreamToString(`is`: InputStream): String? {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

}
