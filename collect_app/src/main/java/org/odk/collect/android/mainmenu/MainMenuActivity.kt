package org.odk.collect.android.mainmenu

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.polstat.pkl.CapiFirstActivity
import org.odk.collect.android.R
import org.odk.collect.android.activities.ActivityUtils
import org.odk.collect.android.activities.CrashHandlerActivity
import org.odk.collect.android.activities.FirstLaunchActivity
import org.odk.collect.android.configure.qr.AppConfigurationGenerator
import org.odk.collect.android.injection.DaggerUtils
import org.odk.collect.android.pkl.CobaLoginJuga
import org.odk.collect.android.projects.ProjectCreator
import org.odk.collect.android.projects.ProjectSettingsDialog
import org.odk.collect.android.utilities.ThemeUtils
import org.odk.collect.androidshared.ui.FragmentFactoryBuilder
import org.odk.collect.crashhandler.CrashHandler
import org.odk.collect.permissions.PermissionsProvider
import org.odk.collect.settings.SettingsProvider
import org.odk.collect.strings.localization.LocalizedActivity
import javax.inject.Inject

class MainMenuActivity : LocalizedActivity() {

    @Inject
    lateinit var viewModelFactory: MainMenuViewModelFactory

    @Inject
    lateinit var settingsProvider: SettingsProvider

    @Inject
    lateinit var permissionsProvider: PermissionsProvider

    @Inject
    lateinit var projectCreator: ProjectCreator

    @Inject
    lateinit var appConfigurationGenerator: AppConfigurationGenerator

    private lateinit var currentProjectViewModel: CurrentProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        //Membuat Splash Screen
        initSplashScreen()

        /*
        Kalau aplikasinya sempat crash, kita akan menampilkan CrashHandlerActivity
        dan menghapus semua activity yang ada di stack.
         */
        CrashHandler.getInstance(this)?.also {
            if (it.hasCrashed(this)) {
                super.onCreate(null)
                ActivityUtils.startActivityAndCloseAllOthers(this, CrashHandlerActivity::class.java)
                return
            }
        }

        DaggerUtils.getComponent(this).inject(this)

        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        currentProjectViewModel = viewModelProvider[CurrentProjectViewModel::class.java]

        ThemeUtils(this).setDarkModeForCurrentProject()

        ActivityUtils.startActivityAndCloseAllOthers(this, CapiFirstActivity::class.java)

        if (!currentProjectViewModel.hasCurrentProject()) {
            super.onCreate(null)

            // ini kodingan buat konek ke central
//            val settingsJson = appConfigurationGenerator.getAppConfigurationAsJsonWithServerDetails(
//                "https://0793-103-171-161-174.ngrok-free.app/v1/key/sJa3lXXUe4IskXpX68BGzUQtCMv10MRXBoZ1UEmnntiRSbJ2XjUU4bbmuCrvc7RZ/projects/2",
//                "",
//                ""
//            )
//            projectCreator.createNewProject(settingsJson)
//            ActivityUtils.startActivityAndCloseAllOthers(this, MainMenuActivity::class.java)

            return
        } else {
            this.supportFragmentManager.fragmentFactory = FragmentFactoryBuilder()
                .forClass(PermissionsDialogFragment::class) {
                    PermissionsDialogFragment(
                        permissionsProvider,
                        viewModelProvider[RequestPermissionsViewModel::class.java]
                    )
                }
                .forClass(ProjectSettingsDialog::class) {
                    ProjectSettingsDialog(viewModelFactory)
                }
                .forClass(MainMenuFragment::class) {
                    MainMenuFragment(viewModelFactory, settingsProvider)
                }
                .build()

            super.onCreate(savedInstanceState)

//            setContent {
//                Surface (modifier = Modifier.fillMaxSize()){
//                    Text(text = "Coba Login Juga di Main Menu")
//                }
//            }
        }
    }

    private fun initSplashScreen() {
        /*
        We don't need the `installSplashScreen` call on Android 12+ (the system handles the
        splash screen for us) and it causes problems if we later switch between dark/light themes
        with the ThemeUtils#setDarkModeForCurrentProject call.
        */
        if (Build.VERSION.SDK_INT < 31) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_Collect)
        }
    }
}
