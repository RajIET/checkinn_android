package com.example.checkinn_android.domain.usecase

import com.example.checkinn_android.data.local.dao.UserDao
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CheckPermissionUseCase @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun hasApprovePermission(): Boolean {
        return hasPermission("Approve Check-In")
    }

    suspend fun hasDenyPermission(): Boolean {
        return hasPermission("Reject Check-In")
    }

    private suspend fun hasPermission(query: String): Boolean {
        val permissions = userDao.getPermissions().firstOrNull() ?: return false

        return permissions.any { p ->
            val name = p.functionName
            if (name.contains(query, ignoreCase = true)) return@any true
            val hyphenated = query.replace(" ", "-")
            if (name.contains(hyphenated, ignoreCase = true)) return@any true
            val parts = query.split(" ")
            parts.isNotEmpty() && parts.all { name.contains(it, ignoreCase = true) }
        }
    }
}
