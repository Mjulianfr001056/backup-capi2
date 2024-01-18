package com.polstat.pkl.viewmodel

import androidx.lifecycle.ViewModel

class PermissionViewModel: ViewModel() {

    val visiblePermissionDialogQueue = mutableListOf<String>()

    fun dismissDialog(){
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
    ){
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)){
            visiblePermissionDialogQueue.add(permission)
        }
    }
}