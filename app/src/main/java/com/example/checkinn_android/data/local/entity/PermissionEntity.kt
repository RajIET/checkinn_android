package com.example.checkinn_android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "permissions")
data class PermissionEntity(
    @PrimaryKey
    val functionId: Int,
    val functionName: String,
    val functionModule: String,
    val functionCategory: String
)
