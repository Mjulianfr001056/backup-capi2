package org.odk.collect.android.mainmenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.odk.collect.android.activities.DeleteSavedFormActivity
import org.odk.collect.android.activities.FormDownloadListActivity
import org.odk.collect.android.activities.InstanceChooserList
import org.odk.collect.android.activities.InstanceUploaderListActivity
import org.odk.collect.android.activities.WebViewActivity
import org.odk.collect.android.application.MapboxClassInstanceCreator
import org.odk.collect.android.databinding.MainMenuBinding
import org.odk.collect.android.formlists.blankformlist.BlankFormListActivity
import org.odk.collect.android.formmanagement.FormFillingIntentFactory
import org.odk.collect.android.gdrive.GoogleDriveActivity
import org.odk.collect.android.projects.ProjectIconView
import org.odk.collect.android.projects.ProjectSettingsDialog
import org.odk.collect.android.utilities.ApplicationConstants
import org.odk.collect.android.utilities.PlayServicesChecker
import org.odk.collect.androidshared.data.getState
import org.odk.collect.androidshared.ui.DialogFragmentUtils
import org.odk.collect.androidshared.ui.SnackbarUtils
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard
import org.odk.collect.projects.Project
import org.odk.collect.settings.SettingsProvider
import org.odk.collect.settings.keys.ProjectKeys
import org.odk.collect.strings.R

