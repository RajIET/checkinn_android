package com.example.checkinn_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.checkinn_android.data.local.dao.UserDao
import com.example.checkinn_android.data.local.entity.PermissionEntity
import com.example.checkinn_android.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PermissionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
