package com.premierticketbookingapp.apicall

import android.os.AsyncTask
import com.app.rcb.util.Constant
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HttpTask(callback: (String?) -> Unit) : AsyncTask<String, Unit, String>()  {

    var callback = callback
    val TIMEOUT = 30*1000




    override fun doInBackground(vararg params: String): String? {
        val url = URL(params[1])
        val httpClient = url.openConnection() as HttpURLConnection
        httpClient.setReadTimeout(TIMEOUT)
        httpClient.setConnectTimeout(TIMEOUT)
        httpClient.requestMethod = params[0]




            httpClient.setRequestProperty("Authorization", "Bearer " + Constant.accesstoken)

        if (params[0] == "POST") {
            httpClient.instanceFollowRedirects = false
            httpClient.doOutput = true
            httpClient.doInput = true
            httpClient.useCaches = false
            httpClient.setRequestProperty("Content-Type", "application/json")
          //  httpClient.setRequestProperty("Accept", "application/json");
           // httpClient.setRequestProperty("charset", "utf-8")
           // httpClient.setRequestProperty("Content-length", params[2].size.toString())


        }
        try {
            if (params[0] == "POST") {
                httpClient.connect()
                val os = httpClient.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(params[2])
                writer.flush()
                writer.close()
                os.close()
            }

             if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = BufferedInputStream(httpClient.inputStream)
                val data: String = readStream(inputStream = stream)
                 val map = httpClient.getHeaderFields()

               /*  for (entry in map.entries) {
                     println("Key : " + entry.key + " ,Value : " + entry.value)
                 }*/
                 /*if(url.toString().contains("event", ignoreCase = true)) {
                     Const.phpsessionid = map.get("Set-Cookie").toString();
                     Const.phpsessionid = Const.phpsessionid.substring(1, Const.phpsessionid.length - 1);
                 }*/
               /*  if(url.toString().equals("http://dev.premierticket.co/BOOKINGAPI/events", ignoreCase = true)) {
                     Const.phpsessionid = map.get("Set-Cookie").toString();
                     Const.phpsessionid = Const.phpsessionid.substring(1, Const.phpsessionid.length - 1);
                 }*/
               /* else if(url.toString().equals("http://dev.premierticket.co/BOOKINGAPI/eventdetail", ignoreCase = true)) {
                     Const.phpsessionid = map.get("Set-Cookie").toString();
                     Const.phpsessionid = Const.phpsessionid.substring(1, Const.phpsessionid.length - 1);
                 }*/

                return data
            }

             else {
                println("ERROR ${httpClient.responseCode}")
            }



        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            httpClient.disconnect()
        }

        return null
    }

    fun readStream(inputStream: BufferedInputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        return stringBuilder.toString()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        callback(result)
    }
}
