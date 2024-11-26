package com.example.roomdbapp.db_utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdbapp.data.models.Comment
import com.example.roomdbapp.data.models.User

@Database(entities = [Comment::class, User::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {

    abstract fun userDao(): CommentDao

    companion object {
        private var instance: CommentDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): CommentDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, CommentDatabase::class.java,
                    "user_database")
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback)
                    .build()

            return instance!!

        }

        /*private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }*/

        /*private fun populateDatabase(db: UserDatabase) {
            val noteDao = db.noteDao()
            subscribeOnBackground {
                noteDao.insert(Note("title 1", "desc 1", 1))
                noteDao.insert(Note("title 2", "desc 2", 2))
                noteDao.insert(Note("title 3", "desc 3", 3))

            }
        }*/
    }



}
