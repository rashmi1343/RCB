package com.app.rcb.viewmodel

import Apirequest.ApiClient
import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.rcb.api.ApiInterface
import com.app.rcb.db.entity.*
import com.app.rcb.repository.rcbrepository
import com.app.rcb.response.*
import com.app.rcb.util.Constant
import kotlinx.coroutines.*

class rcbviewModel(application: Application) : AndroidViewModel(application) {


    private val mRepository = rcbrepository(application)

    var job: Job? = null
    val usersLoadError = MutableLiveData<String?>()
    val loading = MutableLiveData<Boolean>()
    val statelistapi = MutableLiveData<strstateresponse>()

    val dropdownlistapi = MutableLiveData<getdropdownvalue>()
    val Seasonlistapi = MutableLiveData<SeasonResponse>()
    val Programlistapi = MutableLiveData<getstudentprogram>()
    val Studentcenterlistapi = MutableLiveData<getstudentcenter>()
    val branchlistapi = MutableLiveData<getstudentbranch>()
    val aylistapi = MutableLiveData<getacademicyear>()
    val holidaylistapi = MutableLiveData<HolidaysResponse>()

    val splistapi = MutableLiveData<getspecialization>()

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    init {
        getholidaysfromApi()
    }

    fun getallstatelist(): List<State>? {

        return mRepository.getAllState()
    }

    fun getalldropdownlist(): List<Dropdown>? {

        return mRepository.getAlldropdown()
    }

    fun setstatelist(stateList: List<State>) {
        mRepository?.insertstateList(stateList)
        Log.d("state", "inserted")
    }

    fun setdropdownlist(dropdownlist: List<Dropdown>) {
        mRepository?.insertdropdownlist(dropdownlist)
        Log.d("dropdown", "inserted")
    }


    fun setseasonlist(seasonlist: List<Season>) {
        mRepository?.insertseasonlist(seasonlist)
        Log.d("season", "inserted")

    }

    fun setprogramlist(programlist: List<Program>) {
        mRepository?.insertprogramlist(programlist)
        Log.d("program", "inserted")

    }

    fun setcenterlist(centerlist: List<Center>) {
        mRepository?.insertcenterlist(centerlist)
        Log.d("center", "inserted")

    }

    fun setbranchlist(branchlist: List<Branch>) {
        mRepository?.insertbranchlist(branchlist)
        Log.d("branch", "inserted")

    }

    fun setaylist(aylist: List<Ay>) {
        mRepository?.insertaylist(aylist)
        Log.d("aylist", "inserted")

    }

    fun setspecialization(specializationlist: ArrayList<Specialization>) {
        //mRepository?.insert
        mRepository?.insertspecialization(specializationlist)
        Log.d("specailization", "inserted")
    }

