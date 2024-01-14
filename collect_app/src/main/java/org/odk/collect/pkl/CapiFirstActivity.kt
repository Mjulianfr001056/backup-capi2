package org.odk.collect.pkl

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CapiFirstActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent = Intent(this, ProjectConfigurerActivity::class.java)
        startActivity(intent)
//        setContent {
//            Capi63Theme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//
//                    AppNavHost(
//                        navController = navController,
//                    )
//                }
//            }
//        }
    }
}