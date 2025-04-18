package com.example.ProjetoFelipeKotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.ProjetoFelipeKotlin.dao.DataBase
import com.example.ProjetoFelipeKotlin.databinding.ActivityMainBinding
import com.example.ProjetoFelipeKotlin.entities.Task
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() { // Optei usar o Empty Activity e não o JetPack Compose

    private lateinit var binding : ActivityMainBinding // liga o layout (XML) com o código em kotlin

    private lateinit var db: DataBase // Iniciamos nosso banco de dados local

    private lateinit var adapter: TaskAdapter // Adaptador que vai mostrar as tarefas na lista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, DataBase::class.java, "tasks.db").build() // Inicializando o DataBase com o Room [Obrigado KSP e prof. Felipe]

        adapter = TaskAdapter(emptyList()) { task -> // RecyclerView já pronto pra abrir a tela de edição quando clicar numa tarefa

            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("TASK_ID", task.id)
                putExtra("TASK_TITLE", task.title)
                putExtra("TASK_DESCRIPTION", task.description)
            }
            startActivity(intent)
        }

        // Configuração da RecyclerView (como se fosse a lista onde vão aparecer as tarefas)
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        binding.openAddActivityButton.setOnClickListener { // Para abrir a tela AddActivity
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        binding.addButon.setOnClickListener { // Esse botão  é pra adicionar uma tarefa diretamente na MainActivity (sem ir pra outra tela)
            val title = binding.editTitle.text.toString().trim()
            val description = binding.editDescription.text.toString().trim()

            if (title.isNotBlank() && description.isNotBlank()) { // Faz uma simples verificação dos campos
                val task = Task(title = title, description = description)
                insertTask(task)
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Função que insere a tarefa no banco de dados local
    private fun insertTask(task: Task) {
        lifecycleScope.launch {
            try {
                db.daoTask().insertTask(task) // Faz a inserção no banco
                loadTasks() // Recarrega a lista
                Toast.makeText(applicationContext, "Tarefa adicionada!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "Algo deu errado!", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Essa função é chamada sempre que a gente volta pra MainActivity, por exemplo depois de editar ou adicionar uma tarefa
    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    // Essa função busca todas as tarefas do banco e atualiza o adapter pra mostrar na tela
    private fun loadTasks() {
        lifecycleScope.launch {
            val tasks = db.daoTask().listTask()
            adapter.updateList(tasks)
        }
    }
}