class MainMenuFragment(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val settingsProvider: SettingsProvider
) : Fragment() {

    private lateinit var mainMenuViewModel: MainMenuViewModel
    private lateinit var currentProjectViewModel: CurrentProjectViewModel
    private lateinit var permissionsViewModel: RequestPermissionsViewModel

    private val formEntryFlowLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            displayFormSavedSnackbar(it.data?.data)
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        mainMenuViewModel = viewModelProvider[MainMenuViewModel::class.java]
        currentProjectViewModel = viewModelProvider[CurrentProjectViewModel::class.java]
        permissionsViewModel = viewModelProvider[RequestPermissionsViewModel::class.java]

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MainMenuBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentProjectViewModel.currentProject.observe(viewLifecycleOwner) { (_, name): Project.Saved ->
            requireActivity().invalidateOptionsMenu()
            requireActivity().title = name
        }

        val binding = MainMenuBinding.bind(view)
        initToolbar(binding)
        initMapbox()
        initButtons(binding)
        initAppName(binding)

        if (permissionsViewModel.shouldAskForPermissions()) {
            DialogFragmentUtils.showIfNotShowing(
                PermissionsDialogFragment::class.java,
                this.parentFragmentManager
            )
        }
    }

    override fun onResume() {
        super.onResume()

        currentProjectViewModel.refresh()
        mainMenuViewModel.refreshInstances()

        val binding = MainMenuBinding.bind(requireView())
        setButtonsVisibility(binding)
        setupDeprecationBanner(binding)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val projectsMenuItem = menu.findItem(org.odk.collect.android.R.id.projects)
        (projectsMenuItem.actionView as ProjectIconView).apply {
            project = currentProjectViewModel.currentProject.value
            setOnClickListener {
//                onOptionsItemSelected(projectsMenuItem)
            }
            contentDescription = getString(R.string.projects)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(org.odk.collect.android.R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!MultiClickGuard.allowClick(javaClass.name)) {
            return true
        }
        if (item.itemId == org.odk.collect.android.R.id.projects) {
            DialogFragmentUtils.showIfNotShowing(
                ProjectSettingsDialog::class.java,
                parentFragmentManager
            )
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar(binding: MainMenuBinding) {
        val toolbar = binding.root.findViewById<Toolbar>(org.odk.collect.android.R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun initMapbox() {
        if (MapboxClassInstanceCreator.isMapboxAvailable()) {
            childFragmentManager
                .beginTransaction()
                .add(
                    org.odk.collect.android.R.id.map_box_initialization_fragment,
                    MapboxClassInstanceCreator.createMapBoxInitializationFragment()!!
                )
                .commit()
        }
    }

    private fun initButtons(binding: MainMenuBinding) {
        binding.enterData.setOnClickListener {
            formEntryFlowLauncher.launch(
                Intent(requireActivity(), BlankFormListActivity::class.java)
            )
        }

        binding.reviewData.setOnClickListener {
            formEntryFlowLauncher.launch(
                Intent(requireActivity(), InstanceChooserList::class.java).apply {
                    putExtra(
                        ApplicationConstants.BundleKeys.FORM_MODE,
                        ApplicationConstants.FormModes.EDIT_SAVED
                    )
                }
            )
        }

        binding.sendData.setOnClickListener {
            formEntryFlowLauncher.launch(
                Intent(
                    requireActivity(),
                    InstanceUploaderListActivity::class.java
                )
            )
        }

        binding.viewSentForms.setOnClickListener {
            startActivity(
                Intent(requireActivity(), InstanceChooserList::class.java).apply {
                    putExtra(
                        ApplicationConstants.BundleKeys.FORM_MODE,
                        ApplicationConstants.FormModes.VIEW_SENT
                    )
                }
            )
        }

        binding.getForms.setOnClickListener(
            View.OnClickListener {
                val protocol =
                    settingsProvider.getUnprotectedSettings().getString(ProjectKeys.KEY_PROTOCOL)
                val intent =
                    if (protocol.equals(ProjectKeys.PROTOCOL_GOOGLE_SHEETS, ignoreCase = true)) {
                        if (PlayServicesChecker().isGooglePlayServicesAvailable(requireContext())) {
                            Intent(
                                requireContext().applicationContext,
                                GoogleDriveActivity::class.java
                            )
                        } else {
                            PlayServicesChecker().showGooglePlayServicesAvailabilityErrorDialog(
                                requireContext()
                            )
                            return@OnClickListener
                        }
                    } else {
                        Intent(
                            requireContext().applicationContext,
                            FormDownloadListActivity::class.java
                        )
                    }
                startActivity(intent)
            }
        )

        binding.manageForms.setOnClickListener {
            startActivity(Intent(requireContext(), DeleteSavedFormActivity::class.java))
        }

        mainMenuViewModel.sendableInstancesCount.observe(viewLifecycleOwner) { finalized: Int ->
            binding.sendData.setNumberOfForms(finalized)
        }
        mainMenuViewModel.editableInstancesCount.observe(viewLifecycleOwner) { unsent: Int ->
            binding.reviewData.setNumberOfForms(unsent)
        }
        mainMenuViewModel.sentInstancesCount.observe(viewLifecycleOwner) { sent: Int ->
            binding.viewSentForms.setNumberOfForms(sent)
        }
    }

    private fun initAppName(binding: MainMenuBinding) {
        binding.appName.text = String.format(
            "%s %s",
            getString(R.string.collect_app_name),
            mainMenuViewModel.version
        )

        val versionSHA = mainMenuViewModel.versionCommitDescription
        if (versionSHA != null) {
            binding.versionSha.text = versionSHA
        } else {
            binding.versionSha.visibility = View.GONE
        }
    }

    private fun setButtonsVisibility(binding: MainMenuBinding) {
        binding.reviewData.visibility =
            if (mainMenuViewModel.shouldEditSavedFormButtonBeVisible()) View.VISIBLE else View.GONE
        binding.sendData.visibility =
            if (mainMenuViewModel.shouldSendFinalizedFormButtonBeVisible()) View.VISIBLE else View.GONE
        binding.viewSentForms.visibility =
            if (mainMenuViewModel.shouldViewSentFormButtonBeVisible()) View.VISIBLE else View.GONE
        binding.getForms.visibility =
            if (mainMenuViewModel.shouldGetBlankFormButtonBeVisible()) View.VISIBLE else View.GONE
        binding.manageForms.visibility =
            if (mainMenuViewModel.shouldDeleteSavedFormButtonBeVisible()) View.VISIBLE else View.GONE
    }

    private fun setupDeprecationBanner(binding: MainMenuBinding) {
        val unprotectedSettings = settingsProvider.getUnprotectedSettings()
        val protocol = unprotectedSettings.getString(ProjectKeys.KEY_PROTOCOL)
        val usingGoogleDrive = ProjectKeys.PROTOCOL_GOOGLE_SHEETS == protocol
        val editedFinalizedForm =
            requireContext().getState()
                .get<Boolean>(ApplicationConstants.AppStateKeys.EDITED_FINALIZED_FORM) ?: false

        if (usingGoogleDrive) {
            binding.deprecationBanner.root.visibility = View.VISIBLE
            binding.deprecationBanner.message.setText(R.string.google_drive_deprecation_message)
            binding.deprecationBanner.learnMoreButton.setOnClickListener {
                val intent = Intent(requireActivity(), WebViewActivity::class.java)
                intent.putExtra("url", "https://forum.getodk.org/t/40097")
                startActivity(intent)
            }
        } else if (editedFinalizedForm) {
            binding.deprecationBanner.root.visibility = View.VISIBLE
            binding.deprecationBanner.message.setText(R.string.edit_finalized_form_deprecation_message)
            binding.deprecationBanner.learnMoreButton.setOnClickListener {
                val intent = Intent(requireActivity(), WebViewActivity::class.java)
                intent.putExtra("url", "https://forum.getodk.org/t/42007")
                startActivity(intent)
            }
        } else {
            binding.deprecationBanner.root.visibility = View.GONE
        }
    }

    private fun displayFormSavedSnackbar(uri: Uri?) {
        if (uri == null) {
            return
        }

        val formSavedSnackbarDetails = mainMenuViewModel.getFormSavedSnackbarDetails(uri)

        formSavedSnackbarDetails?.let {
            SnackbarUtils.showLongSnackbar(
                requireView(),
                getString(it.first),
                action = it.second?.let { action ->
                    SnackbarUtils.Action(getString(action)) {
                        formEntryFlowLauncher.launch(
                            FormFillingIntentFactory.editInstanceIntent(
                                requireContext(),
                                uri
                            )
                        )
                    }
                },
                displayDismissButton = true
            )
        }
    }
}
