package com.polstat.pkl.auth.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : SessionRepository {

    private val gson = Gson()

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