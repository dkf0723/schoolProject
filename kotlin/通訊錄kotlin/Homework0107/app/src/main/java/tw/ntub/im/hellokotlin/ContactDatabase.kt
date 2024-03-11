package tw.ntub.im.hellokotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class ContactDatabase  : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,ContactDatabase::class.java, "contact_database.db")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}