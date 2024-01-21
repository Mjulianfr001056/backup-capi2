package com.polstat.pkl.repository

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.polstat.pkl.model.domain.Session
import java.time.Instant
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SessionRepository {

    companion object {
        private const val KEY_LAST_ACTIVE_TIME = "last_active_time"
        private const val SESSION_TIMEOUT_MINUTES = 180
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun isLoggedIn(): Boolean {
        val lastActiveTime = sharedPreferences.getLong(KEY_LAST_ACTIVE_TIME, 0)
        val currentTime = Instant.now().toEpochMilli()
        val elapsedMinutes = (currentTime - lastActiveTime) / (1000 * 60)

        return elapsedMinutes < SESSION_TIMEOUT_MINUTES
    }

    private val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun logIn() {
        sharedPreferences.edit().putLong(KEY_LAST_ACTIVE_TIME, Instant.now().toEpochMilli()).apply()
    }

    override fun logOut() {
        sharedPreferences.edit().remove(KEY_LAST_ACTIVE_TIME).apply()
    }

    override fun saveSession(session: Session) {
        sharedPreferences.edit()
            .putString("SESSION", gson.toJson(session))
            .apply()
    }

    override fun getActiveSession(): Session? {
        val json = sharedPreferences.getString("SESSION", null) ?: return null
        return gson.fromJson(json, Session::class.java)
    }

    override fun clearSession() {
        sharedPreferences.edit()
            .remove("SESSION")
            .apply()
    }

}