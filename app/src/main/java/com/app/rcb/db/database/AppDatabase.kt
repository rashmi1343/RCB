package com.app.rcb.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.*
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.rcb.db.dao.*
import com.app.rcb.db.entity.*


@Database(entities = arrayOf(State::class, Dropdown::class, Program::class, Season::class, Center::class,Branch::class,Ay::class, Specialization::class), version = 3,exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stateDao(): Statedao

    abstract fun dropdownDao() : Dropdowndao

    abstract fun Programdao() : Programdao

    abstract fun seasondao() : Seasondao

    abstract fun centerdao() : Centerdao

    abstract fun branchdao() : Branchdao

    abstract fun aydao() : aydao

    abstract fun specializationdao() : Specializationdao

    companion object {

        private var instance: AppDatabase? = null

        fun getInstance(ctx: Context): AppDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, AppDatabase::class.java,
                    "rcb_db"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
           //     populateDatabase(instance!!)
            }

        }

        private fun populateDatabase(db: AppDatabase) {
            val noteDao = db.stateDao()
          /*  subscribeOnBackground {
                noteDao.insert(State("1", "desc 1", 1))
                noteDao.insert(State("2", "desc 2", 2))
                noteDao.insert(State("3", "desc 3", 3))

            }*/
        }
    }
}