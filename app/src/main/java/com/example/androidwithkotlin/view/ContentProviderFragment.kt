package com.example.androidwithkotlin.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.example.androidwithkotlin.databinding.FragmentContentProviderBinding
import com.example.androidwithkotlin.viewmodel.ContentProviderViewModel
import com.example.androidwithkotlin.viewmodel.ContentProviderViewModelFactory

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding
        get() = _binding!!

    private val contentProviderViewModel by viewModels<ContentProviderViewModel> {
        ContentProviderViewModelFactory()
    }

    private val requestPermissions = arrayOf(Manifest.permission.READ_CONTACTS)
    private val permissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.READ_CONTACTS -> {
                        if (it.value) {
                            contentProviderViewModel.loadAllContacts()
                        } else {
                            showPermissionExplanationAlert(
                                showPositiveButton = false
                            )
                        }
                    }
                }
            }

        }

    private fun initViewModel() {
        contentProviderViewModel.contacts.observe(viewLifecycleOwner) {
            binding.containerForContacts.apply {
                removeAllViews()

                it.forEach{
                    addView(
                        AppCompatTextView(requireContext()).apply {
                            text = it.displayName
                            textSize = 14f
                        }
                    )
                }
            }
        }
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    contentProviderViewModel.loadAllContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    showPermissionExplanationAlert()
                }
                else -> {
                    permissionRequest.launch(requestPermissions)
                }
            }
        }
    }

    private fun showPermissionExplanationAlert(showPositiveButton: Boolean = true) {
        val alert = AlertDialog.Builder(requireContext())
        alert
            .setTitle("Доступ к контактам")
            .setMessage("Для корректной работы приложения необходим доступ к контактам")
            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }

        if (showPositiveButton) alert.setPositiveButton("Предоставить доступ") { _, _ ->
            permissionRequest.launch(requestPermissions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(
            inflater,
            container,
            false
        )
        setHasOptionsMenu(true)

        initViewModel()

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.children.forEach { it.isVisible = false }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContentProviderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}