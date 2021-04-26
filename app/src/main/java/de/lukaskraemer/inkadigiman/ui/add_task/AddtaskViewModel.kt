package de.lukaskraemer.inkadigiman.ui.add_task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AddtaskViewModel(application: Application) : AndroidViewModel(application) {
    private val _text = MutableLiveData<String>().apply {
        value = "This show the tasks"
    }
    val text: LiveData<String> = _text

}