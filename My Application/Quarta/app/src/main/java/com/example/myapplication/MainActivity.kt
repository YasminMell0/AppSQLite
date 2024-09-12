package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.roomDB.Pessoa
import com.example.myapplication.roomDB.PessoaDataBase
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewModel.PessoaViewModel
import com.example.myapplication.viewModel.Repository

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PessoaDataBase::class.java,
            "pessoa.db"
        ).build()
    }

    private val viewModel by viewModels<PessoaViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T:ViewModel> create(modelClass: Class<T>): T{
                    return PessoaViewModel(Repository(db)) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(viewModel, this)
                }
            }
        }
    }
}

@Composable
fun App(viewModel: PessoaViewModel, mainActivity: MainActivity) {
    var nome by remember {
        mutableStateOf("")
    }
    var telefone by remember {
        mutableStateOf("")
    }

    val pessoa = Pessoa(
        nome,
        telefone
    )

    var pessoaList by remember {
        mutableStateOf(listOf<Pessoa>())
    }
    
    viewModel.getPessoa().observe(mainActivity){
        pessoaList = it
    }

    Column(
        Modifier

            .background(Color.Black)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            Modifier.fillMaxWidth(),
            Arrangement.Center
        ) {
            Text(
                text = "App Database",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome:") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = telefone,
            onValueChange = { telefone = it },
            label = { Text("Telefone:") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.upsertPessoa(pessoa)
                nome =""
                telefone = ""
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Cadastrar")
        }

        Row(
            Modifier
                .fillMaxWidth(),
            Arrangement.Center
        ) {
            Column (
                Modifier
                    .fillMaxWidth(0.5f)
            ){
                Text(text = "Nome")
            }
            Column(
                Modifier
                    .fillMaxWidth(0.5f)
            ){
                Text(text = "telefone")
            }
        }
        Divider()
        LazyColumn {
            items(pessoaList){ pessoa ->
                Row(
                    Modifier
                        .fillMaxWidth(),
                    Arrangement.Center
                    
                ) {
                    Column (
                        Modifier
                            .fillMaxWidth(0.5f)
                        ){
                        Text(text = "${pessoa.nome}")
                        
                    }
                    Column(
                        Modifier
                            .fillMaxWidth(0.5f)
                    ){
                        Text(text = "${pessoa.telefone}")
                    }
                }
            }
        }
        Divider()
    }
}
