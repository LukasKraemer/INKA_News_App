package de.lukaskraemer.inkadigiman.ui.calender

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This show the Calendar"
    }
    val text: LiveData<String> = _text


}