package com.example.androidwithkotlin.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.example.androidwithkotlin.databinding.FragmentContentProviderBinding
import com.example.androidwithkotlin.viewmodel.ContentProviderViewModel
import com.example.androidwithkotlin.viewmodel.ContentProviderViewModelFactory

class ContentProviderFragment : BaseFragment() {

    // TODO: 21.06.2021 Перенести в BaseFragment
    private var _binding: FragmentContentProviderBinding? = null
    private val binding
        get() = _binding!!

    private val contentProviderViewModel by viewModels<ContentProviderViewModel> {
        ContentProviderViewModelFactory()
    }

    private val requestPermissions = arrayOf(Manifest.permission.READ_CONTACTS)
    private val _permissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.READ_CONTACTS -> {
                        if (it.value) {
                            contentProviderViewModel.loadAllContacts()
                        } else {
                            showAlertDialog(
                                title = "Доступ к контактам",
                                message = "Для корректной работы приложения необходим доступ к контактам",
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

                it.forEach {
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
                    showAlertDialog(
                        title = "Доступ к контактам",
                        message = "Для корректной работы приложения необходим доступ к контактам"
                    ) { _, _ ->
                        requestPermissions()
                    }
                }
                else -> {
                    requestPermissions()
                }
            }
        }
    }

    private fun requestPermissions() {
        _permissionRequest.launch(requestPermissions)
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