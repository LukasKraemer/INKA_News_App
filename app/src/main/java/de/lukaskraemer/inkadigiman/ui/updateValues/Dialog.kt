package de.lukaskraemer.inkadigiman.ui.updateValues


import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import de.lukaskraemer.inkadigiman.CommonViewModel
import de.lukaskraemer.inkadigiman.MainViewModelFactory
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.database.Task
import java.util.*


class Dialog(var task: Task? = null) : DialogFragment() {
    private lateinit var rootView: View

    // Buttons:
    private lateinit var btnSave: Button
    private lateinit var btnAbort: Button

    // TextInputLayout
    private lateinit var spinner1: Spinner
    private lateinit var txtTime: TextView
    private lateinit var txtDate: TextView
    private lateinit var editNote: EditText
    private lateinit var timePicker: TimePickerDialog
    private lateinit var datePicker: DatePickerDialog
    private lateinit var dialogHead: TextView

    val c = Calendar.getInstance()

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var date: Long = 0

    // ViewModel:
    private lateinit var commonViewModel: CommonViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.dialog_change, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commonViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory(requireActivity().application)
        ).get(CommonViewModel::class.java)
        initElements()
        initSpinner()
        initEditTexts()

    }

    private fun initElements() {
        btnSave = rootView.findViewById(R.id.dialog_Btn_Save)
        btnAbort = rootView.findViewById(R.id.dialog_Btn_abort)
        spinner1 = rootView.findViewById(R.id.dialog_spinner1)
        editNote = rootView.findViewById(R.id.dialog_edit_note)


        spinner1.isSoundEffectsEnabled = true
        editNote.setText(task?.notice)
        btnSave.setOnClickListener { saveData() }
        btnAbort.setOnClickListener { dismiss() }

    }

    private fun initEditTexts() {
        dialogHead= rootView.findViewById(R.id.dialog_head)
        if (task != null) {
            c.timeInMillis=task!!.time
            dialogHead.text = getString(R.string.changeAppointment)
        }else{
            dialogHead.text = getString(R.string.createAppointment)
        }
        txtDate = rootView.findViewById(R.id.tv_date)
        txtTime = rootView.findViewById(R.id.tv_time)

        txtDate.text =
            c[Calendar.DAY_OF_MONTH].toString() + "." + (c[Calendar.MONTH] + 1) + "." + c[Calendar.YEAR]
        txtTime.text = c[Calendar.HOUR_OF_DAY].toString() + ":" + c[Calendar.MINUTE]



        txtDate.setOnClickListener {
            datePicker.show()
        }

        txtTime.setOnClickListener {

            timePicker.show()


        }

    }

    private fun initSpinner() {
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        datePicker = DatePickerDialog(
            rootView.context,
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                txtDate.text = dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        )


        mMinute = c[Calendar.MINUTE]
        mHour = c[Calendar.HOUR_OF_DAY]
        // Launch Time Picker Dialog
        timePicker = TimePickerDialog(
            rootView.context,
            OnTimeSetListener { _, hourOfDay, minute -> txtTime.text = "$hourOfDay:$minute" },
            mHour,
            mMinute,
            DateFormat.is24HourFormat(requireContext())
        )

    }

    private fun saveData() {

        if (task != null) {

            task?.type =
                rootView.resources.getStringArray(R.array.kindoftask).indexOf(spinner1.selectedItem)
            task?.time= c.timeInMillis
            task?.notice = editNote.text.toString()
            commonViewModel.update(task!!)
            Toast.makeText(
                requireContext(),
                getString(R.string.alltaskinDBupdated),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val time: List<String> = txtTime.text.split(":")
            val calender = Calendar.getInstance()
            calender.set(Calendar.YEAR, datePicker.datePicker.year)
            calender.set(Calendar.MONTH, datePicker.datePicker.month)
            calender.set(Calendar.DAY_OF_MONTH, datePicker.datePicker.dayOfMonth)
            calender.set(Calendar.HOUR_OF_DAY, time[0].toInt())
            calender.set(Calendar.MINUTE, time[1].toInt())

            commonViewModel.insert(
                rootView.resources.getStringArray(R.array.kindoftask)
                    .indexOf(spinner1.selectedItem),
                calender,
                editNote.text.toString()
            )
            Toast.makeText(requireContext(), getString(R.string.alltaskinDB), Toast.LENGTH_SHORT)
                .show()
        }
        dismiss()
    }
}



