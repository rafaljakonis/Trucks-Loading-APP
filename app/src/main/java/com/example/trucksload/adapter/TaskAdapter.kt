package com.example.trucksload.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksload.R
import com.example.trucksload.data.model.Task
import com.google.android.material.card.MaterialCardView

class TaskAdapter(private var taskList: ArrayList<Task>) :
    RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    var onItemClick: ((Task) -> Unit)? = null

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val elementCard: MaterialCardView
        val elementLinearLayout: LinearLayout
        val elementName: TextView
        val elementDescription: TextView
        val elementLocation: TextView
        val elementStatus: TextView
        val context: Context

        init {
            elementCard = view.findViewById(R.id.element_material_card)
            elementLinearLayout = view.findViewById(R.id.element_linear_layout)
            elementName = view.findViewById(R.id.element_name)
            elementDescription = view.findViewById(R.id.element_description)
            elementLocation = view.findViewById(R.id.element_location)
            elementStatus = view.findViewById(R.id.element_status)
            context = view.context
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_task_element, viewGroup, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, position: Int) {
        myViewHolder.elementName.text = "Zamówienie ${taskList[position].id}"
        myViewHolder.elementDescription.text = taskList[position].description
        myViewHolder.elementLocation.text = "Lokalizacja: ${taskList[position].location}"
        myViewHolder.elementStatus.text = "Status: ${taskList[position].statusName}"

        taskList[position].elements.forEach {
            val temporaryTextView: TextView = TextView(myViewHolder.context)
            var temporaryDrawable: Int = R.drawable.ic_check_green

            if (it.isComplete == 0) {
                temporaryDrawable = R.drawable.ic_close_red
            }

            temporaryTextView.width = ViewGroup.LayoutParams.WRAP_CONTENT
            temporaryTextView.text = "- ${it.name}"
            temporaryTextView.setPadding(40, 0, 40, 0)
            temporaryTextView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                temporaryDrawable,
                0
            )
            myViewHolder.elementLinearLayout.addView(temporaryTextView)
        }

        myViewHolder.elementCard.setOnClickListener {
            onItemClick?.invoke(taskList[position])
        }

    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}














