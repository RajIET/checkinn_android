package com.example.checkinn_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.checkinn_android.data.local.entity.PermissionEntity
import com.example.checkinn_android.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun clearUserSession()

    @Query("UPDATE users SET accessToken = :accessToken, refreshToken = :refreshToken WHERE isLoggedIn = 1")
    suspend fun updateTokens(accessToken: String?, refreshToken: String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPermissions(permissions: List<PermissionEntity>)

    @Query("SELECT * FROM permissions")
    fun getPermissions(): Flow<List<PermissionEntity>>

    @Query("DELETE FROM permissions")
    suspend fun deleteAllPermissions()

    @Transaction
    suspend fun saveUserAndPermissions(user: UserEntity, permissions: List<PermissionEntity>) {
        deleteAllUsers()
        deleteAllPermissions()
        insertUser(user)
        if (permissions.isNotEmpty()) {
            insertPermissions(permissions)
        }
    }

    @Transaction
    suspend fun logout() {
        deleteAllUsers()
        deleteAllPermissions()
    }
}
