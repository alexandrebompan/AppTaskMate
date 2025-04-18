package com.example.ProjetoFelipeKotlin;

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.ProjetoFelipeKotlin.dao.DataBase
import com.example.ProjetoFelipeKotlin.databinding.ActivityAddBinding
import com.example.ProjetoFelipeKotlin.entities.Task
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var db : DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, DataBase::class.java, "tasks.db").build()

        binding.saveButton.setOnClickListener { // Esse botão  vai pegar os dados inseridos e criar uma nova tarefa
            val title = binding.editTitle.text.toString().trim()
            val description = binding.editDescription.text.toString().trim()


            if (title.isNotBlank() && description.isNotBlank()) { // Se os campos não estiverem vazios, a tarefa é criada e salva
                val task = Task(title = title, description = description)
                lifecycleScope.launch {
                    db.daoTask().insertTask(task)
                    Toast.makeText(applicationContext, "Tarefa adicionada", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else { // Se algum campo estiver vazio, aparece uma mensagem de erro
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}