package com.app.rcb.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Base64OutputStream
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import com.app.rcb.BuildConfig

import com.app.rcb.util.Constant.Companion.PREFS_NAME
import java.io.*


public class FileUtil {

    @SuppressLint("NewApi")
    fun getPath(uri: Uri, context: Context): String? {
        var uri: Uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null

        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                when (type) {
                    "image" -> uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    "document" -> uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(
                    split[1]
                )
            }
        }
        if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA
            )
            try {
                context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
                    .use({ cursor ->
                        if (cursor != null && cursor.moveToFirst()) {
                            val columnIndex: Int =
                                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                            return cursor.getString(columnIndex)
                        }
                    })
            } catch (e: Exception) {
                Log.e("on getPath", "Exception", e)
            }
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }
        return null
    }





    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }


    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }


    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    companion object {

        fun createLink(
            targetTextView: TextView, completeString: String,
            partToClick: String, clickableAction: ClickableSpan?
        ): TextView? {
            val spannableString = SpannableString(completeString)

            // make sure the String is exist, if it doesn't exist
            // it will throw IndexOutOfBoundException
            val startPosition = completeString.indexOf(partToClick)
            val endPosition = completeString.lastIndexOf(partToClick) + partToClick.length
            spannableString.setSpan(
                clickableAction, startPosition, endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            targetTextView.text = spannableString
            targetTextView.movementMethod = LinkMovementMethod.getInstance()
            return targetTextView
        }


        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }


        fun getMimeType(context: Context, uri: Uri): String {
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
        }


        fun getStringFile(context: Context,f: File): String {
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
        }


       fun showError(context: Context,errorMessage: String) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }


        fun appGetFirstTimeRun(context: Context): Int {
            //Check if App Start First Time

            //  var settings = getSharedPreferences(PREFS_NAME, 0)

            val appPreferences =context.getSharedPreferences(PREFS_NAME, 0)
            val appCurrentBuildVersion = BuildConfig.VERSION_CODE
            val appLastBuildVersion = appPreferences.getInt("app_first_time", 0)

            //Log.d("appPreferences", "app_first_time = " + appLastBuildVersion);

            if (appLastBuildVersion == appCurrentBuildVersion) {
                return 1 //ya has iniciado la appp alguna vez

            } else {
                appPreferences.edit().putInt("app_first_time",
                    appCurrentBuildVersion).apply()
                return if (appLastBuildVersion == 0) {
                    0 //es la primera vez
                } else {
                    2 //es una versión nueva
                }
            }
        }




    }
}