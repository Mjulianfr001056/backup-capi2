package org.odk.collect.android.mainmenu

import android.os.Build
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import org.odk.collect.android.R
import org.odk.collect.android.activities.ActivityUtils
import org.odk.collect.android.activities.CrashHandlerActivity
import org.odk.collect.android.configure.qr.AppConfigurationGenerator
import org.odk.collect.android.injection.DaggerUtils
import org.odk.collect.android.projects.ProjectCreator
import org.odk.collect.android.projects.ProjectSettingsDialog
import org.odk.collect.android.utilities.ThemeUtils
import org.odk.collect.androidshared.ui.FragmentFactoryBuilder
import org.odk.collect.crashhandler.CrashHandler
import org.odk.collect.permissions.PermissionsProvider
import org.odk.collect.pkl.CapiFirstActivity
import org.odk.collect.settings.SettingsProvider
import org.odk.collect.strings.localization.LocalizedActivity
import timber.log.Timber
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

    val TAG = "CAPI_MainMenu"

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

        setContentView(R.layout.main_menu_activity)
        Timber.tag(TAG).d("Created Main Menu View")

//        TODO("Hapus kalau ga dipakai lagi")
//        if (!currentProjectViewModel.hasCurrentProject()) {
//            super.onCreate(null)
//            return
//        } else {
//            this.supportFragmentManager.fragmentFactory = FragmentFactoryBuilder()
//                .forClass(PermissionsDialogFragment::class) {
//                    PermissionsDialogFragment(
//                        permissionsProvider,
//                        viewModelProvider[RequestPermissionsViewModel::class.java]
//                    )
//                }
//                .forClass(ProjectSettingsDialog::class) {
//                    ProjectSettingsDialog(viewModelFactory)
//                }
//                .forClass(MainMenuFragment::class) {
//                    MainMenuFragment(viewModelFactory, settingsProvider)
//                }
//                .build()
//
//            super.onCreate(savedInstanceState)
//
//            setContentView(R.layout.main_menu_activity)
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityUtils.startActivityAndCloseAllOthers(this, CapiFirstActivity::class.java)
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
