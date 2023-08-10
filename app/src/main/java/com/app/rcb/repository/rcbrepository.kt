package com.app.rcb.repository

import android.app.Application
import com.app.rcb.db.dao.*
import com.app.rcb.db.database.AppDatabase
import com.app.rcb.db.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class rcbrepository(private val application: Application
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var mStateDao: Statedao
    var mDropdowndao : Dropdowndao
    var mProgramdao : Programdao
    var mSeasondao : Seasondao
    var mCenterdao : Centerdao
    var mBranchdao : Branchdao
    var maydao: aydao

    var spdao: Specializationdao

    init {
        mStateDao = AppDatabase.getInstance(application).stateDao()
       mDropdowndao = AppDatabase.getInstance(application).dropdownDao()
        mProgramdao = AppDatabase.getInstance(application).Programdao()
        mSeasondao = AppDatabase.getInstance(application).seasondao()
        mCenterdao = AppDatabase.getInstance(application).centerdao()
        mBranchdao = AppDatabase.getInstance(application).branchdao()
        maydao = AppDatabase.getInstance(application).aydao()
        spdao = AppDatabase.getInstance(application).specializationdao()
    }
    fun getAllState() = mStateDao.getAllState()


    fun getAlldropdown() = mDropdowndao.getAlldropdown()

    fun getAllprogram() = mProgramdao.getAllprogram()

    fun getAllseason() = mSeasondao.getAllSeason()

    fun getAllcenter() = mCenterdao.getAllCenter()

    fun getAllbranch() = mBranchdao.getAllBranch()

    fun getAllay() = maydao.getAllay()

    fun getAllSpecialization() = spdao.getAllSpecialization()


    fun insertState(objstate: State) {
        launch { insertStateSuspend(objstate) }
    }

    fun insertstateList(stateList: List<State>) {
        launch { insertstateListSuspend(stateList)

        }
    }

    fun insertdropdownlist(dropdownlist: List<Dropdown>) {
        launch {
            insertdropdownListSuspend(dropdownlist)
        }
    }

    fun insertprogramlist(programlist:List<Program>) {
        launch {
            insertprogramListSuspend(programlist)
        }
    }

    fun insertseasonlist(seasonlist:List<Season>) {
        launch {
            insertseasonListSuspend(seasonlist)
        }
    }

    fun insertcenterlist(centerlist:List<Center>) {
        launch {
            insertcenterlistSuspend(centerlist)
        }
    }


    fun insertbranchlist(branchlist:List<Branch>) {
        launch {
            insertbranchlistSuspend(branchlist)
        }
    }

    fun insertaylist(aylist:List<Ay>) {
        launch {
            insertaySuspend(aylist)
        }
    }

    fun insertspecialization(specializationlist: List<Specialization>) {
        launch {
            insertspecializationSuspend(specializationlist)
        }
    }

    private suspend fun insertStateSuspend(objstate: State) {
        withContext(Dispatchers.IO) {
            mStateDao.insert(objstate)
        }
    }

    private suspend fun insertstateListSuspend(stateList: List<State>) {
        withContext(Dispatchers.IO) {
           // mStateDao.insertList(stateList)
            mStateDao.insertAll(stateList)
        }
    }


    private suspend fun insertdropdownListSuspend(dropdownlist: List<Dropdown>) {
        withContext(Dispatchers.IO) {
            mDropdowndao.insertAll(dropdownlist)
        }
    }

    private suspend fun insertprogramListSuspend(programlist:List<Program>)
    {
        withContext(Dispatchers.IO) {
            mProgramdao.insertAll(programlist)
        }
    }

    private suspend fun insertseasonListSuspend(seasonlist:List<Season>) {

        withContext(Dispatchers.IO) {
            mSeasondao.insertAll(seasonlist)
        }
    }

    private suspend fun insertcenterlistSuspend(centerlist:List<Center>) {

        withContext(Dispatchers.IO) {
            mCenterdao.insertAll(centerlist)
        }
    }

    private suspend fun insertbranchlistSuspend(branchlist:List<Branch>) {

        withContext(Dispatchers.IO) {
            mBranchdao.insertAll(branchlist)
        }
    }


    private suspend fun insertaySuspend(aylist:List<Ay>) {
        withContext(Dispatchers.IO) {
            maydao.insertAll(aylist)
        }
    }

    private suspend fun insertspecializationSuspend(speclizationlist: List<Specialization>) {
        withContext(Dispatchers.IO) {
            spdao.insertAll(speclizationlist)
        }
    }

}