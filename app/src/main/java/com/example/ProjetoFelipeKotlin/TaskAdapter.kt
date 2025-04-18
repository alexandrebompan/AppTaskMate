package com.example.ProjetoFelipeKotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ProjetoFelipeKotlin.databinding.ItemTaskBinding
import com.example.ProjetoFelipeKotlin.entities.Task

class TaskAdapter (
    private var tasks: List<Task>, // Lista de tarefas criada
    private val onItemClick: (Task) -> Unit // Função chamada ao clicar em um item
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    inner class TaskViewHolder(val binding: ItemTaskBinding ) : RecyclerView.ViewHolder(binding.root)

    // Cria o ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    // Associa dados à view
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.taskTitle.text = task.title
        holder.binding.taskDescription.text = task.description
        holder.binding.root.setOnClickListener { onItemClick(task) }
    }

    // Retorna a quantidade de itens na lista
    override fun getItemCount() = tasks.size

    // Atualiza a lista de tarefas
    fun updateList(newList: List<Task>) {
        tasks = newList
        notifyDataSetChanged() // Atualiza o RecyclerView
    }
}