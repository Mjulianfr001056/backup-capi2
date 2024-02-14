package org.odk.collect.pkl.navigation

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
        const val BERANDA = "Beranda"
        const val SAMPLING = "Sampling"
        const val KUESIONER = "Kuesioner"
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
        const val SALIN_RUTA = "salin_ruta"
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