    fun getStateListfromApi() {

        loading.value = true


        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requeststate = ApiInterface.CommonParam("getstate")
            val rcbApiStaterequest =
                ApiClient.getClient.getstatesnew("Bearer " + Constant.accesstoken, requeststate)

            Log.d("allstates", rcbApiStaterequest.States.toString())
            statelistapi.postValue(rcbApiStaterequest)
            //Inserting in room in batch all data
            setstatelist(rcbApiStaterequest.States)
            //

        }
        usersLoadError.value = ""
        loading.value = false
        // listOfState = Constant.arrstate
//        return listOfState
    }

    //
    fun getholidaysfromApi(): ArrayList<Holiday>? {
        var holidayarr = ArrayList<Holiday>()

        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
    //        val holidayrequest = ApiInterface.CommonParamOther("getholiday",Constant.objProfileResponse.stdprgname.toInt())
            val holidayrequest = ApiInterface.CommonParamOther("getholiday",Constant.strprgmid)
            val holidaysresponse = ApiClient.getClient.getholidayslist(
                "Bearer " + Constant.accesstoken,
                holidayrequest
            )
            holidayarr = holidaysresponse.holiday


            Log.d("holidayarr",holidayarr.toString())
            holidaylistapi.postValue(holidaysresponse)

        }
        usersLoadError.value = ""
        loading.value = false
        return holidayarr

    }


    fun getdropdownListfromApi() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestdropdown = ApiInterface.CommonParam("dropdownvalues")
            val rcbApidropdownresponse = ApiClient.getClient.getdropdownnew(
                "Bearer " + Constant.accesstoken,
                requestdropdown
            )

            Log.d("alldropdown", rcbApidropdownresponse.Data.toString())
            mRepository?.insertdropdownlist(rcbApidropdownresponse.Data)
            dropdownlistapi.postValue(rcbApidropdownresponse)
        }
        usersLoadError.value = ""
        loading.value = false
    }


    fun getallseasonlist(): List<Season>? {

        return mRepository.getAllseason()
    }

    fun getallseasonListfromApi() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestseason = ApiInterface.CommonParam("getseason")
            val rcbApiseasonresponse =
                ApiClient.getClient.getseasonnew("Bearer " + Constant.accesstoken, requestseason)
            Seasonlistapi.postValue(rcbApiseasonresponse)
            Log.d("allseason", rcbApiseasonresponse.Season.toString())
            mRepository?.insertseasonlist(rcbApiseasonresponse.Season)

        }
        usersLoadError.value = ""
        loading.value = false

    }

    fun getallprogramlist(): List<Program>? {

        return mRepository.getAllprogram()
    }

    fun getallprogramlistfromApi() {

        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestprogram = ApiInterface.CommonParam("getstudentprogram")
            val rcbApiprogramresponse =
                ApiClient.getClient.getprogramnew("Bearer " + Constant.accesstoken, requestprogram)

            Programlistapi.postValue(rcbApiprogramresponse)
            Log.d("allseason", rcbApiprogramresponse.Programs.toString())
            mRepository?.insertprogramlist(rcbApiprogramresponse.Programs)

        }
        usersLoadError.value = ""
        loading.value = false
    }

    fun getallcenterlist(): List<Center>? {
        return mRepository.getAllcenter()
    }

    fun getallcenterListfromApi() {

        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestcenter = ApiInterface.CommonParam("getstudentcenter")
            val rcbApicenterresponse =
                ApiClient.getClient.getcenternew("Bearer " + Constant.accesstoken, requestcenter)
            Studentcenterlistapi.postValue(rcbApicenterresponse)
            Log.d("allcenter", rcbApicenterresponse.centers.toString())
            mRepository?.insertcenterlist(rcbApicenterresponse.centers)
            //  getallbranchlistfromApi()
        }
        usersLoadError.value = ""
        loading.value = false
    }

    fun getallbranchlist(): List<Branch>? {

        return mRepository.getAllbranch()
    }

    fun getallbranchlistfromApi() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestbranch = ApiInterface.CommonParam("studentbranch")
            val rcbApibranchresponse =
                ApiClient.getClient.getbranchnew("Bearer " + Constant.accesstoken, requestbranch)
            branchlistapi.postValue(rcbApibranchresponse)
            Log.d("allcenter", rcbApibranchresponse.Branch.toString())
            mRepository?.insertbranchlist(rcbApibranchresponse.Branch)
            //   getallayListfromApi()
        }
        usersLoadError.value = ""
        loading.value = false

    }

    fun getallaylist(): List<Ay>? {

        return mRepository.getAllay()
    }

    @SuppressLint("SuspiciousIndentation")
    fun getallayListfromApi() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestay = ApiInterface.CommonParam("getacademicyear")
            val rcbApiayresponse =
                ApiClient.getClient.getaynew("Bearer " + Constant.accesstoken, requestay)
            aylistapi.postValue(rcbApiayresponse)
            Log.d("allay", rcbApiayresponse.AcademicYear.toString())
            //  mRepository?.insertaylist(rcbApiayresponse.AcademicYear)
        }
        usersLoadError.value = ""
        loading.value = false

    }

    fun getallspecializationfromApi() {
        loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val requestsp = ApiInterface.CommonParam("getspecialization")
            val requestspecialization =
                ApiClient.getClient.getsp("Bearer " + Constant.accesstoken, requestsp)
            splistapi.postValue(requestspecialization)
        }
    }

    fun getallspecializationlist(): List<Specialization>? {

        return mRepository.getAllSpecialization()
    }


    private fun onError(message: String) {
        Log.d("Error", message.toString())
    }

}