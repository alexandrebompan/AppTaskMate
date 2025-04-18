package com.example.ProjetoFelipeKotlin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.ProjetoFelipeKotlin.dao.DataBase
import com.example.ProjetoFelipeKotlin.databinding.ActivityEditBinding
import com.example.ProjetoFelipeKotlin.entities.Task
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var db : DataBase
    private var taskId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, DataBase::class.java, "tasks.db").build()

        // Pegando os dados da tarefa passada pela MainActivity
        taskId = intent.getLongExtra("TASK_ID", 0L)
        val title = intent.getStringExtra("TASK_TITLE") ?: ""
        val description = intent.getStringExtra("TASK_DESCRIPTION") ?: ""

        // Preenchendo os campos com os dados da tarefa
        binding.editTitle.setText(title)
        binding.editDescription.setText(description)

        binding.saveButton.setOnClickListener { // Botao de atualizar
            val updatedTask = Task ( // Criando a tarefa atualizada
                id = taskId,
                title = binding.editTitle.text.toString().trim(),
                description = binding.editDescription.text.toString().trim()
            )
            lifecycleScope.launch { // Usando o lifecycle para uma operação async (JS) no banco de dados
                db.daoTask().updateTask(updatedTask)
                Toast.makeText(applicationContext, "A tarefa foi atualizada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.deleteButton.setOnClickListener { // Botao de excluir
            val taskToDelete = Task(taskId, title, description) // Criando uma tarefa com os dados da tarefa para deletar
            lifecycleScope.launch {
                db.daoTask().deleteTask(taskToDelete) // Deletando a tarefa no banco
                Toast.makeText(applicationContext, "A tarefa foi excluida", Toast.LENGTH_SHORT).show() // Só mostra que foi excluida
                finish()
            }
        }


    }
}