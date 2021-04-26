package de.lukaskraemer.inkadigiman.ui.allHiddenTask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AllHiddenTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val _text = MutableLiveData<String>().apply {
        value = "This show the hidden tasks"
    }
    val text: LiveData<String> = _text


}