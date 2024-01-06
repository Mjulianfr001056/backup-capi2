package com.polstat.pkl.auth.repository

interface SessionRepository {

    fun saveSession(session: Session)
    fun getActiveSession(): Session?
    fun clearSession()

}