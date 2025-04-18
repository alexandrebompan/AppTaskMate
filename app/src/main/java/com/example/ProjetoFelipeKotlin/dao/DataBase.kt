package com.example.ProjetoFelipeKotlin.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ProjetoFelipeKotlin.entities.Task

@Database(entities = [Task::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun daoTask(): DaoTask
}