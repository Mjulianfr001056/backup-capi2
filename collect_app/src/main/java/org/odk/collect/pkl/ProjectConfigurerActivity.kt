package org.odk.collect.pkl

import android.os.Bundle
import androidx.activity.ComponentActivity
import org.odk.collect.android.activities.ActivityUtils
import org.odk.collect.android.configure.qr.AppConfigurationGenerator
import org.odk.collect.android.injection.DaggerUtils
import org.odk.collect.android.mainmenu.MainMenuActivity
import org.odk.collect.android.projects.CurrentProjectProvider
import org.odk.collect.android.projects.ProjectCreator
import org.odk.collect.android.projects.SettingsConnectionMatcher
import org.odk.collect.androidshared.ui.ToastUtils
import org.odk.collect.projects.ProjectsRepository
import org.odk.collect.settings.SettingsProvider
import timber.log.Timber
import javax.inject.Inject

class ProjectConfigurerActivity : ComponentActivity(){

    @Inject
    lateinit var appConfigurationGenerator: AppConfigurationGenerator

    @Inject
    lateinit var projectCreator: ProjectCreator

    @Inject
    lateinit var projectsRepository: ProjectsRepository

    @Inject
    lateinit var settingsProvider: SettingsProvider

    @Inject
    lateinit var currentProjectProvider: CurrentProjectProvider

    private lateinit var settingsConnectionMatcher: SettingsConnectionMatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUtils.getComponent(this).inject(this)
        settingsConnectionMatcher = SettingsConnectionMatcher(projectsRepository, settingsProvider)

        val token = intent.getStringExtra("token")

        val settingsJson = appConfigurationGenerator.getAppConfigurationAsJsonWithServerDetails(
            "https://central.pkl63.stis.ac.id/v1/key/${token}/projects/4",
            "",
            ""
        )

        Timber.tag("TEST").d("onCreate: it also works this time!")
        settingsConnectionMatcher.getProjectWithMatchingConnection(settingsJson)?.let { uuid ->
            switchToProject(uuid)
        } ?: run {
            createProject(settingsJson)
        }

    }

    fun createProject(settingsJson: String) {
        projectCreator.createNewProject(settingsJson)
        ActivityUtils.startActivityAndCloseAllOthers(this, MainMenuActivity::class.java)
        ToastUtils.showLongToast(
            this,
            getString(org.odk.collect.strings.R.string.switched_project, currentProjectProvider.getCurrentProject().name)
        )
    }

    fun switchToProject(uuid: String) {
        currentProjectProvider.setCurrentProject(uuid)
        ActivityUtils.startActivityAndCloseAllOthers(this, MainMenuActivity::class.java)
        ToastUtils.showLongToast(
            this,
            getString(
                org.odk.collect.strings.R.string.switched_project,
                currentProjectProvider.getCurrentProject().name
            )
        )
    }
}