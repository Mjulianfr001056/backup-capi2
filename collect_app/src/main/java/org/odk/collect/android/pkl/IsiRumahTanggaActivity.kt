package org.odk.collect.android.pkl

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import org.odk.collect.android.pkl.ui.IsiRumahTanggaScreen

class IsiRumahTanggaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IsiRumahTanggaScreen()
        }
    }
}