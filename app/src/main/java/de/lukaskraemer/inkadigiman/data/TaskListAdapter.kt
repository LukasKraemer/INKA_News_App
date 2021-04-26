package de.lukaskraemer.inkadigiman.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.lukaskraemer.inkadigiman.R
import de.lukaskraemer.inkadigiman.data.database.Task
import java.util.*
import kotlin.collections.ArrayList


class TaskListAdapter(var content: ArrayList<Task>) :
    RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    // Interface:
    private lateinit var mItemListener: OnItemClickListener
    private lateinit var mItemLongListener: OnItemLongClickListener
    private lateinit var rootview: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_appointment,
            parent,
            false
        )
        rootview = parent
        return ViewHolder(view, mItemListener, mItemLongListener)
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onBindViewHolder(holder: TaskListAdapter.ViewHolder, position: Int) {
        val task = content[position]
        val timer = Calendar.getInstance()
        timer.timeInMillis = task.time
        holder.itemType.text =
            rootview.resources.getStringArray(R.array.kindoftask)[task.type].toString()
        holder.itemDate.text =
            timer.get(Calendar.YEAR).toString() + ". " + timer.get(Calendar.MONTH).toString() + ". " + timer.get(Calendar.DAY_OF_MONTH).toString()

        //val minutes = if (task.minutes < 10) {
        //    "0" + task.minutes.toString()
        //} else {
        //    task.minutes.toString()
        //}
        holder.itemTime.text =  timer.get(Calendar.HOUR_OF_DAY).toString() + ":" +  timer.get(Calendar.MINUTE).toString()

    }

    class ViewHolder(
        itemView: View,
        mItemListener: OnItemClickListener,
        mItemLongListener: OnItemLongClickListener
    ) :
        RecyclerView.ViewHolder(itemView) {
        val itemType: TextView = itemView.findViewById(R.id.item_type)
        val itemTime: TextView = itemView.findViewById(R.id.item_time)
        val itemDate: TextView = itemView.findViewById(R.id.item_date)

        init {
            // Implement simple OnClickListener for each Entry
            itemView.setOnClickListener {
                mItemListener.setOnItemClickListener(adapterPosition)
            }

            // Implement simple OnLongClickListener for each Entry:
            itemView.setOnLongClickListener {
                mItemLongListener.setOnItemLongClickListener(adapterPosition)
                true
            }

        }
    }

    fun updateContent(content: ArrayList<Task>) {
        this.content = content
        notifyDataSetChanged()
    }

    // OnItemClickListener:
    interface OnItemClickListener {
        fun setOnItemClickListener(pos: Int)
    }

    fun setOnItemClickListener(mItemListener: OnItemClickListener) {
        this.mItemListener = mItemListener
    }

    // OnLongItemClickListener:
    interface OnItemLongClickListener {
        fun setOnItemLongClickListener(pos: Int)
    }

    fun setOnItemLongClickListener(mItemLongListener: OnItemLongClickListener) {
        this.mItemLongListener = mItemLongListener
    }

}
