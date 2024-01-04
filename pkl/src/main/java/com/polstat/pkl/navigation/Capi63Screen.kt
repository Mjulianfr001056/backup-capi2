package com.polstat.pkl.navigation

sealed class Capi63Screen (val route: String) {
    object Login : Capi63Screen(route = "login")
    object Beranda : Capi63Screen(route = "beranda")
    object Sampling : Capi63Screen(route = "sampling")
    object ListBs : Capi63Screen(route = "list_bs")
    object ListRuta : Capi63Screen(route = "list_ruta")
    object IsiRuta : Capi63Screen(route = "isi_ruta")
}