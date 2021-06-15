package com.example.androidwithkotlin.viewmodel

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidwithkotlin.db.room.entity.ContactEntity
import com.example.androidwithkotlin.extension.getApplicationContext
import com.example.androidwithkotlin.intent.WeatherConstants

class ContentProviderViewModel : ViewModel() {

    private val _contacts = MutableLiveData<List<ContactEntity>>()
    val contacts: LiveData<List<ContactEntity>> get() = _contacts

    private val contactsProjection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME
    )
    private var selectionClause = ""
    private var selectionArgs = arrayOf<String>()
    private var sortOrder = "${ContactsContract.Contacts.DISPLAY_NAME} ASC"

    fun loadAllContacts() {
        val cursor = getApplicationContext()
            .contentResolver
            .query(
                ContactsContract.Contacts.CONTENT_URI,
                contactsProjection,
                selectionClause,
                selectionArgs,
                sortOrder
            )
        when (cursor?.count) {
            null, 0 -> _contacts.value = listOf(ContactEntity())
            else -> {
                val contactList = mutableListOf<ContactEntity>()
                cursor.apply {
                    while (moveToNext()) {
                        val displayName =
                            getString(getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        contactList += ContactEntity(
                            displayName = displayName
                        )
                    }

                    _contacts.value = contactList.toMutableList()
                }
            }
        }

        cursor?.close()
    }
}

class ContentProviderViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentProviderViewModel::class.java)) {
            return ContentProviderViewModel() as T
        } else {
            throw WeatherConstants.Exceptions.UNKNOWN_VIEW_MODEL
        }
    }

}