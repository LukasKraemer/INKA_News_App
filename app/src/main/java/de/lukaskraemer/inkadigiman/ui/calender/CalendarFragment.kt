package de.lukaskraemer.inkadigiman.ui.calender


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import de.lukaskraemer.inkadigiman.CommonViewModel
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.TaskListAdapter
import de.lukaskraemer.inkadigiman.data.database.Task
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    private lateinit var calendarViewmodel: CalendarViewModel
    private lateinit var root: View
    private lateinit var rv: RecyclerView
    private lateinit var adapter: TaskListAdapter
    private lateinit var calendarView: CalendarView
    private lateinit var commonViewModel: CommonViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        calendarViewmodel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_calendar, container, false)
        commonViewModel = ViewModelProvider(this).get(CommonViewModel::class.java)
        calender()
        initRecyclerView()

        initEvents()

        calendarView.setOnDayClickListener { eventDay ->
            commonViewModel.getLiveTaskListbyDay(eventDay.calendar)
                    .observe(viewLifecycleOwner, Observer { items ->
                        adapter.updateContent(ArrayList(items))
                    })
        }

        adapter.setOnItemClickListener(object : TaskListAdapter.OnItemClickListener {
            override fun setOnItemClickListener(pos: Int) {
                var ausgabetext: String = if (adapter.content[pos].notice != "") {
                    adapter.content[pos].notice.toString()
                } else {
                    getString(R.string.nonotice)
                }
                val info = Toast.makeText(context, ausgabetext, Toast.LENGTH_LONG)
                info.show()
            }
        })

        adapter.setOnItemLongClickListener(object : TaskListAdapter.OnItemLongClickListener {
            override fun setOnItemLongClickListener(pos: Int) {
                startAlarmDialog(adapter.content[pos])
            }
        })

        return root
    }


    private fun calender() {
        val events: MutableList<EventDay> = ArrayList()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2021)
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        events.add(EventDay(calendar, R.drawable.ic_baseline_schedule_24))
        calendarView = root.findViewById(R.id.calendarView)
        calendarView.setEvents(events)
        calendarView.showCurrentMonthPage()
    }

    private fun initRecyclerView() {
        rv = root.findViewById(R.id.main_rv)
        adapter = TaskListAdapter(ArrayList())
        rv.adapter = adapter
    }

    private fun startAlarmDialog(task: Task) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(getString(R.string.deleted_message_temp))
            setPositiveButton("Ok") { _, _ ->
                Toast.makeText(
                    requireContext(),
                    "${task.type} " + getString(R.string.deleted),
                    Toast.LENGTH_SHORT
                ).show()
                task.show = false
                commonViewModel.update(task)

            }
            setNegativeButton(getString(R.string.abort)) { dialog, _ ->
                dialog.dismiss()

            }
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun initEvents() {
        commonViewModel.dates().observe(viewLifecycleOwner, { items ->
            try {
                var useddates: ArrayList<String> = ArrayList()
                val events: MutableList<EventDay> = ArrayList()

                for (i in items.indices) {

                    var datefromTask = Calendar.getInstance()
                    datefromTask.timeInMillis = items[i].time
                    var checkstring =
                            datefromTask.get(Calendar.YEAR).toString() + "." + datefromTask.get(Calendar.MONTH).toString() + "." + datefromTask.get(Calendar.DAY_OF_MONTH).toString()

                        var calendar = Calendar.getInstance()
                        calendar.timeInMillis = items[i].time

                        events.add(EventDay(datefromTask, R.drawable.ic_baseline_schedule_24))
                        useddates.add(checkstring)

                }
                calendarView.setEvents(events)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        })
    }
}
