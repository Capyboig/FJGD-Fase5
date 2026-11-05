package com.kuro.fjgd_fase5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kuro.fjgd_fase5.Data.Data
import com.kuro.fjgd_fase5.Data.PeliculaRepository
import com.kuro.fjgd_fase5.ui.Screens.FormularioAgregarPeliculaScreen
import com.kuro.fjgd_fase5.ui.Screens.ListaPeliculasScreen
import com.kuro.fjgd_fase5.ui.ViewModel.PeliculaViewModel
import com.kuro.fjgd_fase5.ui.ViewModel.PeliculaViewModelFactory


class MainActivity : ComponentActivity() {

    private val viewModel: PeliculaViewModel by viewModels {
        val dbHelper = Data(applicationContext)
        val repository = PeliculaRepository(dbHelper)
        PeliculaViewModelFactory(repository)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Estado simple para alternar entre Lista (false) y Formulario (true)
                var mostrarFormulario by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "DAMFlix") },
                            navigationIcon = {
                                if (mostrarFormulario) {
                                    IconButton(onClick = { mostrarFormulario = false }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Volver"
                                        )
                                    }
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        // Solo mostramos el botón '+' si estamos en la lista
                        if (!mostrarFormulario) {
                            FloatingActionButton(onClick = { mostrarFormulario = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Añadir Película"
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (mostrarFormulario) {
                            FormularioAgregarPeliculaScreen(
                                viewModel = viewModel,
                                onPeliculaGuardada = {
                                    // Al guardar con éxito, regresamos automáticamente a la lista
                                    mostrarFormulario = false
                                }
                            )
                        } else {
                            ListaPeliculasScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}