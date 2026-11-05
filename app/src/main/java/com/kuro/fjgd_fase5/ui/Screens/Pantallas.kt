package com.kuro.fjgd_fase5.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuro.fjgd_fase5.R
import com.kuro.fjgd_fase5.ui.ViewModel.PeliculaViewModel

@Composable
fun FormularioAgregarPeliculaScreen(
    viewModel: PeliculaViewModel,
    onPeliculaGuardada: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var titulo by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var anioTexto by remember { mutableStateOf("") }
    var esVista by remember { mutableStateOf(false) }

    val esValido = titulo.isNotBlank() && director.isNotBlank() && anioTexto.toIntOrNull() != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.title_add_movie),
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text(stringResource(R.string.label_movie_title)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            label = { Text(stringResource(R.string.label_director)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = anioTexto,
            onValueChange = { anioTexto = it },
            label = { Text(stringResource(R.string.label_year)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(checked = esVista, onCheckedChange = { esVista = it })
            Text(text = stringResource(R.string.chk_watched))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val anio = anioTexto.toIntOrNull() ?: 0
                viewModel.agregarPelicula(titulo, director, anio, esVista)
                titulo = ""
                director = ""
                anioTexto = ""
                esVista = false
                onPeliculaGuardada()
            },
            enabled = esValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_save))
        }
    }
}

@Composable
fun ListaPeliculasScreen(
    viewModel: PeliculaViewModel,
    modifier: Modifier = Modifier
) {
    val peliculas by viewModel.listaPeliculas.collectAsStateWithLifecycle()

    if (peliculas.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.empty_list))
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = peliculas, key = { it.id }) { pelicula ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = pelicula.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${pelicula.director} • ${pelicula.anio}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        IconButton(onClick = { viewModel.conmutarFavorito(pelicula) }) {
                            Icon(
                                imageVector = if (pelicula.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = stringResource(R.string.cd_fav_icon),
                                tint = if (pelicula.esFavorita) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
