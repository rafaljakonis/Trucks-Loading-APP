package com.example.trucksload.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksload.R
import com.example.trucksload.data.model.Element
import com.example.trucksload.data.model.Task
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.card.MaterialCardView

class TaskAdapter(private var taskList:ArrayList<Task>) :
    RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    var onItemClick: ((Task) -> Unit)? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView
        val taskCard: MaterialCardView

        init {
            nameTextView = view.findViewById(R.id.task_name)
            taskCard = view.findViewById(R.id.task_card)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_task_element, viewGroup, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        myViewHolder.nameTextView.text = "Zamówienie ${taskList[position].id}"
        myViewHolder.taskCard.setOnClickListener {
            onItemClick?.invoke(taskList[position])
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}














