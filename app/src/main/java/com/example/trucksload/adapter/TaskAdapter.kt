package com.example.trucksload.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksload.R
import com.example.trucksload.data.Element
import com.example.trucksload.data.Task
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskAdapter(private val sharedViewModel: SharedViewModel) :
    RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {
    private val taskList = listOf<Task>(
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123"))),
        Task("123", "123", "1231", listOf<Element>(Element("123", "123", "123")))
    )
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
        myViewHolder.nameTextView.text = "Zamówienie ${taskList[position].name}"
        myViewHolder.taskCard.setOnClickListener {
            onItemClick?.invoke(taskList[position])
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}














