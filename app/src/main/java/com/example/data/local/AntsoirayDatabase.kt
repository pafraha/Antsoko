package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CallLogEntity
import com.example.data.model.ContactEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MessageEntity
import com.example.data.model.TeamMemberEntity
import com.example.data.model.UserProfileEntity

@Database(
    entities = [
        ContactEntity::class,
        CallLogEntity::class,
        MessageEntity::class,
        ConversationEntity::class,
        UserProfileEntity::class,
        TeamMemberEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AntsoirayDatabase : RoomDatabase() {

    abstract fun antsoirayDao(): AntsoirayDao

    companion object {
        @Volatile
        private var INSTANCE: AntsoirayDatabase? = null

        fun getDatabase(context: Context): AntsoirayDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AntsoirayDatabase::class.java,
                    "antsoiray_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
