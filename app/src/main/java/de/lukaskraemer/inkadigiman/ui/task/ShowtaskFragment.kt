package de.lukaskraemer.inkadigiman.ui.task

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
import de.lukaskraemer.inkadigiman.CommonViewModel
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.TaskListAdapter
import de.lukaskraemer.inkadigiman.data.database.Task
import de.lukaskraemer.inkadigiman.ui.updateValues.Dialog

class ShowtaskFragment : Fragment() {
    private lateinit var rv: RecyclerView
    private lateinit var adapter: TaskListAdapter

    private lateinit var commonViewModel: CommonViewModel
    private lateinit var showtaskViewmodel: ShowtaskViewModel
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showtaskViewmodel =
            ViewModelProvider(this).get(ShowtaskViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_showtask, container, false)
        initRecyclerView()
        commonViewModel =
            ViewModelProvider(this).get(CommonViewModel::class.java)
        commonViewModel.getLiveTaskList().observe(viewLifecycleOwner, Observer { items ->
            adapter.updateContent(ArrayList(items))
        })
        return root
    }


    private fun initRecyclerView() {
        rv = root.findViewById(R.id.showtask_rv)
        adapter = TaskListAdapter(ArrayList())
        rv.adapter = adapter
        // Implement the Interfaces:
        adapter.setOnItemClickListener(object : TaskListAdapter.OnItemClickListener {
            override fun setOnItemClickListener(pos: Int) {
                val dialog = Dialog(adapter.content[pos])
                dialog.show(parentFragmentManager, "update Task")
            }
        })

        adapter.setOnItemLongClickListener(object : TaskListAdapter.OnItemLongClickListener {
            override fun setOnItemLongClickListener(pos: Int) {
                startAlarmDialog(adapter.content[pos])
            }
        })
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

}