package com.polstat.pkl.task

import android.os.Build
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.N)
class LoginTask (
    loginListener : LoginTask.LoginListener
) {

    companion object {
        val LOGIN_SUCCESS = "SUCCESS"
        val LOGIN_FAIL_URL = "FAIL_URL"
        val LOGIN_FAIL_USER = "FAIL_USER"
        val LOGIN_FAIL_EXTRA = "FAIL_EXTRA"
    }

    sealed interface LoginListener {
        fun onLoginFinished(result: String)
    }


}