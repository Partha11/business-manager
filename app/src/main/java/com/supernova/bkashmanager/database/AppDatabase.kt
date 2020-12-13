package com.supernova.bkashmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.util.Constants

@Database(entities = [ User::class, History::class ], version = Constants.DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `${Constants.TABLE_HISTORY}` ADD COLUMN `${Constants.TABLE_HISTORY_TRX_MESSAGE}` VARCHAR(100)")
            }
        }

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = instance

            if (tempInstance != null) {

                return tempInstance
            }

            synchronized(this) {

                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, Constants.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build()
                this.instance = instance

                return instance
            }
        }
    }

}