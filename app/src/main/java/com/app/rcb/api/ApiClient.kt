package Apirequest

import com.app.rcb.api.ApiInterface
import com.app.rcb.util.Constant
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiClient {


   // var BASE_URL: String = "https://commissionflowv2.broadwayinfotech.net.au/dev/api/"

    val getClient: ApiInterface
        get() {

            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val interceptor = HttpLoggingInterceptor()
         //   interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        //    interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
          //  interceptor.setLevel = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().
                connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).
            addInterceptor(interceptor).connectionPool(ConnectionPool(0,5,TimeUnit.MINUTES))
                .protocols(listOf(Protocol.HTTP_1_1))
           //    .authenticator(SupportInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.url)
                    .client(client)


                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(ApiInterface::class.java)

        }

    val getClientStudent: ApiInterface
        get() {

            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val interceptor = HttpLoggingInterceptor()
            //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
           // interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            //  interceptor.setLevel = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().

            addInterceptor(interceptor)

                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constant.url)
                    .client(client)


                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(ApiInterface::class.java)

        }

    fun getInstance(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
            //    .authenticator(SupportInterceptor())
            .build()


        return Retrofit.Builder().baseUrl(Constant.url)
            // .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}


