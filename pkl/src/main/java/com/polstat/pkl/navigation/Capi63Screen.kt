package com.polstat.pkl.navigation

sealed class Capi63Screen (
    val route: String,
    val parent: String,
) {
    object OnBoarding : Capi63Screen(
        route = "onboarding",
        parent = "root"
    )
    object Login : Capi63Screen(
        route = "login",
        parent = "root"
    )
    object Beranda : Capi63Screen(
        route = "beranda",
        parent = "bottom_bar"
    )
    object Sampling : Capi63Screen(
        route = "sampling",
        parent = "bottom_bar"
    )
    object Kuesioner : Capi63Screen(
        route = "kuesioner",
        parent = "bottom_bar"
    )
    object PasswordMaster : Capi63Screen(
        route = "password",
        parent = "password"
    )
    object ListBs : Capi63Screen(
        route = "list_bs",
        parent = "listing"
    )
    object ListRuta : Capi63Screen(
        route = "list_ruta",
        parent = "listing"
    )
    object ListSample : Capi63Screen(
        route = "list_sample",
        parent = "listing"
    )
    object IsiRuta : Capi63Screen(
        route = "isi_ruta",
        parent = "listing"
    )
}

sealed class CapiScreen{
    object Top : CapiScreen() {
        const val AUTH = "auth"
        const val MAIN = "main"
    }

    object Auth : CapiScreen() {
        const val ONBOARDING = "onboarding"
        const val LOGIN = "login"
    }

    object Main : CapiScreen() {
        const val BERANDA = "beranda"
        const val SAMPLING = "sampling"
        const val KUESIONER = "kuesioner"
    }

    object Sampling : CapiScreen(){
        const val START = "start"
        const val LISTING = "listing_screen"
        const val PASSWORD = "password_screen"
    }

    object Listing : CapiScreen() {
        const val LIST_BS = "list_bs"
        const val LIST_RUTA = "list_ruta"
        const val LIST_SAMPLE = "list_sample"
        const val ISI_RUTA = "isi_ruta"
        const val EDIT_RUTA = "edit_ruta"
    }

    object Password : CapiScreen() {
        const val PASSWORD_MASTER = "password"
    }

    object Beranda : CapiScreen() {
        const val START = "start"
    }

    object Kuesioner : CapiScreen() {
        const val START = "start"
    }
}