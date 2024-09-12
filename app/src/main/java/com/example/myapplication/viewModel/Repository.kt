package com.example.myapplication.viewModel;

import com.example.myapplication.roomDB.Pessoa
import com.example.myapplication.roomDB.PessoaDataBase

public class Repository (private val db : PessoaDataBase){
    suspend fun upsertPessoa(pessoa: Pessoa){
        db.pessoaDao().upsertPessoa(pessoa)
    }

    suspend fun deletePessoa(pessoa : Pessoa){
        db.pessoaDao().deletePessoa(pessoa)
    }

    fun getAllPessoa() = db.pessoaDao().getAllPessoa()
}
