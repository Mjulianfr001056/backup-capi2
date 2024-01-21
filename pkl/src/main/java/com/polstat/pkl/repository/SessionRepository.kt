package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Session

interface SessionRepository {

    fun saveSession(session: Session)
    fun getActiveSession(): Session?
    fun clearSession()
    fun logIn()
    fun logOut()
    fun isLoggedIn() : Boolean


}