package com.polstat.pkl.navigation

sealed class Capi63Screen (val route: String) {
    object OnBoarding : Capi63Screen(route = "onboarding")
    object Login : Capi63Screen(route = "login")
    object Beranda : Capi63Screen(route = "beranda")
    object Sampling : Capi63Screen(route = "sampling")
    object PasswordMaster : Capi63Screen(route = "password")
    object ListBs : Capi63Screen(route = "list_bs")
    object ListRuta : Capi63Screen(route = "list_ruta")
    object ListSample : Capi63Screen(route = "list_sample")
    object IsiRuta : Capi63Screen(route = "isi_ruta")
}