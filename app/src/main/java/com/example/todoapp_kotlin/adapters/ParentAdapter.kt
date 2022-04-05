package com.example.todoapp_kotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp_kotlin.R
import com.example.todoapp_kotlin.database.entities.Parent
import com.example.todoapp_kotlin.database.entities.Task
import com.example.todoapp_kotlin.pages.addTaskPage.AddTaskActivity
import com.example.todoapp_kotlin.pages.mainPage.fragments.MonthFragment

class ParentAdapter(val context: Context): RecyclerView.Adapter<ParentAdapter.ParentViewHolder>(),
    TaskAdapter.TaskClickInterface, TaskAdapter.TaskDoneClickInterface {

    // on below line we are creating a
    // variable for our all notes list.
    val allParent = ArrayList<Parent>()
    lateinit var taskAdapter : TaskAdapter


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        // inflating our layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent, false
        )
        return ParentViewHolder(itemView)    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.title.text = allParent[position].title

        holder.recyclerview.layoutManager = LinearLayoutManager(context)
        taskAdapter = TaskAdapter( holder.recyclerview.context,this,this)
        taskAdapter.updateList(allParent[position].tasks)
        holder.recyclerview.adapter = taskAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = taskAdapter.allTasks[viewHolder.adapterPosition]
                val fragment = MonthFragment()
                fragment.deleteTask(task)            }
        }).attachToRecyclerView(holder.recyclerview)
    }

    override fun getItemCount() = allParent.size

    // below method is use to update our list of notes.
    fun updateList(newList: List<Parent>) {
        // on below line we are clearing
        // our notes array list
        allParent.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allParent.addAll(newList)
    }

    inner class ParentViewHolder(itView: View) :
        RecyclerView.ViewHolder(itView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val recyclerview = itemView.findViewById<RecyclerView>(R.id.tasks)
    }

    override fun onEditClick(task: Task) {
        val intent = Intent(context, AddTaskActivity::class.java)
        intent.putExtra("id",task.idTask.toString())
        intent.putExtra("title",task.title)
        intent.putExtra("description",task.description)
        intent.putExtra("category",task.categoryName)
        intent.putExtra("color",task.categoryColor)
        intent.putExtra("catevoryid",task.categoryId)
        intent.putExtra("date",task.date)
        intent.putExtra("time",task.time)
        intent.putExtra("state",task.state.toString())
        context.startActivity(intent)
    }

    override fun onDoneClick(task: Task) {
        val fragment = MonthFragment()
        fragment.doneTask(task)
    }

    fun MonthFragment.deleteTask(task:Task){
        viewModel?.deleteTask(task)
    }

    fun MonthFragment.doneTask(task:Task){
        if(task.state==0) task.state=1
        else task.state=0
        viewModel.updateTask(task)    }
}