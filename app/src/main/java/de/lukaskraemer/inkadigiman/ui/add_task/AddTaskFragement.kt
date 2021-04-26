package de.lukaskraemer.inkadigiman.ui.add_task


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.lukaskraemer.inkadigiman.CommonViewModel
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.database.Task
import java.util.*


class AddTaskFragement(var task: Task? = null) : Fragment() {
    private lateinit var addtaskViewmodel: AddtaskViewModel
    private lateinit var rootView: View
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var spinner1: Spinner
    private lateinit var timePicker: TimePicker
    private lateinit var datePicker: DatePicker
    private lateinit var editNote: EditText
    private lateinit var btnSave: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addtaskViewmodel =
            ViewModelProvider(this).get(AddtaskViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_add_task, container, false)
        commonViewModel =
            ViewModelProvider(this).get(CommonViewModel::class.java)
        loadElements()

        initButtons()

        return rootView
    }

    private fun loadElements() {
        spinner1 = rootView.findViewById(R.id.add_task_spinner1) as Spinner
        timePicker = rootView.findViewById(R.id.add_task_edit_time) as TimePicker
        datePicker = rootView.findViewById(R.id.add_task_edit_date) as DatePicker
        editNote = rootView.findViewById(R.id.add_task_edit_note) as EditText
        btnSave = rootView.findViewById(R.id.add_task_Btn_Save) as Button

        //set 24Hour layout
        timePicker.setIs24HourView(true)
    }

    private fun initButtons() {
        btnSave = rootView.findViewById(R.id.add_task_Btn_Save)

        btnSave.setOnClickListener { saveData() }
    }

    fun createCalender(datePicker: DatePicker, timePicker: TimePicker): Calendar {
        val timer = Calendar.getInstance()
        timer.set(Calendar.YEAR, datePicker.year)
        timer.set(Calendar.MONTH, datePicker.month)
        timer.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
        timer.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        timer.set(Calendar.MINUTE, timePicker.minute)
        return timer
    }

    private fun saveData() {

        if (task != null) {
            task?.type =
                rootView.resources.getStringArray(R.array.kindoftask).indexOf(spinner1.selectedItem)

            task?.time = createCalender(datePicker, timePicker).timeInMillis
            task?.notice = editNote.text.toString()
            commonViewModel.update(task!!)
            Toast.makeText(requireContext(), "Task updated in Database", Toast.LENGTH_SHORT).show()
        } else {

            commonViewModel.insert(
                rootView.resources.getStringArray(R.array.kindoftask)
                    .indexOf(spinner1.selectedItem),
                createCalender(datePicker, timePicker),
                editNote.text.toString()
            )
            Toast.makeText(requireContext(), "Task inserted in Database", Toast.LENGTH_SHORT).show()
        }


    }

}